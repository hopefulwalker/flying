/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.common.msg.handler;

public interface IMsgHandler {
    public byte[] handle(byte[] msg);
}
