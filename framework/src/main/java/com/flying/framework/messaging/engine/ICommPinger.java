/*
 Created by Walker.Zhang on 2015/3/19.
 Revision History:
 Date          Who              Version      What
 2015/3/19     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.endpoint.IEndpoint;

public interface ICommPinger extends ICommEngine {
    /**
     * ping endpoint and wait for the reply before timeout.
     *
     * @param endpoint target to ping.
     * @param timeout  milliseconds.
     * @return true if success, false if fail.
     */
    public boolean ping(IEndpoint endpoint, int timeout);
}
