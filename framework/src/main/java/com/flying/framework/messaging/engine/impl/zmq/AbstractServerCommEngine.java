/*
 Created by Walker.Zhang on 2017/5/3.
 Revision History:
 Date          Who              Version      What
 2017/5/3      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.engine.IServerCommEngineConfig;
import com.flying.framework.messaging.event.IMsgEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.util.List;
import java.util.Map;

public abstract class AbstractServerCommEngine extends AbstractCommEngine implements IServerCommEngine, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractServerCommEngine.class);
    private ZMQ.Socket pipe;             //  Pipe through to background
    private IServerCommEngineConfig config;

    AbstractServerCommEngine(IServerCommEngineConfig config) {
        setConfig(config);
    }

    @Override
    public IServerCommEngineConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(IServerCommEngineConfig config) {
        this.config = config;
    }

    @Override
    void initialize(ZMQ.Socket pipe) {
        this.pipe = pipe;
        new Thread(this, "ServerEngine").start();
    }

    @Override
    void setupDispatcherHandler(Dispatcher dispatcher, List<IEndpoint> froms) {
        if (config.getServerListener() == null) {
            logger.error("Server listener is null, please recheck");
            return;
        }
        dispatcher.addMsgEventListener(froms, ZMQ.DEALER, false, config.getServerListener());
        if (config.getClientListeners() != null) {
            for (Map.Entry<List<IEndpoint>, IMsgEventListener> entry : config.getClientListeners().entrySet()) {
                dispatcher.addMsgEventListener(entry.getKey(), ZMQ.ROUTER, true, entry.getValue());
            }
        }
    }

    @Override
    int getWorkers() {
        return config.getWorkers();
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
