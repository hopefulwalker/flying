/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created.
 2015/2/27     Walker.Zhang     0.2.0        Refactor this class with the whole messaging framework.
 2015/3/2      Walker.zhang     0.2.1        Refactor: force to to use timeout.
                                             timeout =-1 has no use any more.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.event.IMsgEvent;

public interface IClientCommEngine extends ICommEngine {

    IClientCommEngineConfig getConfig();

    void setConfig(IClientCommEngineConfig config);

    /**
     * Send message in asynchronous mode.
     *
     * @param msgEvent message to be sent.
     */
    void sendMsg(IMsgEvent msgEvent);

    /**
     * Poll socket for a reply, with timeout
     *
     * @param timeout it shall wait timeout milliseconds for the reply to come back.
     *                If the value of timeout is 0, it shall return immediately.
     *                If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event, null when the context shut down.
     */
    IMsgEvent recvMsg(int timeout);

    /**
     * @param msgEvent the message that need to be sent out.
     * @param timeout  it shall wait timeout milliseconds for the reply to come back.
     *                 If the value of timeout is 0, it shall return immediately.
     *                 If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event, null when the context shut down.
     */
    IMsgEvent request(IMsgEvent msgEvent, int timeout);
}
