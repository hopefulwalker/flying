/*
 Created by Walker.Zhang on 2017/4/28.
 Revision History:
 Date          Who              Version      What
 2017/4/28     Walker.Zhang     0.3.3        Created to extract protocol related function.
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

/**
 * This class define the decoder and encoder for communication.
 * Here is the message protocol:
 * 1. address(if socket type is router)
 * 2. others
 * 3. message event id
 * 4. message data.
 */
class Codec {
    private static long sequence = 0L;

    static ZMsg encodePingMsg(String address) {
        return encode(null, address, IMsgEvent.ID_PING, Longs.toByteArray(System.currentTimeMillis()));
    }

    static ZMsg encodePongMsg(ZMsg msg, String address) {
        return encode(msg, address, IMsgEvent.ID_PONG, Longs.toByteArray(System.currentTimeMillis()));
    }

    static ZMsg encode(IMsgEvent event) {
        return encode(null, null, event.getId(), event.getInfo().getBytes());
    }

    static ZMsg encode(ZMsg msg, String address, int eventID, byte[] data) {
        if (msg == null) msg = new ZMsg();
        if (address != null) msg.push(address);
        msg.add(Ints.toByteArray(eventID));
        msg.add(data);
        return msg;
    }

    static void pushAddress(ZMsg msg, String address) {
        if (address != null) msg.push(address);
    }

    static Msg decode(ZMsg msg, int socketType) {
        String address = null;
        if (socketType == ZMQ.ROUTER) address = msg.popString();
        byte[] data = msg.pollLast().getData();
        int eventID = Ints.fromByteArray(msg.pollLast().getData());
        return new Msg(address, msg, eventID, data);
    }

    static class Msg {
        String address;
        int eventID;
        byte[] data;
        ZMsg others;

        Msg(String address, ZMsg others, int eventID, byte[] data) {
            this.address = address;
            this.others = others;
            this.eventID = eventID;
            this.data = data;
        }
    }
}
