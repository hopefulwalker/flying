/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEventResult;

public interface IMsgEventResult extends IEventResult {
    /**
     * @return the reply information for the request. it has no use if the required flag is false, It's a suggestion
     * to return null when replyRequired is false.
     */
    public byte[] getByteArray();

    /**
     * @return false when the request doesn't need to reply, or the reply will sent back through the listener(used it
     * when processed in asynchronous mode)
     */
    public boolean isReplyRequired();
}
