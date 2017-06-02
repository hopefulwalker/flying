/**
 * Created by Walker.Zhang on 2017/6/2.
 * Revision History:
 * Date          Who              Version      What
 * 2017/6/2     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.messaging.engine.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerCommEngineConfig;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.HashMap;
import java.util.List;

public class ServerCommEngineConfig implements IServerCommEngineConfig {
    private IEndpoint listenEndpoint;
    private int workers = 1;
    private IMsgEventListener serverListener;
    private HashMap<List<IEndpoint>, IMsgEventListener> clientListeners;

    public ServerCommEngineConfig(IEndpoint listenEndpoint, IMsgEventListener serverListener) {
        this.listenEndpoint = listenEndpoint;
        this.serverListener = serverListener;
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
        this.workers = (workers > 1) ? workers : 1;
    }

    @Override
    public IMsgEventListener getServerListener() {
        return serverListener;
    }

    @Override
    public void setServerListener(IMsgEventListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public HashMap<List<IEndpoint>, IMsgEventListener> getClientListeners() {
        return clientListeners;
    }

    public void setClientListeners(HashMap<List<IEndpoint>, IMsgEventListener> clientListeners) {
        this.clientListeners = clientListeners;
    }
}
