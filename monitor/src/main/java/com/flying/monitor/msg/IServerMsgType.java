/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.msg;

public interface IServerMsgType {
    public static final short ServerRegistryRequest = 1;
    public static final short ServerRegistryReply = 2;
    public static final short ServerQueryRequest = 3;
    public static final short ServerQueryReply = 4;

}
