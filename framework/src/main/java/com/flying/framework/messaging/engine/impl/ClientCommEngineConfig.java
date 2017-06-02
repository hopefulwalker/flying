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
import com.flying.framework.messaging.engine.IClientCommEngineConfig;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.List;

public class ClientCommEngineConfig implements IClientCommEngineConfig {
    private List<IEndpoint> endpoints;
    private int workers = 1;
    private IMsgEventListener msgEventListener;

    public ClientCommEngineConfig(List<IEndpoint> endpoints) {
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
}