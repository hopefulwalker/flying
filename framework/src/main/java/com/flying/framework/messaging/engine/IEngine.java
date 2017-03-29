/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.event.IEventSource;

public interface IEngine extends IEventSource {
    public void start();

    public void stop();
}
