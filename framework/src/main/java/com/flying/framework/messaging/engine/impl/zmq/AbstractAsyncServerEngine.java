/*
 Created by Walker.Zhang on 2017/5/3.
 Revision History:
 Date          Who              Version      What
 2017/5/3      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/


package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerEngine;
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
import java.util.List;

public abstract class AbstractAsyncServerEngine extends AbstractAsyncEngine implements IServerEngine, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncServerEngine.class);
    private ZMQ.Socket pipe;             //  Pipe through to background
    private IEndpoint listenEndpoint;

    public AbstractAsyncServerEngine(IEndpoint listenEndpoint) {
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
        this.pipe = pipe;
        new Thread(this, "ServerEngine").start();
    }

    @Override
    void setupDispatcherHandler(Dispatcher dispatcher, List<IEndpoint> froms) {
        dispatcher.addMsgEventListener(froms, ZMQ.DEALER, false, getMsgEventListener());
    }

    @Override
    int getPipeSocketType() {
        return ZMQ.DEALER;
    }

    @Override
    public abstract void run();

    public ZMQ.Socket getPipe() {
        return pipe;
    }
}
