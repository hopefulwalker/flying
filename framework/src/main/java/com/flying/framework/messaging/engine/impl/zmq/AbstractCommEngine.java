/*
 Created by Walker on 2017/5/1.
 Revision History:
 Date          Who              Version      What
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.ICommEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractCommEngine implements ICommEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCommEngine.class);
    private boolean running = false;
    private ZContext context;
    private ExecutorService threadPool;

    @Override
    public void start() {
        if (!running) {
            // prepare thread pool.
            threadPool = Executors.newFixedThreadPool(getWorkers());
            context = new ZContext();
            initialize(setupMsgPipe(getPipeSocketType()));
            running = true;
        } else {
            logger.info("Service is running...");
        }
    }

    @Override
    public void stop() {
        if (running) {
            context.destroy();
            threadPool.shutdown();
            running = false;
        } else {
            logger.info("Service is not running...");
        }
    }

    public ZContext getContext() {
        return context;
    }

    private ZMQ.Socket setupMsgPipe(int socketType) {
        ZMQ.Socket pipe = context.createSocket(socketType);
        IEndpoint endpoint = new Endpoint("inproc://" + System.nanoTime());
        pipe.bind(endpoint.asString());
        for (int i = 0; i < getWorkers(); i++) {
            Dispatcher dispatcher = new Dispatcher(this, ZContext.shadow(context));
            List<IEndpoint> froms = new ArrayList<>(1);
            froms.add(endpoint);
            setupDispatcherHandler(dispatcher, froms);
            threadPool.submit(dispatcher);
        }
        return pipe;
    }

    abstract void initialize(ZMQ.Socket pipe);

    abstract void setupDispatcherHandler(Dispatcher dispatcher, List<IEndpoint> froms);

    abstract int getPipeSocketType();

    abstract int getWorkers();
}