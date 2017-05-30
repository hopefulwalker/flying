/*
 Created by Walker on 2017/5/14.
 Revision History:
 Date          Who              Version      What
 2017/5/14     Walker           0.3.5        Created for building control center.
*/
package com.flying.monitor.model;

import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.util.schedule.Scheduler;

public class Server implements IServer {
    private IServerCommEngine serverEngine;
    private ServerBO serverBO;
    private Scheduler scheduler;

    public Server(ServerBO serverBO) {
        this.serverBO = serverBO;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setServerEngine(IServerCommEngine serverEngine) {
        this.serverEngine = serverEngine;
        this.serverBO.setEndpoint(serverEngine.getListenEndpoint().asString());
        this.serverBO.setWorkers(serverEngine.getWorkers());
    }

    @Override
    public ServerBO getServerBO() {
        return serverBO;
    }

    @Override
    public void start() {
        if (serverEngine != null) serverEngine.start();
        if (scheduler != null) scheduler.start();
        serverBO.setStateId(IServerStateId.RUNNING);
    }

    @Override
    public void stop() {
        if (scheduler != null) scheduler.stop();
        if (serverEngine != null) serverEngine.stop();
        serverBO.setStateId(IServerStateId.READY);
    }
}
