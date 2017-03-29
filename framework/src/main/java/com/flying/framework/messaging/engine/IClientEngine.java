/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created.
 2015/2/27     Walker.Zhang     0.2.0        Refactor this class with the whole messaging framework.
 2015/3/2      Walker.zhang     0.2.1        Refactor: force to to use timeout.
                                             timeout =-1 has no use any more.
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;

import java.util.List;

public interface IClientEngine extends IEngine {
    public List<IEndpoint> getEndpoints();

    public void setEndpoints(List<IEndpoint> endpoints);

    /**
     * @param msgEvent the message that need to be sent out.
     * @param timeout  it shall wait timeout milliseconds for the reply to come back.
     *                 If the value of timeout is 0, it shall return immediately.
     *                 If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event, null when the context shut down.
     */
    public IMsgEvent request(IMsgEvent msgEvent, int timeout);

    public void sendMsg(IMsgEvent msgEvent);

    /**
     * Poll socket for a reply, with timeout
     *
     * @param timeout it shall wait timeout milliseconds for the reply to come back.
     *                If the value of timeout is 0, it shall return immediately.
     *                If the value of timeout < 0, it shall use 0 directly.
     * @return the reply message event, null when the context shut down.
     */
    public IMsgEvent recvMsg(int timeout);
}
