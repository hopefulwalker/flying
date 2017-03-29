/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.messaging.event.IMsgEventResult;

public class MsgEventResult implements IMsgEventResult {
    private byte[] resultBytes;
    private boolean replyRequired;

    public MsgEventResult(byte[] bytes) {
        this.replyRequired = (bytes != null);
        this.resultBytes = bytes;
    }

    @Override
    public byte[] getByteArray() {
        return resultBytes;
    }

    @Override
    public boolean isReplyRequired() {
        return replyRequired;
    }
}
