/*
 Created by Walker.Zhang on 2017/5/2.
 Revision History:
 Date          Who              Version      What
 2017/5/2      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;

public class BCAsyncServerCommEngine extends AbstractAsyncServerCommEngine {
    private static final Logger logger = LoggerFactory.getLogger(BCAsyncServerCommEngine.class);
    private static final int PACKET_SIZE = 512;

    public BCAsyncServerCommEngine(ICommEngineConfig config) {
        super(config);
    }

    @Override
    public void run() {
        try (DatagramChannel front = DatagramChannel.open()) {
            front.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            front.bind(new InetSocketAddress(getListenEndpoint().getPort()));
            ZMQ.PollItem[] items = {
                    new ZMQ.PollItem(front, ZMQ.Poller.POLLIN),
                    new ZMQ.PollItem(getPipe(), ZMQ.Poller.POLLIN)
            };
            DatagramPacket requestPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            while (!Thread.currentThread().isInterrupted()) {
                int rc = ZMQ.poll(items, -1);
                if (rc == -1) break;              //  Context has been shut down
                if (items[0].isReadable()) routeInMsg(front, getPipe(), requestPacket);
                if (items[1].isReadable()) routeOutMsg(front, getPipe());
            }
        } catch (ZMQException zmqe) {
            if (zmqe.getErrorCode() != ZMQ.Error.ETERM.getCode()) {
                logger.error("Error info: " + ZMQ.Error.findByCode(zmqe.getErrorCode()), zmqe);
            }
        } catch (Exception e) {
            logger.error("Error in server engine!", e);
        } finally {
            stop();
        }
    }

    private void routeOutMsg(DatagramChannel channel, ZMQ.Socket backend) {
        try {
            DatagramPacket replyPacket;
            ZMsg msg = ZMsg.recvMsg(backend);
            Codec.Msg decodedMsg = Codec.decode(msg, backend.getType());
            // address
            InetAddress address = InetAddress.getByAddress(decodedMsg.others.pop().getData());
            // port
            int port = Ints.fromByteArray(decodedMsg.others.pop().getData());
            byte[] data = decodedMsg.data;
            replyPacket = new DatagramPacket(data, data.length, address, port);
            channel.socket().send(replyPacket);
            msg.destroy();
        } catch (IOException ioe) {
            logger.error("Exception in route out message.", ioe);
        }
    }

    private void routeInMsg(DatagramChannel channel, ZMQ.Socket backend, DatagramPacket requestPacket) {
        try {
            requestPacket.setLength(PACKET_SIZE);
            channel.socket().receive(requestPacket);
            ZMsg msg = new ZMsg();
            msg.add(requestPacket.getAddress().getAddress());
            msg.add(Ints.toByteArray(requestPacket.getPort()));
            Codec.encode(msg, null, IMsgEvent.ID_REQUEST, requestPacket.getData()).send(backend);
            msg.destroy();
        } catch (IOException ioe) {
            logger.error("Exception in route in message.", ioe);
        }
    }
}
