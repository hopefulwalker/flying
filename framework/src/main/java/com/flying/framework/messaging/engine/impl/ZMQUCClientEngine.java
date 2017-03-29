/*
 Created by Walker.Zhang on 2015/2/27.
 Revision History:
 Date          Who              Version      What
 2015/2/27     Walker.Zhang     0.1.0        Created.
 2015/6/15     Walker.Zhang     0.2.0        Simplify the implementation of setEndpoints because Endpoint overrides hashCode and equals.
 */
package com.flying.framework.messaging.engine.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * todo refactor this engine to support CONNECT settings.
 * the background ZMQ.Socket is a router socket. when the peer is dead, the message will silently discard the message.
 * so the client should call with timeout parameters.
 */
public class ZMQUCClientEngine implements IClientEngine {
    private static final Logger logger = LoggerFactory.getLogger(ZMQUCClientEngine.class);
    //  If not a single service replies within this time, give up
    private static int serverDisconnectTtl = 1 * 60 * 1000;
    //  Server considered dead if silent for this long
    private static int serverTtl = 30 * 1000;
    //  PING interval for servers we think are alive
    private static int serverPingInterval = 5 * 1000;
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
    private List<IEndpoint> endpoints;

    public ZMQUCClientEngine(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public void setServerPingInterval(int pingInterval) {
        if (running) {
            logger.info("Please stop the engine before setting...");
            return;
        }
        serverPingInterval = pingInterval;
    }

    public void setServerTtl(int ttl) {
        if (running) {
            logger.info("Please stop the engine before setting...");
            return;
        }
        serverTtl = ttl;
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

//    public IMsgEvent request(IMsgEvent msgEvent, int timeout) {
//        // send request. setup the command and the msgID(for the corresponding reply).
//        ZMsg reqMsg = new ZMsg();
//        reqMsg.add(IMsgEvent.NAME_REQUEST);
//        long msgNO = ++sequence;
//        reqMsg.add(Longs.toByteArray(msgNO));
//        reqMsg.add(msgEvent.getEventInfo().getByteArray());
//        reqMsg.send(pipe);
//        reqMsg.destroy();
//        // receive reply.
//        long startMills = System.currentTimeMillis();
//        long pollTime;
//        if (timeout < 0) timeout = 0;
//        MsgEvent event = null;
//        ZMQ.Poller items = new ZMQ.Poller(1);
//        items.register(pipe, ZMQ.Poller.POLLIN);
//        ZMsg repMsg;
//        while (true) {
//            // setup the pollTime var. just in case pollTime = -1, the poll method will wait forever.
//            pollTime = startMills + timeout - System.currentTimeMillis();
//            if (pollTime < 0) pollTime = 0;
//            int rc = items.poll(pollTime);
//            if (rc == -1) { // Context has been shut down
//                event = new MsgEvent(IMsgEvent.NAME_REPLY_FAILED, this, new MsgEventInfo(new byte[0]));
//                break;
//            }
//            if (rc == 0) {  // no reply.
//                event = new MsgEvent(IMsgEvent.NAME_REPLY_TIMEOUT, this, new MsgEventInfo(new byte[0]));
//                break;
//            }
//            repMsg = ZMsg.recvMsg(pipe);
//            String answer = repMsg.popString();
//            // check the message NO.
//            if (msgNO != Longs.fromByteArray(repMsg.pop().getData())) {
//                repMsg.destroy();
//                continue;
//            }
//            if (answer.equals(IMsgEvent.NAME_REPLY_SUCCEED)) {
//                event = new MsgEvent(answer, this, new MsgEventInfo(repMsg.pop().getData()));
//            } else {
//                event = new MsgEvent(answer, this, new MsgEventInfo(new byte[0]));
//            }
//            repMsg.destroy();
//            break;
//        }
//        return event;
//    }

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
        reqMsg.add(msgEvent.getEventInfo().getByteArray());
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
        reqMsg.add(msgEvent.getEventInfo().getByteArray());
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
        FreelanceAgent agent = new FreelanceAgent();
        pipe = ZThread.fork(context, agent);
        // issue connect commands for all endpoints
        endpoints.forEach(this::connect);
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
        msg.add(endpoint.getEndpoint());
        msg.send(pipe);
        msg.destroy();
    }

    //  Here we see the backend agent. It runs as an attached thread, talking
    //  to its parent over a pipe socket. It is a fairly complex piece of work
    //  so we'll break it down into pieces. First, the agent manages a set of
    //  servers, using our familiar class approach:

    //  Simple class for one server we talk to
    private static class Server {
        private String endpoint;        //  Server identity/endpoint
        private long pingAt;            //  Next ping at this time
        private long expires;           //  Expires at this time
        private long disconnectAt;      //  Disconnect at this time

        protected Server(String endpoint) {
            this.endpoint = endpoint;
            refresh();
        }

        private void refresh() {
            pingAt = System.currentTimeMillis() + ZMQUCClientEngine.serverPingInterval;
            expires = System.currentTimeMillis() + ZMQUCClientEngine.serverTtl;
            disconnectAt = System.currentTimeMillis() + ZMQUCClientEngine.serverDisconnectTtl;
        }

        private void refreshPingAt() {
            pingAt = System.currentTimeMillis() + ZMQUCClientEngine.serverPingInterval;
        }

        protected void destroy() {
        }

        private void ping(ZMQ.Socket socket) {
            if (System.currentTimeMillis() >= pingAt) {
                ZMsg ping = new ZMsg();
                ping.add(endpoint);
                ping.add(Ints.toByteArray(IMsgEvent.ID_PING));
                ping.add(Longs.toByteArray(System.currentTimeMillis()));
                ping.send(socket);
                refreshPingAt();
            }
        }

        private long tickless(long tickless) {
            return (tickless > pingAt ? pingAt : tickless);
        }
    }

    //  We build the agent as a class that's capable of processing messages
    //  coming in from its various sockets:

    //  Simple class for one background agent
    private static class Agent {
        private ZContext ctx;                   //  Own context
        private ZMQ.Socket pipe;                //  Socket to talk back to application
        private ZMQ.Socket router;              //  Socket to talk to servers
        private Map<String, Server> servers;    //  Servers we've connected to
        private List<Server> actives;           //  Servers we know are alive

        protected Agent(ZContext ctx, ZMQ.Socket pipe) {
            this.ctx = ctx;
            this.pipe = pipe;
            router = ctx.createSocket(ZMQ.ROUTER);
            servers = new HashMap<>();
            actives = new ArrayList<>();
        }

        protected void destroy() {
            servers.values().forEach(ZMQUCClientEngine.Server::destroy);
            ctx.destroy();
        }

        private void trackServers() {
            Iterator<Map.Entry<String, Server>> iterator = servers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Server> entry = iterator.next();
                    if (System.currentTimeMillis() > entry.getValue().disconnectAt) {
                        iterator.remove();
                        actives.remove(entry.getValue());
                        router.disconnect(entry.getValue().endpoint);
                        logger.info("ZMQUCClientEngine: disconnect from expired server:" + entry.getValue().endpoint);
                    } else {
                        entry.getValue().ping(router);
                    }
            }
        }

        //  This method processes one message from our frontend class
        //  (it's going to be CNT or REQ):
        //  Callback when we remove server from agent 'servers' hash table
        private void controlMessage() {
            ZMsg msg = ZMsg.recvMsg(pipe);
            int command = Ints.fromByteArray(msg.pop().getData());
            switch (command) {
                case IMsgEvent.ID_CONNECT:
                    String endpoint = msg.popString();
                    if (logger.isDebugEnabled()) {
                        logger.debug("ZMQUCClientEngine: connecting to:" + endpoint);
                    }
                    router.connect(endpoint);
                    Server newServer = new Server(endpoint);
                    servers.put(endpoint, newServer);
                    actives.add(newServer);
                    newServer.refresh();
                    break;
                case IMsgEvent.ID_REQUEST:
                    boolean reqSent = false;
                    while (!actives.isEmpty()) {
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, actives.size());
                        Server server = actives.get(randomIndex);
                        if (System.currentTimeMillis() >= server.expires) {
                            actives.remove(randomIndex);
                            logger.info("ZMQUCClientEngine: remove expired server:" + server.endpoint);
                        } else {
                            msg.push(Ints.toByteArray(IMsgEvent.ID_REQUEST));
                            msg.push(server.endpoint);
                            msg.send(router);
                            reqSent = true;
                            break;
                        }
                    }
                    if (!reqSent) {
                        logger.warn("ZMQUCClientEngine: request didn't send! active server size is :" + actives.size());
                        ZMsg repMsg = new ZMsg();
                        repMsg.add(Ints.toByteArray(IMsgEvent.ID_REPLY_FAILED));
                        repMsg.add(msg.pop());
                        repMsg.send(pipe);
                        repMsg.destroy();
                    }
                    break;
            }
            msg.destroy();
        }

        //  This method processes one message from a connected server:
        private void routerMessage() {
            ZMsg reply = ZMsg.recvMsg(router);
            //  Frame 0 is the identity of server that replied
            String endpoint = reply.popString();
            Server server = servers.get(endpoint);
            assert (server != null);
            if (!actives.contains(server)) actives.add(server);
            server.refresh();
            //  Frame 1 may be the command, we only handler PING ourselves, others should be route out.
            ZFrame command = reply.peekFirst();
            if (command != null && Ints.fromByteArray(command.getData()) != IMsgEvent.ID_PING) {
                reply.send(pipe);
            }
            reply.destroy();
        }
    }

    //  Finally, here's the agent task itself, which polls its two sockets
    //  and processes incoming messages:
    private static class FreelanceAgent implements ZThread.IAttachedRunnable {
        @Override
        public void run(Object[] args, ZContext ctx, ZMQ.Socket pipe) {
            Agent agent = new Agent(ctx, pipe);
            ZMQ.PollItem[] items = {
                    new ZMQ.PollItem(agent.pipe, ZMQ.Poller.POLLIN),
                    new ZMQ.PollItem(agent.router, ZMQ.Poller.POLLIN)
            };
            Thread.currentThread().setName("FreelanceAgent");
            while (!Thread.currentThread().isInterrupted()) {
                //  Calculate tickless timer, up to 1 hour
                long tickless = System.currentTimeMillis() + 1000 * 3600;
                for (Server server : agent.servers.values()) tickless = server.tickless(tickless);
                tickless = tickless - System.currentTimeMillis();
                if (tickless < 0) tickless = 0;
                int rc = ZMQ.poll(items, tickless);
                if (rc == -1) break;              //  Context has been shut down
                if (items[0].isReadable()) agent.controlMessage(); // route out message
                if (items[1].isReadable()) agent.routerMessage();  // route in message
                //  Disconnect and delete any expired servers
                //  Send heartbeats to idle servers if needed
                agent.trackServers();                           // maintain servers status.
            }
            agent.destroy();
        }
    }
}
