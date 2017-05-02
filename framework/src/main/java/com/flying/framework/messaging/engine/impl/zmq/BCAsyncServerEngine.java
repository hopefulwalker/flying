/*
 Created by Walker.Zhang on 2017/5/2.
 Revision History:
 Date          Who              Version      What
 2017/5/2      Walker.Zhang     0.3.4        Create to build new server engine based on zloop dispatcher.
*/

package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.util.List;

public class BCAsyncServerEngine extends AbstractAsyncEngine implements IServerEngine {
    private static final Logger logger = LoggerFactory.getLogger(BCAsyncServerEngine.class);
    private static final int PACKET_SIZE = 512;
    private IEndpoint listenEndpoint;

    public BCAsyncServerEngine(IEndpoint listenEndpoint) {
        this.listenEndpoint = listenEndpoint;
    }

    @Override
    public IEndpoint getListenEndpoint() {
        return listenEndpoint;
    }

    @Override
    public void setListenEndpoint(IEndpoint listenEndpoint) {
        this.listenEndpoint = listenEndpoint;
    }

    @Override
    void initialize(ZMQ.Socket pipe) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.bind(new InetSocketAddress(listenEndpoint.getPort()));
            new ServerThread(this, channel, pipe).start();
        } catch (Exception e) {
            logger.error("ZMQ Service wrong exit, stop engine now", e);
            stop();
        }
    }

    @Override
    void setupDispatcherHandler(Dispatcher dispatcher, List<IEndpoint> froms) {
        dispatcher.addMsgEventListener(froms, ZMQ.DEALER, false, getMsgEventListener());
    }

    @Override
    int getPipeSocketType() {
        return ZMQ.DEALER;
    }

    private static class ServerThread extends Thread {
        private IServerEngine serverEngine;
        private DatagramChannel front;
        private ZMQ.Socket back;

        ServerThread(IServerEngine serverEngine, DatagramChannel front, ZMQ.Socket back) {
            super("ServerEngine");
            this.serverEngine = serverEngine;
            this.front = front;
            this.back = back;
        }

        @Override
        public void run() {
            try {
                ZMQ.PollItem[] items = {
                        new ZMQ.PollItem(front, ZMQ.Poller.POLLIN),
                        new ZMQ.PollItem(back, ZMQ.Poller.POLLIN)
                };
                DatagramPacket requestPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                while (!Thread.currentThread().isInterrupted()) {
                    int rc = ZMQ.poll(items, -1);
                    if (rc == -1) break;              //  Context has been shut down
                    if (items[0].isReadable()) routeInMsg(front, back, requestPacket);
                    if (items[1].isReadable()) routeOutMsg(front, back);
                }
            } catch (Exception e) {
                logger.error("ZMQ Service wrong exit, stop engine now", e);
                serverEngine.stop();
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
}
