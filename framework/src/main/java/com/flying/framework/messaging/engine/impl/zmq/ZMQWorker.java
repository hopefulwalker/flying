/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
 2015/2/23     Walker.Zhang     0.1.0        Created.
 2017/5/1      Walker.Zhang     0.3.4        redefine the message event id.
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class ZMQWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ZMQWorker.class);
    private AbstractZMQServerEngine engine;

    public ZMQWorker(AbstractZMQServerEngine engine) {
        this.engine = engine;
    }

    /**
     * The request message structure.
     * Frame1: identity
     * Frame2: sequence of the message
     * Frame3: command = ID_PING, no real msg, only msgNO.
     * Frame4: real msg command = REQ.
     */
    @Override
    public void run() {
        ZContext context;
        ZMQ.Socket socket;
        try {
            context = ZContext.shadow(engine.getContext());
            socket = context.createSocket(ZMQ.DEALER);
            socket.connect(engine.getWorkerURL());
        } catch (Throwable t) {
            logger.error("Error in initializing worker, engine could not start!", t);
            return;
        }
//        ZFrame address, msgNO, content;
//        int command;
        ZMsg msg = null;
        Codec.Msg decodedMsg = null;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                msg = ZMsg.recvMsg(socket);
                if (msg == null) break;
                decodedMsg = Codec.decode(msg, socket.getType());
                switch (decodedMsg.eventID) {
                    case IMsgEvent.ID_PING:
                        Codec.encodePongMsg(decodedMsg.others, decodedMsg.address).send(socket);
                        break;
                    case IMsgEvent.ID_REQUEST:
                    case IMsgEvent.ID_MESSAGE:
                        IMsgEventListener listener = engine.getMsgEventListener();
                        if (listener != null) {
                            IMsgEventResult result = listener.onEvent(MsgEvent.newInstance(decodedMsg.eventID, engine, decodedMsg.data));
                            if (result != null) {
                                Codec.encode(decodedMsg.others, decodedMsg.address, IMsgEvent.ID_REPLY, result.getByteArray()).send(socket);
                            }
                        } else {
                            Codec.encode(decodedMsg.others, decodedMsg.address, IMsgEvent.ID_FAILED).send(socket);
                        }
                        break;
                }
                msg.destroy();
            } catch (Throwable t) {
                logger.error("Error in handling the request, discard it...!", t);
            }
        }
        if (context != null) context.destroy();
    }
}