/*
 Created by Walker.Zhang on 2015/3/19.
 Revision History:
 Date          Who              Version      What
 2015/3/19     Walker.Zhang     0.1.0        Created.
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.ICommPinger;
import com.flying.framework.messaging.event.IMsgEvent;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Pinger implements ICommPinger {
    private ConcurrentMap<String, ZMQ.Socket> sockets = null;
    private ZContext context = null;

    @Override
    public void start() {
        this.context = new ZContext();
        this.sockets = new ConcurrentHashMap<>();
    }

    public void start(ZContext context) {
        this.context = ZContext.shadow(context);
        this.sockets = new ConcurrentHashMap<>();
    }

    @Override
    public void stop() {
        if (context != null) context.destroy();
    }

    /**
     * ping endpoint and wait for the reply before timeout.
     *
     * @param endpoint target to ping.
     * @param timeout  milliseconds. should be >=0. if <0, will set to 0 automatically.
     * @return true if success, false if fail.
     */
    @Override
    public boolean ping(IEndpoint endpoint, int timeout) {
        if (sockets.containsKey(endpoint.asString())) {
            ZMQ.Socket socket = sockets.get(endpoint.asString());
            synchronized (socket) {
                return ping(socket, timeout);
            }
        } else {
            ZMQ.Socket socket = context.createSocket(ZMQ.DEALER);
            socket.connect(endpoint.asString());
            synchronized (socket) {
                sockets.put(endpoint.asString(), socket);
                return ping(socket, timeout);
            }
        }
    }

    private boolean ping(ZMQ.Socket socket, int timeout) {
        // send ping message.

        ZMsg msg = Codec.encodePingMsg(null);
        msg.send(socket);
        msg.destroy();
        // set up the time out.
        if (timeout < 0) timeout = 0;
        boolean succeed = false;
        int oldTimeout = socket.getReceiveTimeOut();
        int waitTime;
        long startTime = System.currentTimeMillis();
        do {
            socket.setReceiveTimeOut(timeout);
            ZMsg repMsg = ZMsg.recvMsg(socket);
            if (repMsg == null) break;
            Codec.Msg decodedMsg = Codec.decode(repMsg, ZMQ.DEALER);
            if (decodedMsg.eventID == IMsgEvent.ID_PONG) {
                succeed = true;
                break;
            }
        } while (System.currentTimeMillis() < startTime + timeout);
        socket.setReceiveTimeOut(oldTimeout);
        return succeed;
    }
}
