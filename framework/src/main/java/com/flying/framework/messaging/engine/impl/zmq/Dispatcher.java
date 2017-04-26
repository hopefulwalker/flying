/**
 * Created by Walker.Zhang on 2017/4/25.
 * Revision History:
 * Date          Who              Version      What
 * 2017/4/25     Walker.Zhang     0.3.3        Created to support zloop.
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ZContext context;
    private AsyncClientEngine engine;
    private ZLoop reactor;
    private Map<List<IEndpoint>, ZMQ.Socket> sockets;
    private Map<List<IEndpoint>, Map<IEndpoint, Server>> activeServers;
    private long sequence = 0L;

    public Dispatcher(AsyncClientEngine engine) {
        this.engine = engine;
        this.context = ZContext.shadow(engine.getContext());
        this.reactor = new ZLoop();
        this.sockets = new HashMap<>();
        this.activeServers = new HashMap<>();
    }

    public void connect(List<IEndpoint> endpoints, int socketType) {
        if (sockets.containsKey(endpoints)) return;
        ZMQ.Socket socket = context.createSocket(socketType);
        Map<IEndpoint, Server> servers = new HashMap<>();
        for (IEndpoint endpoint : endpoints) {
            socket.connect(endpoint.asString());
            servers.put(endpoint, new Server(endpoint, socketType == ZMQ.ROUTER));
        }
        sockets.put(endpoints, socket);
        activeServers.put(endpoints, servers);
    }

    public int addZLoopHandler(List<IEndpoint> endpoints, ZLoop.IZLoopHandler handler, Object handlerArg) {
        ZMQ.Socket socket = sockets.get(endpoints);
        ZMQ.PollItem pollInput = new ZMQ.PollItem(socket, ZMQ.Poller.POLLIN);
        return reactor.addPoller(pollInput, handler, handlerArg);
    }

    public int addMsgEventListener(List<IEndpoint> endpoints, IMsgEventListener listener) {
        ZLoop.IZLoopHandler handler = new ZLoop.IZLoopHandler() {
            @Override
            public int handle(ZLoop loop, ZMQ.PollItem item, Object arg) {
                listener.onEvent(new MsgEvent(IMsgEvent.ID_REPLY, engine, new MsgEventInfo(new byte[0])));
                return 0;
            }
        };
        connect(endpoints, ZMQ.ROUTER);
        return addZLoopHandler(endpoints, handler, this);
    }

    public void refreshServer(List<IEndpoint> endpoints, IEndpoint endpoint) {
        //  Frame 0 is the identity of server that replied
        Server server = activeServers.get(endpoints).get(endpoint);
        server.refresh();
    }

    public void sendMsg(List<IEndpoint> endpoints, ZMsg msg) {
        boolean reqSent = false;
        ZMQ.Socket socket = sockets.get(endpoints);
        if (socket.getType() != ZMQ.ROUTER) {
            msg.send(socket);
            return;
        }
        Map<IEndpoint, Server> servers = activeServers.get(endpoints);
        while (!servers.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, servers.size());
            Server server = servers.get(servers.keySet().toArray()[randomIndex]);
            if (System.currentTimeMillis() >= server.expires) {
                servers.remove(randomIndex);
                logger.warn("ZMQUCClientEngine: remove expired server:" + server.endpoint.asString());
            } else {
                msg.push(server.endpoint.asString());
                msg.send(socket);
                reqSent = true;
                break;
            }
        }
        if (!reqSent) {
            logger.warn("Dispatcher: request didn't send! active server size is :" + servers.size());
        }
    }

    public void sendMsg(List<IEndpoint> endpoints, MsgEvent msgEvent) {
        ZMsg msg = new ZMsg();
        msg.push(msgEvent.getInfo().getByteArray());
        msg.push(Longs.toByteArray(++sequence));
        msg.push(Ints.toByteArray(IMsgEvent.ID_MESSAGE));
        sendMsg(endpoints, msg);
        msg.destroy();
    }

    @Override
    public void run() {
        try {
            reactor.start();
        } catch (ZMQException zmqe) {
            if (zmqe.getErrorCode() != ZMQ.Error.ETERM.getCode()) {
                logger.error("Error occurs when running reactor!", zmqe);
            }
        }
        context.destroy();
    }

    private class Server {
        //  If not a single service replies within this time, give up
        private final static int serverDisconnectTtl = 1 * 60 * 1000;
        //  Server considered dead if silent for this long
        private final static int serverTtl = 30 * 1000;
        //  PING interval for servers we think are alive
        private final static int serverPingInterval = 5 * 1000;

        private IEndpoint endpoint;                          // Server identity/endpoint
        private boolean pingEnabled = false;              // Flag to ping.
        private long pingAt = Long.MAX_VALUE;            //  Next ping at this time
        private long expires = Long.MAX_VALUE;           //  Expires at this time
        private long disconnectAt = Long.MAX_VALUE;      //  Disconnect at this time

        Server(IEndpoint endpoint) {
            this(endpoint, false);
        }

        Server(IEndpoint endpoint, boolean pingEnabled) {
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
                ZMsg ping = new ZMsg();
                ping.add(endpoint.asString());
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
}