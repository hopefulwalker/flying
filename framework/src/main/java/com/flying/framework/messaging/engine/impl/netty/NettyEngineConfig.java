/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker.Zhang     0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IEngineConfig;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.List;
import java.util.concurrent.Executor;

public class NettyEngineConfig implements IEngineConfig {
    private List<IEndpoint> endpoints;
    private Executor executor;
    private IMsgEventListener msgEventListener;

    public NettyEngineConfig(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public List<IEndpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
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
