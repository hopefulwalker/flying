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
import org.zeromq.ZFrame;
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.List;

public abstract class AbstractZLoopSocketHandler implements ZLoop.IZLoopHandler {
    private Dispatcher dispatcher;
    private List<IEndpoint> froms;
    private int fromType;

    @Override
    public int handle(ZLoop zLoop, ZMQ.PollItem pollItem, Object o) {
        ZMQ.Socket socket = pollItem.getSocket();
        ZMsg msg = ZMsg.recvMsg(socket);
        // if the router , drop the identity frame before forward.
        ZFrame address = null;
        if (socket.getType() == ZMQ.ROUTER) {
            address = msg.pop();
        }
        // handle the heart beat message.
        ZFrame command = msg.pop();
        if (Ints.fromByteArray(command.getData()) == IMsgEvent.ID_PING) {
            msg.push(Ints.toByteArray(IMsgEvent.ID_PONG));
            if (socket.getType() == ZMQ.ROUTER) msg.push(address);
            msg.send(socket);
        }
        if (Ints.fromByteArray(command.getData()) == IMsgEvent.ID_PONG) {
            dispatcher.
        }


        dispatcher.sendMsg((List<IEndpoint>) arg, msg);
        msg.destroy();
        return 0;
    }

    public abstract int handle();


    public AbstractZLoopSocketHandler(Dispatcher dispatcher, List<IEndpoint> froms, int fromType) {
        this.dispatcher = dispatcher;
        this.froms = froms;
        this.fromType = fromType;
    }

    private void initialize() {
        dispatcher.connect(froms, fromType);
        dispatcher.connect(tos, toType);
        dispatcher.addZLoopHandler(froms, this, tos);
    }


}
