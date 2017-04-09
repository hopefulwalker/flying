/**
 * Created by Walker.Zhang on 2015/5/19.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/19     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;

public class ZMQBCServerEngine extends AbstractZMQServerEngine {
    public static final int PACKET_SIZE = 512;
    private static final Logger logger = LoggerFactory.getLogger(ZMQBCServerEngine.class);

    public ZMQBCServerEngine(IEndpoint listenEndpoint) {
        super(listenEndpoint);
    }

    @Override
    public void run() {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.bind(new InetSocketAddress(getListenEndpoint().getPort()));
            ZMQ.Socket backend = getContext().createSocket(ZMQ.DEALER);
            backend.bind(getWorkerURL());
            for (int i = 0; i < getWorkers(); i++) {
                getThreadPool().submit(new ZMQWorker(this));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Broadcast Receiving Service is started, listening to: " + getListenEndpoint().getPort());
            }
            ZMQ.PollItem[] items = {
                    new ZMQ.PollItem(channel, ZMQ.Poller.POLLIN),
                    new ZMQ.PollItem(backend, ZMQ.Poller.POLLIN)
            };
            DatagramPacket requestPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            while (!Thread.currentThread().isInterrupted()) {
                int rc = ZMQ.poll(items, -1);
                if (rc == -1) break;              //  Context has been shut down
                if (items[0].isReadable()) routeInMsg(channel, backend, requestPacket);
                if (items[1].isReadable()) routeOutMsg(channel, backend);
            }
        } catch (Exception e) {
            logger.error("ZMQ Service wrong exit, stop engine now", e);
            stop();
        }
    }

    private void routeOutMsg(DatagramChannel channel, ZMQ.Socket backend) {
        try {
            DatagramPacket replyPacket;
            ZMsg msg = ZMsg.recvMsg(backend);
            InetAddress address = InetAddress.getByAddress(msg.pop().getData());
            // event ID.
            msg.pop();
            // port
            int port = Ints.fromByteArray(msg.pop().getData());
            // data
            byte[] data = msg.pop().getData();
            msg.destroy();
            replyPacket = new DatagramPacket(data, data.length, address, port);
            channel.socket().send(replyPacket);
        } catch (IOException ioe) {
            logger.error("Exception in route out message.", ioe);
        }
    }

    private void routeInMsg(DatagramChannel channel, ZMQ.Socket backend, DatagramPacket requestPacket) {
        try {
            requestPacket.setLength(PACKET_SIZE);
            channel.socket().receive(requestPacket);
            // address
            backend.send(requestPacket.getAddress().getAddress(), ZMQ.SNDMORE);
            // event id
            backend.send(Ints.toByteArray(IMsgEvent.ID_REQUEST), ZMQ.SNDMORE);
            // port
            backend.send(Ints.toByteArray(requestPacket.getPort()), ZMQ.SNDMORE);
            // data
            backend.send(requestPacket.getData(), 0, requestPacket.getLength(), 0);
        } catch (IOException ioe) {
            logger.error("Exception in route in message.", ioe);
        }
    }
}