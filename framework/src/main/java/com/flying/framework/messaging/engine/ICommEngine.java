/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.event.IEventSource;

public interface ICommEngine extends IEventSource {
    /**
     * start the engine
     */
    void start();

    /**
     * stop the engine.
     */
    void stop();
}