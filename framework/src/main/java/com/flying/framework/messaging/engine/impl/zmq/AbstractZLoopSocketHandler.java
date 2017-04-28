/*
 Created by Walker on 2017/4/26.
 Revision History:
 Date          Who              Version      What
 2017/4/26      Walker           0.3.3       Created to support zloop.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    AbstractZLoopSocketHandler(Dispatcher dispatcher, List<IEndpoint> froms, int fromType, boolean pingEnabled) {
        this.dispatcher = dispatcher;
        this.froms = froms;
        this.fromType = fromType;
        this.pingEnabled = pingEnabled;
        dispatcher.connect(this.froms, this.fromType, this.pingEnabled);
    }

    Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object arg) {
        ZMQ.Socket socket = pollItem.getSocket();
        ZMsg msg = ZMsg.recvMsg(socket);
        String address = Codec.popAddress(msg, socket.getType());
        if (pingEnabled) dispatcher.refreshServer(froms, address);
        int rc = 0;
        // handle the heart beat message.
        switch (Codec.decodeEventID(msg)) {
            case IMsgEvent.ID_PING:
                ZMsg ping = Codec.encodePongMsg(address);
                ping.send(socket);
                ping.destroy();
                break;
            case IMsgEvent.ID_MESSAGE:
                rc = handle(msg, arg);
                break;
            default:
                //logger.warn("Deprecated Command");
                rc = handle(msg, arg);
        }
        msg.destroy();
        return rc;
    }

    public abstract int handle(ZMsg msg, Object arg);
}