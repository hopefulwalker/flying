/*
 Created by Walker on 2017/5/14.
 Revision History:
 Date          Who              Version      What
 2017/5/14     Walker           0.3.5        Created for building control center.
*/
package com.flying.monitor.model;

import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractServerManager implements IServerManager {
    private IServer server = null;
    private AbstractApplicationContext context = null;

    protected void setServer(String bean) {
        this.server = context.getBean(bean, IServer.class);
    }

    protected void setContext(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public void startServer() {
        if (server != null) server.start();
    }

    @Override
    public void stopServer() {
        if (server != null) server.stop();
    }

    @Override
    public void destroyServer() {
        if (server != null) server.stop();
        if (context != null) context.close();
    }
}
