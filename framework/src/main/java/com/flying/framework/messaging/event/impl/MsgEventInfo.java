/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.messaging.event.IMsgEventInfo;

public class MsgEventInfo implements IMsgEventInfo {
    private byte[] msgBytes;

    public MsgEventInfo(byte[] bytes) {
        this.msgBytes = bytes;
    }

    @Override
    public byte[] getByteArray() {
        return msgBytes;
    }
}