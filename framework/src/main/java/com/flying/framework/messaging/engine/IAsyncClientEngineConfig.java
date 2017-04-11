/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker           0.3.0        Created.
*/
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.List;
import java.util.concurrent.Executor;

public interface IAsyncClientEngineConfig {
    List<IEndpoint> getEndpoints();

    Executor getExecutor();

    void setExecutor(Executor executor);

    IMsgEventListener getMsgEventListener();

    void setMsgEventListener(IMsgEventListener msgEventListener);
}