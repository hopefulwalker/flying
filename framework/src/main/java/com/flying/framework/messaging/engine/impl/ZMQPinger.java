/*
 Created by Walker.Zhang on 2015/3/19.
 Revision History:
 Date          Who              Version      What
 2015/3/19     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.engine.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.engine.IPinger;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZMQPinger implements IPinger {
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
     * <p>
     * The request message structure.
     * Frame0: command = ID_PING, no subsequent frame.
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
        ZMsg msg = new ZMsg();
        msg.add(Ints.toByteArray(IMsgEvent.ID_PING));
        long msgNO = System.currentTimeMillis();
        msg.add(Longs.toByteArray(msgNO));
        msg.send(socket);
        // set up the time out.
        if (timeout < 0) timeout = 0;
        boolean succeed = false;
        int oldTimeout = socket.getReceiveTimeOut();
        int waitTime;
        do {
            waitTime = (int) (timeout - (System.currentTimeMillis() - msgNO));
            if (waitTime < 0) waitTime = 0;
            socket.setReceiveTimeOut(waitTime);
            ZMsg repMsg = ZMsg.recvMsg(socket);
            if (repMsg == null) break;
            int command = Ints.fromByteArray(repMsg.pop().getData());
            if (command == IMsgEvent.ID_PING && msgNO == Longs.fromByteArray(repMsg.pop().getData())) {
                succeed = true;
                break;
            }
        } while (System.currentTimeMillis() < msgNO + timeout);
        socket.setReceiveTimeOut(oldTimeout);
        return succeed;
    }
}
