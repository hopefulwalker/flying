/**
 * Created by Walker.Zhang on 2017/4/25.
 * Revision History:
 * Date          Who              Version      What
 * 2017/4/25     Walker.Zhang     0.1.0        Created.
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
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private AsyncClientEngine engine;
    private ZLoop reactor;
    private Map<List<IEndpoint>, ZMQ.Socket> sockets;
    private Map<List<IEndpoint>, List<Server>> activeServers;
    private long sequence = 0L;

    public Dispatcher(AsyncClientEngine engine) {
        this.engine = engine;
        this.reactor = new ZLoop();
        this.sockets = new HashMap<>();
        this.activeServers = new HashMap<>();
    }

    public void sendMsg(List<IEndpoint> endpoints, MsgEvent msgEvent) {
        ZMsg msg = new ZMsg();
        boolean reqSent = false;
        ZMQ.Socket socket = sockets.get(endpoints);
        List<Server> servers = activeServers.get(endpoints);
        while (!servers.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, servers.size());
            Server server = servers.get(randomIndex);
            if (System.currentTimeMillis() >= server.expires) {
                servers.remove(randomIndex);
                logger.info("ZMQUCClientEngine: remove expired server:" + server.endpoint);
            } else {
                msg.push(msgEvent.getInfo().getByteArray());
                msg.push(Longs.toByteArray(++sequence));
                msg.push(Ints.toByteArray(IMsgEvent.ID_MESSAGE));
                msg.push(server.endpoint);
                msg.send(socket);
                msg.destroy();
                reqSent = true;
                break;
            }
        }
        if (!reqSent) {
            logger.warn("Dispatcher: request didn't send! active server size is :" + servers.size());
        }
    }

    public int addMsgEventListener(List<IEndpoint> endpoints, IMsgEventListener listener) {
        ZLoop.IZLoopHandler socketEvent = new ZLoop.IZLoopHandler() {
            @Override
            public int handle(ZLoop loop, ZMQ.PollItem item, Object arg) {
                listener.onEvent(new MsgEvent(IMsgEvent.ID_REPLY, engine, new MsgEventInfo(new byte[0])));
                return 0;
            }
        };
        ZMQ.Socket socket = engine.getContext().createSocket(ZMQ.ROUTER);
        List<Server> servers = new ArrayList<>();
        for (IEndpoint endpoint : endpoints) {
            socket.connect(endpoint.asString());
            servers.add(new Server(endpoint.asString()));
        }
        ZMQ.PollItem pollInput = new ZMQ.PollItem(socket, ZMQ.Poller.POLLIN);
        sockets.put(endpoints, socket);
        activeServers.put(endpoints, servers);

        return reactor.addPoller(pollInput, socketEvent, this);
    }

    @Override
    public void run() {
        reactor.start();
    }

    private class Server {
        //TODO: support ping.
        private String endpoint;        //  Server identity/endpoint
        private long pingAt = Long.MAX_VALUE;            //  Next ping at this time
        private long expires = Long.MAX_VALUE;           //  Expires at this time
        private long disconnectAt = Long.MAX_VALUE;      //  Disconnect at this time

        protected Server(String endpoint) {
            this.endpoint = endpoint;
            refresh();
        }

        private void refresh() {
//            pingAt = System.currentTimeMillis() + ZMQUCClientEngine.serverPingInterval;
//            expires = System.currentTimeMillis() + ZMQUCClientEngine.serverTtl;
//            disconnectAt = System.currentTimeMillis() + ZMQUCClientEngine.serverDisconnectTtl;
        }

        private void refreshPingAt() {
//            pingAt = System.currentTimeMillis() + ZMQUCClientEngine.serverPingInterval;
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
}