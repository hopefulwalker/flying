/*
 Created by Walker.Zhang on 2017/4/25.
 Revision History:
 Date          Who              Version      What
 2017/4/25     Walker.Zhang     0.3.3        Created to support zloop.
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * the background ZMQ.Socket is a router socket. when the peer is dead, the message will silently discard the message.
 * so the client should call with timeout parameters.
 * This API works in two halves, a common pattern for APIs that need to run in the background.
 * One half is an frontend object our application creates and works with;
 * the other half is a backend "agent" that runs in a background thread. The frontend talks to the backend over an
 * inproc pipe socket.
 */
public class AsyncClientEngine implements IClientEngine {
    private static final Logger logger = LoggerFactory.getLogger(AsyncClientEngine.class);
    private volatile boolean running = false;
    private List<IEndpoint> endpoints;

    private ZContext context;            //  Our context wrapper
    private ZMQ.Socket pipe;             //  Pipe through to background

    public AsyncClientEngine(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
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
        this.endpoints = endpoints;
    }

    /**
     * todo this method should removed to sync engine.
     *
     * @param msgEvent the message that need to be sent out.
     * @param timeout  it shall wait timeout milliseconds for the reply to come back.
     *                 If the value of timeout is 0, it shall return immediately.
     *                 If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event.
     */
    @Override
    public IMsgEvent request(IMsgEvent msgEvent, int timeout) {
        // send request.
        ZMsg reqMsg = Codec.encode(msgEvent);
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
                event = MsgEvent.newInstance(IMsgEvent.ID_TIMEOUT, this);
                break;
            }
            Codec.Msg decodedMsg = Codec.decode(repMsg, pipe.getType());
            event = MsgEvent.newInstance(decodedMsg.eventID, this, decodedMsg.data);
            repMsg.destroy();
            break;
        } while (System.currentTimeMillis() < startTime + timeout);
        pipe.setReceiveTimeOut(oldTimeout);
        return event;
    }

    /**
     * send message to the backend
     *
     * @param msgEvent the msg event that need to be sent.
     */
    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        // send request. setup the command.
        ZMsg reqMsg = Codec.encode(msgEvent);
        reqMsg.send(pipe);
        reqMsg.destroy();
    }

    /**
     * Poll socket for a reply, with timeout
     *
     * @param timeout timeout in milliseconds, same as the interface definition.
     * @return message event.
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
            event = MsgEvent.newInstance(IMsgEvent.ID_FAILED, this);
            return event;
        }
        if (rc == 0) {
            event = MsgEvent.newInstance(IMsgEvent.ID_TIMEOUT, this);
            return event;
        }
        repMsg = ZMsg.recvMsg(pipe);
        if (repMsg == null) {
            event = MsgEvent.newInstance(IMsgEvent.ID_TIMEOUT, this);
            return event;
        }
        Codec.Msg decodedMsg = Codec.decode(repMsg, pipe.getType());
        event = MsgEvent.newInstance(decodedMsg.eventID, this, decodedMsg.data);
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
        //setup pipe to connect to dispatcher and remote server.
        pipe = context.createSocket(ZMQ.PAIR);
        IEndpoint pipeEndpoint = new Endpoint(String.format("inproc://zctx-pipe-%d", pipe.hashCode()));
        pipe.bind(pipeEndpoint.asString());
        List<IEndpoint> pipeEndpointAdapter = new ArrayList<>(1);
        pipeEndpointAdapter.add(pipeEndpoint);
        Dispatcher dispatcher = new Dispatcher(this, ZContext.shadow(context));
        ZLoop.IZLoopHandler requestHandler = new RouteHandler(dispatcher, pipeEndpointAdapter, ZMQ.PAIR, false, endpoints, ZMQ.ROUTER, true);
        dispatcher.addZLoopHandler(pipeEndpointAdapter, requestHandler, endpoints);
        ZLoop.IZLoopHandler replyHandler = new RouteHandler(dispatcher, endpoints, ZMQ.ROUTER, true, pipeEndpointAdapter, ZMQ.PAIR, false);
        dispatcher.addZLoopHandler(endpoints, replyHandler, pipeEndpointAdapter);
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

    private class RouteHandler extends AbstractZLoopSocketHandler {
        private List<IEndpoint> tos;
        private int toType;
        private boolean toPing;

        RouteHandler(Dispatcher dispatcher, List<IEndpoint> froms, int fromType, boolean fromPing,
                     List<IEndpoint> tos, int toType, boolean toPing) {
            super(dispatcher, froms, fromType, fromPing);
            this.tos = tos;
            this.toType = toType;
            this.toPing = toPing;
            getDispatcher().connect(tos, toType, toPing);
        }

        @Override
        public ZMsg handle(Codec.Msg decodedMsg, Object arg) {
            getDispatcher().sendMsg(tos, Codec.encode(decodedMsg.others, decodedMsg.address, decodedMsg.eventID, decodedMsg.data));
            return null;
        }
    }
}
