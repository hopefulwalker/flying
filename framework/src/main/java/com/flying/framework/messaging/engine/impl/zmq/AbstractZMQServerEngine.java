/*
 Created by Walker.Zhang on 2015/5/19.
 Revision History:
 Date          Who              Version      What
 2015/5/19     Walker.Zhang     0.1.0        Created.
*/
package com.flying.framework.messaging.engine.impl.zmq;


import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.engine.IServerEngine;
import org.zeromq.ZContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractZMQServerEngine implements IServerEngine, Runnable {
    private IEndpoint listenEndpoint;
    private int workers;
    private IMsgEventListener msgEventListener;
    private ZContext context;
    private ExecutorService threadPool;
    private boolean running = false;
    private String workerURL;

    public AbstractZMQServerEngine(IEndpoint listenEndpoint) {
        this.listenEndpoint = listenEndpoint;
        this.workers = 1;
        this.msgEventListener = null;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public String getWorkerURL() {
        return workerURL;
    }

    public ZContext getContext() {
        return context;
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
    public int getWorkers() {
        return workers;
    }

    @Override
    public void setWorkers(int workers) {
        this.workers = workers;
    }

    @Override
    public IMsgEventListener getMsgEventListener() {
        return msgEventListener;
    }

    @Override
    public void setMsgEventListener(IMsgEventListener msgEventListener) {
        this.msgEventListener = msgEventListener;
    }

    @Override
    public void start() {
        if (!running) {
            // prepare thread pool.
            threadPool = Executors.newFixedThreadPool(workers);
            context = new ZContext();
            new Thread(this, "ServerEngine").start();
            this.workerURL = "inproc://" + System.nanoTime();
            running = true;
        }
    }

    @Override
    public void stop() {
        if (running) {
            context.destroy();
            threadPool.shutdown();
            running = false;
        }
    }

    @Override
    public abstract void run();
}
