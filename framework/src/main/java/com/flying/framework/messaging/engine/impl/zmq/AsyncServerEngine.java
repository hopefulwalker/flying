/*
 Created by Walker.Zhang on 2017/5/2.
 Revision History:
 Date          Who              Version      What
 2017/5/2      Walker.Zhang     0.3.4        Create to build new server engine based on zloop dispatcher.
*/

package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AsyncServerEngine extends AbstractAsyncEngine implements IServerEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncEngine.class);

    private IEndpoint listenEndpoint;

    public AsyncServerEngine(IEndpoint listenEndpoint) {
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
        try {
            ZMQ.Socket frontend = getContext().createSocket(ZMQ.ROUTER);
            // charset == UTF-8
            frontend.setIdentity(listenEndpoint.asString().getBytes("UTF-8"));
            frontend.bind(listenEndpoint.asString());
            new ServerThread(this, frontend, pipe).start();
        } catch (UnsupportedEncodingException uee) {
            logger.error("Failed to initialize frontend socket.", uee);
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
        private ZMQ.Socket front;
        private ZMQ.Socket back;

        ServerThread(IServerEngine serverEngine, ZMQ.Socket front, ZMQ.Socket back) {
            super("ServerEngine");
            this.serverEngine = serverEngine;
            this.front = front;
            this.back = back;
        }

        @Override
        public void run() {
            try {
                ZMQ.proxy(front, back, null);
            } catch (Exception e) {
                logger.error("ZMQ Service wrong exit, stop engine now", e);
                serverEngine.stop();
            }
        }
    }
}
