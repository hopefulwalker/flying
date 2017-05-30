/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.event.IMsgEvent;

/**
 * A client engine should include the following information:
 * 1. Configuration about the communication channel.
 */
public interface IAsyncClientCommEngine extends ICommEngine {
    /**
     * Send message in asynchronous mode.
     *
     * @param msgEvent message to be sent.
     */
    void sendMsg(IMsgEvent msgEvent);
}
