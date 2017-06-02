/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEventInfo;
import com.flying.framework.messaging.endpoint.IEndpoint;

import java.util.List;

public interface IMsgEventInfo extends IEventInfo {
    byte[] getBytes();

    List<IEndpoint> getFroms();
}
