/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.event.*;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
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
     * Frame0: identity
     * Frame1: command = ID_PING, no real msg, only msgNO.
     * Frame2: sequence of the message
     * Frame3: real msg command = REQ.
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
        ZFrame address, msgNO, content;
        int command;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ZMsg msg = ZMsg.recvMsg(socket);
                if (msg == null) break;
                address = msg.pop();
                command = Ints.fromByteArray(msg.pop().getData());
                switch (command) {
                    case IMsgEvent.ID_PING:
                        msgNO = msg.pop();
                        sendReply(socket, address, command, msgNO, null);
                        break;
                    case IMsgEvent.ID_REQUEST:
                        msgNO = msg.pop();
                        content = msg.pop();
                        IMsgEventListener listener = engine.getMsgEventListener();
                        if (listener != null) {
                            IMsgEventResult result = listener.onEvent(new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(content.getData())));
                            if (result == null) {
                                sendReply(socket, address, IMsgEvent.ID_REPLY_UNSUPPORTED, msgNO, null);
                            } else if (result.isReplyRequired()) {
                                sendReply(socket, address, IMsgEvent.ID_REPLY_SUCCEED, msgNO, result.getByteArray());
                            }
                        } else {
                            sendReply(socket, address, IMsgEvent.ID_REPLY_UNSUPPORTED, msgNO, null);
                        }
                        content.destroy();
                        msgNO.destroy();
                        break;
                }
                address.destroy();
                msg.destroy();
            } catch (Throwable t) {
                logger.error("Error in handling the request, discard it...!", t);
            }
        }
        if (context != null) context.destroy();
    }


    private void sendReply(ZMQ.Socket socket, ZFrame address, int command, ZFrame msgNO, byte[] result) {
        ZMsg repMsg = new ZMsg();
        repMsg.add(address);
        repMsg.add(Ints.toByteArray(command));
        if (msgNO != null) repMsg.add(msgNO);
        if (result != null) repMsg.add(result);
        repMsg.send(socket);
        repMsg.destroy();
    }
}