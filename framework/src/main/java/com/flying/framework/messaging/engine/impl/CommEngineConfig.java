/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker.Zhang     0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommEngineConfig implements ICommEngineConfig {
    private List<IEndpoint> endpoints;
    private int workers = 1;
    private IMsgEventListener msgEventListener;

    public CommEngineConfig(IEndpoint endpoint) {
        this.endpoints = new ArrayList<>();
        endpoints.add(endpoint);
    }

    public CommEngineConfig(List<IEndpoint> endpoints) {
        setEndpoints(endpoints);
    }

    @Override
    public List<IEndpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public void setEndpoints(List<IEndpoint> endpoints) {
        if (endpoints.size() <= 0) throw new IllegalArgumentException("endpoint size should > 0");
        this.endpoints = endpoints;
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
    public int getWorkers() {
        return workers;
    }

    /**
     * @param workers should > 0, when <=0 do nothing(==1);
     */
    @Override
    public void setWorkers(int workers) {
        if (workers > 1) this.workers = workers;
    }
}