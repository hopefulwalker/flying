/*
 Created by Walker.Zhang on 2015/2/27.
 Revision History:
 Date          Who              Version      What
 2015/2/27     Walker.Zhang     0.1.0        Created.
 2015/6/15     Walker.Zhang     0.2.0        Simplify the implementation of setEndpoints because Endpoint overrides hashCode and equals.
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.*;

import java.util.ArrayList;
import java.util.List;

/**
 * todo refactor this engine to support CONNECT settings.
 * the background ZMQ.Socket is a router socket. when the peer is dead, the message will silently discard the message.
 * so the client should call with timeout parameters.
 */
public class AsyncClientEngine implements IClientEngine {
    private static final Logger logger = LoggerFactory.getLogger(AsyncClientEngine.class);
    //  If not a single service replies within this time, give up
    private boolean running = false;
    private long sequence = 0L;
    //  This API works in two halves, a common pattern for APIs that need to
    //  run in the background. One half is an frontend object our application
    //  creates and works with; the other half is a backend "agent" that runs
    //  in a background thread. The frontend talks to the backend over an
    //  inproc pipe socket:
    //  Structure of our frontend class
    private ZContext context;            //  Our context wrapper
    private ZMQ.Socket pipe;             //  Pipe through to background
    private Dispatcher dispatcher;
    private List<IEndpoint> endpoints;
    private IMsgEventListener msgEventListener;

    private ZLoop.IZLoopHandler requestHandler;
    private ZLoop.IZLoopHandler replyHandler;

    public AsyncClientEngine(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public AsyncClientEngine(List<IEndpoint> endpoints, IMsgEventListener msgEventListener) {
        this.endpoints = endpoints;
        this.msgEventListener = msgEventListener;
    }

    public ZContext getContext() {
        return context;
    }

    @Override
    public List<IEndpoint> getEndpoints() {
        return this.endpoints;
    }

    @Override
    public void setEndpoints(List<IEndpoint> endpoints) {
        if (running) {
            for (IEndpoint newEndpoint : endpoints)
                if (!this.endpoints.contains(newEndpoint)) connect(newEndpoint);
        }
        this.endpoints = endpoints;
    }

    /**
     * @param msgEvent the message that need to be sent out.
     * @param timeout  it shall wait timeout milliseconds for the reply to come back.
     *                 If the value of timeout is 0, it shall return immediately.
     *                 If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event.
     */
    @Override
    public IMsgEvent request(IMsgEvent msgEvent, int timeout) {
        // send request. setup the command and the msgID(for the corresponding reply).
        ZMsg reqMsg = new ZMsg();
        reqMsg.add(Ints.toByteArray(IMsgEvent.ID_REQUEST));
        long msgNO = ++sequence;
        reqMsg.add(Longs.toByteArray(msgNO));
        reqMsg.add(msgEvent.getInfo().getByteArray());
        reqMsg.send(pipe);
        reqMsg.destroy();
        // receive reply.
        MsgEvent event = null;
        ZMsg repMsg;
        int oldTimeout = pipe.getReceiveTimeOut();
        int waitTime;
        long startTime = System.currentTimeMillis();
        do {
            waitTime = (int) (timeout - (System.currentTimeMillis() - startTime));
            if (waitTime < 0) waitTime = 0;
            pipe.setReceiveTimeOut(waitTime);
            repMsg = ZMsg.recvMsg(pipe);
            if (repMsg == null) {
                event = new MsgEvent(IMsgEvent.ID_REPLY_TIMEOUT, this, new MsgEventInfo(new byte[0]));
                break;
            }
            int answer = Ints.fromByteArray(repMsg.pop().getData());
            // check the message NO.
            if (msgNO != Longs.fromByteArray(repMsg.pop().getData())) {
                repMsg.destroy();
                continue;
            }
            if (answer == IMsgEvent.ID_REPLY_SUCCEED) {
                event = new MsgEvent(answer, this, new MsgEventInfo(repMsg.pop().getData()));
            } else {
                event = new MsgEvent(answer, this, new MsgEventInfo(new byte[0]));
            }
            repMsg.destroy();
            break;
        } while (System.currentTimeMillis() < startTime + timeout);
        pipe.setReceiveTimeOut(oldTimeout);
        return event;
    }

    /**
     * To implement the request method, the frontend object sends a message
     * to the backend, specifying a command "REQ" and the request message:
     *
     * @param msgEvent the msg event that need to be sent.
     */
    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        // send request. setup the command.
        ZMsg reqMsg = new ZMsg();
        reqMsg.add(Ints.toByteArray(IMsgEvent.ID_REQUEST));
        reqMsg.add(Longs.toByteArray(++sequence));
        reqMsg.add(msgEvent.getInfo().getByteArray());
        assert (pipe != null);
        reqMsg.send(pipe);
        reqMsg.destroy();
    }

    /**
     * Poll socket for a reply, with timeout
     *
     * @param timeout timeout in milliseconds, same as the interface definition.
     * @return message event. null when time out.
     */
    @Override
    public IMsgEvent recvMsg(int timeout) {
        long startMills = System.currentTimeMillis();
        long pollTime;
        if (timeout < 0) timeout = 0;
        MsgEvent event;
        ZMQ.Poller items = new ZMQ.Poller(1);
        items.register(pipe, ZMQ.Poller.POLLIN);
        ZMsg repMsg;
        // setup the pollTime var. just in case pollTime = -1, the poll method will wait forever.
        pollTime = startMills + timeout - System.currentTimeMillis();
        if (pollTime < 0) pollTime = 0;
        int rc = items.poll(pollTime);
        if (rc == -1) {
            event = new MsgEvent(IMsgEvent.ID_REPLY_FAILED, this, new MsgEventInfo(new byte[0]));
            return event;
        }
        if (rc == 0) {
            event = new MsgEvent(IMsgEvent.ID_REPLY_TIMEOUT, this, new MsgEventInfo(new byte[0]));
            return event;
        }
        repMsg = ZMsg.recvMsg(pipe);
        int answer = Ints.fromByteArray(repMsg.pop().getData());
        repMsg.pop(); // pop up the msgNO;
        if (answer == IMsgEvent.ID_REPLY_SUCCEED) {
            event = new MsgEvent(answer, this, new MsgEventInfo(repMsg.pop().getData()));
        } else {
            event = new MsgEvent(answer, this, new MsgEventInfo(new byte[0]));
        }
        repMsg.destroy();
        return event;
    }

    @Override
    public void start() {
        if (running) {
            logger.info("Service is already running ...");
            return;
        }
        if (endpoints.size() <= 0) {
            logger.info("Please setup endpoint before you start.");
            return;
        }
        context = new ZContext();
        pipe = context.createSocket(ZMQ.PAIR);
        IEndpoint pipeEndpoint = new Endpoint(String.format("inproc://zctx-pipe-%d", pipe.hashCode()));
        pipe.bind(pipeEndpoint.asString());
        List<IEndpoint> pipeEndpointAdapter = new ArrayList<>(1);
        pipeEndpointAdapter.add(pipeEndpoint);

        dispatcher = new Dispatcher(this);
        requestHandler = new RequestHandler(dispatcher, endpoints);
        replyHandler = new ReplyHandler(dispatcher, pipeEndpointAdapter);

        dispatcher.addZLoopHandler(pipeEndpointAdapter, requestHandler, ZMQ.PAIR);
        dispatcher.addZLoopHandler(endpoints, replyHandler, ZMQ.ROUTER);

        dispatcher.addMsgEventListener(endpoints, new IMsgEventListener() {
            @Override
            public IMsgEventResult onEvent(IMsgEvent event) {
                logger.warn("Don't forget this");
                return null;
            }
        });
        dispatcher.sendMsg(endpoints, new MsgEvent(IMsgEvent.ID_MESSAGE, this, new MsgEventInfo(new byte[0])));
        new Thread(dispatcher).start();
        running = true;
    }

    @Override
    public void stop() {
        if (!running) {
            logger.info("Service is not running now.");
            return;
        }
        context.destroy();
        running = false;
    }

    private void connect(IEndpoint endpoint) {
        ZMsg msg = new ZMsg();
        msg.add(Ints.toByteArray(IMsgEvent.ID_CONNECT));
        msg.add(endpoint.asString());
        msg.send(pipe);
        msg.destroy();
    }

    private class RequestHandler implements ZLoop.IZLoopHandler {
        private Dispatcher dispatcher;
        private List<IEndpoint> endpoints;

        public RequestHandler(Dispatcher dispatcher, List<IEndpoint> endpoints) {
            this.dispatcher = dispatcher;
            this.endpoints = endpoints;
        }

        @Override
        public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object o) {
            ZMQ.Socket pipe = pollItem.getSocket();
            ZMsg msg = ZMsg.recvMsg(pipe);
            dispatcher.sendMsg(endpoints, msg);
            msg.destroy();
            return 0;
        }
    }

    private class ReplyHandler implements ZLoop.IZLoopHandler {
        private IMsgEventListener msgEventListener;

        public ReplyHandler(IMsgEventListener msgEventListener) {
            this.msgEventListener = msgEventListener;
        }

        @Override
        public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object o) {
            ZMQ.Socket socket = pollItem.getSocket();
            ZMsg msg = ZMsg.recvMsg(socket);
            //  Frame 0 is the identity of server that replied
            String endpoint = msg.popString();
            //  Frame 1 may be the command, we only handler PING ourselves, others should be route out.
            ZFrame command = msg.pop();
            // Frame 2 is msgNO.
            msg.pop();
            // Frame 3 is real data.
            MsgEvent event = new MsgEvent(IMsgEvent.ID_MESSAGE, engine, new MsgEventInfo(msg.pop().getData()));
            msgEventListener.onEvent(event);
            msg.destroy();
            return 0;
        }
    }
}
