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
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

/**
 * This class define the decoder and encoder for communication.
 */
class Codec {
    private static long sequence = 0L;

    static ZMsg encodePingMsg(String endpoint, int socketType) {
        ZMsg ping = new ZMsg();
        if (socketType == ZMQ.ROUTER) ping.add(endpoint);
        ping.add(Ints.toByteArray(IMsgEvent.ID_PING));
        ping.add(Longs.toByteArray(System.currentTimeMillis()));
        return ping;
    }

    static ZMsg encodeTransData(byte[] data) {
        ZMsg msg = new ZMsg();
        msg.add(Ints.toByteArray(IMsgEvent.ID_MESSAGE));
        msg.add(data);
        return msg;
    }

    static ZMsg encode(Msg decodedMsg, int eventID, byte[] data) {
        if (decodedMsg.address != null) decodedMsg.others.push(decodedMsg.address);
        decodedMsg.others.add(Ints.toByteArray(eventID));
        decodedMsg.others.add(data);
        return decodedMsg.others;
    }

    //    static String popAddress(ZMsg msg, int socketType) {
//        if (socketType == ZMQ.ROUTER) return msg.popString();
//        return null;
//    }
//
    static void pushAddress(ZMsg msg, int socketType, String address) {
        if (socketType == ZMQ.ROUTER) msg.push(address);
    }

//    static ZMsg encodeTransData(byte[] data, String endpoint) {
//        ZMsg msg = new ZMsg();
//        if (endpoint != null) msg.add(endpoint);
//        msg.add(Ints.toByteArray(IMsgEvent.ID_MESSAGE));
//        msg.add(data);
//        return msg;
//    }

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
