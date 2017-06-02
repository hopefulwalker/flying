/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEventResult;
import com.flying.framework.messaging.endpoint.IEndpoint;

import java.util.List;

public interface IMsgEventResult extends IEventResult {
    List<IEndpoint> getTarget();

    /**
     * @return the reply information for the request. it has no use if the required flag is false, It's a suggestion
     * to return null when replyRequired is false.
     */
    byte[] getBytes();
}
