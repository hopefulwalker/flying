/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker           0.3.0        Created.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventListener;

import java.util.List;

public interface IClientCommEngineConfig {
    List<IEndpoint> getEndpoints();

    void setEndpoints(List<IEndpoint> endpoints);

    IMsgEventListener getMsgEventListener();

    void setMsgEventListener(IMsgEventListener msgEventListener);
}