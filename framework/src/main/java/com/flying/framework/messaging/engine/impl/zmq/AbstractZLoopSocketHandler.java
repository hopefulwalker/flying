/*
 Created by Walker on 2017/4/26.
 Revision History:
 Date          Who              Version      What
 2017/4/26      Walker           0.1.0        Created. 
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZFrame;
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.List;

public abstract class AbstractZLoopSocketHandler implements ZLoop.IZLoopHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractZLoopSocketHandler.class);
    private Dispatcher dispatcher;
    private List<IEndpoint> froms;
    private int fromType;
    private boolean pingEnabled;

    public AbstractZLoopSocketHandler(Dispatcher dispatcher, List<IEndpoint> froms, int fromType, boolean pingEnabled) {
        this.dispatcher = dispatcher;
        this.froms = froms;
        this.fromType = fromType;
        this.pingEnabled = pingEnabled;
        dispatcher.connect(this.froms, this.fromType, this.pingEnabled);
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object arg) {
        ZMQ.Socket socket = pollItem.getSocket();
        ZMsg msg = ZMsg.recvMsg(socket);
        String address = null;
        if (socket.getType() == ZMQ.ROUTER) {
            address = msg.popString();
            dispatcher.refreshServer(froms, address);
        } else {
            dispatcher.refreshServer(froms);
        }
        int rc = 0;
        // handle the heart beat message.
        ZFrame command = msg.peekFirst();
        switch (Ints.fromByteArray(command.getData())) {
            case IMsgEvent.ID_PING:
                ZMsg ping = new ZMsg();
                if (socket.getType() == ZMQ.ROUTER) ping.add(address);
                ping.add(Ints.toByteArray(IMsgEvent.ID_PONG));
                ping.add(Longs.toByteArray(System.currentTimeMillis()));
                ping.send(socket);
                ping.destroy();
                break;
            case IMsgEvent.ID_MESSAGE:
                rc = handle(msg, arg);
                break;
            default:
//                logger.warn("Deprecated Command");
                rc = handle(msg, arg);
                break;
        }
        msg.destroy();
        return rc;
    }

    public abstract int handle(ZMsg msg, Object arg);
}