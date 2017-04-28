/*
 Created by Walker.Zhang on 2017/4/25.
 Revision History:
 Date          Who              Version      What
 2017/4/25     Walker.Zhang     0.3.3        Created to support zloop.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Acting as the dispatcher in reactor pattern.
 */
public class Dispatcher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ZContext context;
    private AsyncClientEngine engine;
    private ZLoop reactor;
    private Map<List<IEndpoint>, ZMQ.Socket> sockets;
    private Map<List<IEndpoint>, List<Server>> activeServers;
    private Map<List<IEndpoint>, Map<String, Server>> allServers;

    Dispatcher(AsyncClientEngine engine) {
        this.engine = engine;
        this.context = ZContext.shadow(engine.getContext());
        this.reactor = new ZLoop();
        this.sockets = new HashMap<>();
        this.allServers = new HashMap<>();
        this.activeServers = new HashMap<>();
    }

    void connect(List<IEndpoint> endpoints, int socketType, boolean pingEnabled) {
        if (sockets.containsKey(endpoints)) return;
        ZMQ.Socket socket = context.createSocket(socketType);
        Map<String, Server> serverMap = new HashMap<>();
        List<Server> serverList = new ArrayList<>();
        for (IEndpoint endpoint : endpoints) {
            socket.connect(endpoint.asString());
            Server server = new Server(endpoint.asString(), pingEnabled);
            serverMap.put(endpoint.asString(), server);
            serverList.add(server);
        }
        sockets.put(endpoints, socket);
        allServers.put(endpoints, serverMap);
        activeServers.put(endpoints, serverList);
    }

    int addZLoopHandler(List<IEndpoint> endpoints, ZLoop.IZLoopHandler handler, Object handlerArg) {
        ZMQ.Socket socket = sockets.get(endpoints);
        ZMQ.PollItem pollInput = new ZMQ.PollItem(socket, ZMQ.Poller.POLLIN);
        return reactor.addPoller(pollInput, handler, handlerArg);
    }

    public int addMsgEventListener(List<IEndpoint> froms, int fromType, boolean pingEnabled, IMsgEventListener listener) {
        ZLoop.IZLoopHandler handler = new HandlerAdapter(this, froms, fromType, pingEnabled, listener);
        return addZLoopHandler(froms, handler, this);
    }

    void refreshServer(List<IEndpoint> endpoints, String endpoint) {
        Map<String, Server> serverMap = allServers.get(endpoints);
        List<Server> serverList = activeServers.get(endpoints);
        if (endpoint == null) {
            for (Server server : serverMap.values()) {
                if (!serverList.contains(server)) serverList.add(server);
                server.refresh();
            }
        } else {
            Server server = serverMap.get(endpoint);
            if (!serverList.contains(server)) serverList.add(server);
            server.refresh();
        }
    }

    public void sendMsg(List<IEndpoint> endpoints, ZMsg msg) {
        boolean reqSent = false;
        ZMQ.Socket socket = sockets.get(endpoints);
        if (socket.getType() != ZMQ.ROUTER) {
            msg.send(socket);
            return;
        }
        List<Server> serverList = activeServers.get(endpoints);
        while (!serverList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, serverList.size());
            Server server = serverList.get(randomIndex);
            if (System.currentTimeMillis() >= server.expires) {
                serverList.remove(randomIndex);
                logger.warn("Dispatcher: remove expired server:" + server.endpoint);
            } else {
                Codec.pushAddress(msg, ZMQ.ROUTER, server.endpoint);
                msg.send(socket);
                reqSent = true;
                break;
            }
        }
        if (!reqSent) {
            logger.warn("Dispatcher: request didn't send! active server size is :" + serverList.size());
        }
    }

    public void sendMsg(List<IEndpoint> endpoints, MsgEvent msgEvent) {
        ZMsg msg = Codec.encodeTransMsg(msgEvent.getInfo().getByteArray());
        sendMsg(endpoints, msg);
        msg.destroy();
    }

    @Override
    public void run() {
        try {
            //setup Scavenger
            reactor.addTimer(Server.serverPingInterval, 0, new Scavenger(), this);
            reactor.start();
        } catch (ZMQException zmqe) {
            if (zmqe.getErrorCode() != ZMQ.Error.ETERM.getCode()) {
                logger.error("Error occurs when running reactor!", zmqe);
            }
        }
        context.destroy();
    }

    private void trackServers() {
        for (Map.Entry<List<IEndpoint>, Map<String, Server>> allEntry : allServers.entrySet()) {
            Iterator<Map.Entry<String, Server>> iterator = allEntry.getValue().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Server> entry = iterator.next();
                Server server = entry.getValue();
                if (!server.pingEnabled) continue;
                if (System.currentTimeMillis() > server.disconnectAt) {
                    iterator.remove();
                    activeServers.get(allEntry.getKey()).remove(server);
                    sockets.get(allEntry.getKey()).disconnect(server.endpoint);
                    logger.info("Dispatcher: disconnect from expired server:" + entry.getValue().endpoint);
                } else {
                    if (System.currentTimeMillis() > server.pingAt) {
                        server.ping(sockets.get(allEntry.getKey()));
                    }
                }
            }
        }
    }

    private class Server {
        //  If not a single service replies within this time, give up
        private final static int serverDisconnectTtl = 1 * 60 * 1000;
        //  Server considered dead if silent for this long
        private final static int serverTtl = 30 * 1000;
        //  PING interval for servers we think are alive
        private final static int serverPingInterval = 5 * 1000;

        private String endpoint;                          // Server identity/endpoint
        private boolean pingEnabled = false;              // Flag to ping.
        private long pingAt = Long.MAX_VALUE;            //  Next ping at this time
        private long expires = Long.MAX_VALUE;           //  Expires at this time
        private long disconnectAt = Long.MAX_VALUE;      //  Disconnect at this time

        Server(String endpoint) {
            this(endpoint, false);
        }

        Server(String endpoint, boolean pingEnabled) {
            this.endpoint = endpoint;
            this.pingEnabled = pingEnabled;
            if (this.pingEnabled) refresh();
        }

        private void refresh() {
            pingAt = System.currentTimeMillis() + Server.serverPingInterval;
            expires = System.currentTimeMillis() + Server.serverTtl;
            disconnectAt = System.currentTimeMillis() + Server.serverDisconnectTtl;
        }

        private void refreshPingAt() {
            pingAt = System.currentTimeMillis() + Server.serverPingInterval;
        }

        private void ping(ZMQ.Socket socket) {
            if (System.currentTimeMillis() >= pingAt) {
                ZMsg ping = Codec.encodePingMsg(endpoint);
                ping.send(socket);
                ping.destroy();
                refreshPingAt();
            }
        }
    }

    private class HandlerAdapter extends AbstractZLoopSocketHandler {
        private IMsgEventListener listener;

        HandlerAdapter(Dispatcher dispatcher, List<IEndpoint> froms, int fromType, boolean pingEnabled,
                       IMsgEventListener listener) {
            super(dispatcher, froms, fromType, pingEnabled);
            this.listener = listener;
        }

        @Override
        public int handle(ZMsg msg, Object arg) {
            // handle the message.
            listener.onEvent(new MsgEvent(IMsgEvent.ID_MESSAGE, engine, new MsgEventInfo(Codec.decodeTransMsg(msg))));
            return 0;
        }
    }

    private class Scavenger implements ZLoop.IZLoopHandler {
        @Override
        public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object arg) {
            Dispatcher dispatcher = (Dispatcher) arg;
            dispatcher.trackServers();
            return 0;
        }
    }
}