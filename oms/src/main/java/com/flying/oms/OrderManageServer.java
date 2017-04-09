/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.oms;

import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.monitor.model.IServer;
import com.flying.monitor.model.IServerStateId;
import com.flying.monitor.model.ServerBO;
import com.flying.util.schedule.Scheduler;

public class OrderManageServer implements IServer {
    private IServerEngine serverEngine;
    private ServerBO serverBO;
    private Scheduler scheduler;

    public OrderManageServer(String region, short type, String name) {
        serverBO = new ServerBO(region, type, name);
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setServerEngine(IServerEngine serverEngine) {
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
        serverEngine.start();
        if (scheduler != null) scheduler.start();
        serverBO.setStateId(IServerStateId.RUNNING);
    }

    @Override
    public void stop() {
        if (scheduler != null) scheduler.stop();
        serverEngine.stop();
        serverBO.setStateId(IServerStateId.READY);
    }
}
