/*
 Created by Walker on 2017/4/26.
 Revision History:
 Date          Who              Version      What
 2017/4/26     Walker.Zhang     0.3.3        Created to support zloop.
 2017/5/2      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.

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
        Codec.Msg decodedMsg = Codec.decode(msg, socket.getType());
        if (pingEnabled) dispatcher.refreshServer(froms, decodedMsg.address);
        // handle the heart beat message.
        ZMsg reply = null;
        switch (decodedMsg.eventID) {
            case IMsgEvent.ID_PING:
                reply = Codec.encodePongMsg(decodedMsg.others, decodedMsg.address);
                break;
            case IMsgEvent.ID_PONG:
                break;
            case IMsgEvent.ID_MESSAGE:
            case IMsgEvent.ID_REQUEST:
            case IMsgEvent.ID_REPLY:
            case IMsgEvent.ID_FAILED:
                reply = handle(decodedMsg, arg);
                break;
            case IMsgEvent.ID_TIMEOUT:
                logger.warn("ID_TIMEOUT should not happen here, please check");
                break;
            default:
                logger.warn("Undefined Event" + decodedMsg.eventID);
                break;
        }
        if (reply != null) {
            reply.send(socket);
            reply.destroy();
        }
        msg.destroy();
        return 0;
    }

    public abstract ZMsg handle(Codec.Msg decodedMsg, Object arg);
}