/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/6/1      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
 */
package com.flying.common.msg.handler;

import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;

public interface IMsgHandler {
    IMsgEventResult handle(IMsgEvent event);
}
