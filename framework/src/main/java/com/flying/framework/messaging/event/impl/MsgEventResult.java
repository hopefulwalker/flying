/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventResult;

import java.util.List;

public class MsgEventResult implements IMsgEventResult {
    List<IEndpoint> target;
    private byte[] bytes;

    public MsgEventResult(List<IEndpoint> target, byte[] bytes) {
        this.target = target;
        this.bytes = bytes;
    }

    @Override
    public List<IEndpoint> getTarget() {
        return target;
    }

    public void setTarget(List<IEndpoint> target) {
        this.target = target;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
