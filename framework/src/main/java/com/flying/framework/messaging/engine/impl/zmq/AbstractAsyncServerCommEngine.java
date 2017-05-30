/*
 Created by Walker.Zhang on 2017/5/3.
 Revision History:
 Date          Who              Version      What
 2017/5/3      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IAsyncServerCommEngine;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.List;

public abstract class AbstractAsyncServerCommEngine extends AbstractAsyncCommEngine implements IAsyncServerCommEngine, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncServerCommEngine.class);
    private ZMQ.Socket pipe;             //  Pipe through to background

    AbstractAsyncServerCommEngine(ICommEngineConfig config) {
        setConfig(config);
    }

    @Override
    void initialize(ZMQ.Socket pipe) {
        this.pipe = pipe;
        new Thread(this, "ServerEngine").start();
    }

    @Override
    void setupDispatcherHandler(Dispatcher dispatcher, List<IEndpoint> froms) {
        dispatcher.addMsgEventListener(froms, ZMQ.DEALER, false, getConfig().getMsgEventListener());
    }

    @Override
    int getPipeSocketType() {
        return ZMQ.DEALER;
    }

    @Override
    public abstract void run();

    ZMQ.Socket getPipe() {
        return pipe;
    }
}
