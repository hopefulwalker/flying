/*
 Created by Walker on 2017/4/26.
 Revision History:
 Date          Who              Version      What
 2017/4/26     Walker.Zhang     0.3.3        Created to support zloop.
 2017/5/2      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
 2017/6/1      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
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

    public List<IEndpoint> getFroms() {
        return froms;
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
        switch (decodedMsg.eventID) {
            case IMsgEvent.ID_PING:
                // no need to destroy reply because of the implementation mechanism(using the same address of msg)
                ZMsg reply = Codec.encodePongMsg(decodedMsg.others, decodedMsg.address);
                reply.send(socket);
                break;
            case IMsgEvent.ID_PONG:
                break;
            case IMsgEvent.ID_MESSAGE:
            case IMsgEvent.ID_REQUEST:
            case IMsgEvent.ID_REPLY:
            case IMsgEvent.ID_FAILED:
                handle(decodedMsg);
                break;
            case IMsgEvent.ID_TIMEOUT:
                logger.warn("ID_TIMEOUT should not happen here, please check");
                break;
            default:
                logger.warn("Undefined Event" + decodedMsg.eventID);
                break;
        }
        msg.destroy();
        return 0;
    }

    /**
     * @param decodedMsg the decoded Message.
     */
    public abstract void handle(Codec.Msg decodedMsg);
}