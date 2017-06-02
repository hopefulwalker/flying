/*
 Created by Walker on 2017/4/12.
 Revision History:
 Date          Who              Version      What
 2017/4/12     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine;

public interface IServerCommEngine extends ICommEngine {
    IServerCommEngineConfig getConfig();

    void setConfig(IServerCommEngineConfig config);
}
