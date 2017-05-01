/*
 Created by Walker on 2017/5/1.
 Revision History:
 Date          Who              Version      What
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractAsyncEngine implements IEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncEngine.class);
    //    private IEndpoint listenEndpoint;
    private boolean running = false;
    private ZContext context;

    private int dispatchers = 1;
    private String dispatcherURL;
    private ExecutorService threadPool;
    private IMsgEventListener msgEventListener;


    private List<IEndpoint> endpoints;
    private ZMQ.Socket pipe;             //  Pipe through to background


    private ZMQ.Socket setupMsgPipe(int socketType) {
        ZMQ.Socket pipe = context.createSocket(socketType);
        IEndpoint endpoint = new Endpoint(dispatcherURL);
        pipe.bind(endpoint.asString());
        for (int i = 0; i < dispatchers; i++) {
            List<IEndpoint> froms = new ArrayList<>(1);
            froms.add(endpoint);
            Dispatcher dispatcher = new Dispatcher(this, ZContext.shadow(context));
            setupDispatcherHandler(dispatcher);
            threadPool.submit(dispatcher);
        }
        return pipe;
    }

    @Override
    public void start() {
        if (!running) {
            // prepare thread pool.
            threadPool = Executors.newFixedThreadPool(dispatchers);
            context = new ZContext();
            dispatcherURL = "inproc://" + System.nanoTime();
            setupMsgPipe(getPipeSocketType());
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

    abstract int getPipeSocketType();

    abstract int setupDispatcherHandler(Dispatcher dispatcher);
}
