/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
 2015/2/23     Walker.Zhang     0.1.0        Created.
 2017/6/1      Walker.Zhang     0.3.7        Add two more event info to support asynchronous message handler.
*/
package com.flying.framework.messaging.event.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventInfo;

import java.util.List;

public class MsgEventInfo implements IMsgEventInfo {
    private byte[] msgBytes;
    private List<IEndpoint> froms;

    public MsgEventInfo(byte[] msgBytes, List<IEndpoint> froms) {
        this.msgBytes = msgBytes;
        this.froms = froms;
    }

    public MsgEventInfo(byte[] bytes) {
        this(bytes, null);
    }

    @Override
    public byte[] getBytes() {
        return msgBytes;
    }

    @Override
    public List<IEndpoint> getFroms() {
        return this.froms;
    }
}