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
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.concurrent.Executor;

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
        new ServerThread(this, pipe).start();
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
        private ZMQ.Socket back;

        ServerThread(IServerEngine serverEngine, ZMQ.Socket back) {
            super("ServerEngine");
            this.serverEngine = serverEngine;
            this.back = back;
        }

        @Override
        public void run() {
            try (DatagramChannel front = DatagramChannel.open()) {
                front.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                front.bind(new InetSocketAddress(serverEngine.getListenEndpoint().getPort()));
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
                if (e instanceof ZMQException) {
                    logger.error("ZMQException:" + ZMQ.Error.findByCode(((ZMQException) e).getErrorCode()), e);
                } else {
                    logger.error("Error in server engine!", e);
                }
                serverEngine.stop();
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
}
