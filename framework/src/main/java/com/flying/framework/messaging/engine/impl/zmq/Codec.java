/*
 Created by Walker.Zhang on 2017/4/28.
 Revision History:
 Date          Who              Version      What
 2017/4/28     Walker.Zhang     0.3.3        Created to extract protocol related function.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.sun.istack.internal.Nullable;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

/**
 * This class define the decoder and encoder for communication.
 */
class Codec {
    private static long sequence = 0L;

    static ZMsg encodePingMsg() {
        return encodePingPongMsg(null, IMsgEvent.ID_PING);
    }

    static ZMsg encodePingMsg(String endpoint) {
        return encodePingPongMsg(endpoint, IMsgEvent.ID_PING);
    }

    static ZMsg encodePongMsg() {
        return encodePingPongMsg(null, IMsgEvent.ID_PONG);
    }

    static ZMsg encodePongMsg(String endpoint) {
        return encodePingPongMsg(endpoint, IMsgEvent.ID_PONG);
    }

    private static ZMsg encodePingPongMsg(String endpoint, int eventType) {
        ZMsg ping = new ZMsg();
        if (endpoint != null) ping.add(endpoint);
        ping.add(Ints.toByteArray(eventType));
        ping.add(Longs.toByteArray(System.currentTimeMillis()));
        return ping;
    }

    static ZMsg encodeTransMsg(byte[] data) {
        return encodeTransMsg(data, null);
    }

    static ZMsg encodeTransMsg(byte[] data, String endpoint) {
        ZMsg msg = new ZMsg();
        if (endpoint != null) msg.add(endpoint);
        msg.add(Ints.toByteArray(IMsgEvent.ID_MESSAGE));
        // todo: remove needed.
        msg.add(Longs.toByteArray(++sequence));
        msg.add(data);
        return msg;
    }

    static byte[] decodeTransMsg(ZMsg msg) {
        return msg.peekLast().getData();
    }

    static int decodeEventID(ZMsg msg) {
        return Ints.fromByteArray(msg.peekFirst().getData());
    }

    @Nullable
    static String popAddress(ZMsg msg, int socketType) {
        if (socketType == ZMQ.ROUTER) return msg.popString();
        return null;
    }

    static void pushAddress(ZMsg msg, int socketType, String address) {
        if (socketType == ZMQ.ROUTER) msg.push(address);
    }
}
