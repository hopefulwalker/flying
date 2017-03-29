/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEvent;
import com.flying.framework.messaging.engine.IEngine;

/**
 * This class is for Convenience. we can use IEvent<IServerEngine, IMsgEventInfo> directly because the IEvent is a generic
 * interface.
 */
public interface IMsgEvent extends IEvent<IEngine, IMsgEventInfo> {
    // control command
    public static final int ID_PING = 1;                    // ping
    public static final int ID_CONNECT = 2;                 // connect
    // request
    public static final int ID_REQUEST = 3;                 // request prefix
    // reply
    public static final int ID_REPLY_SUCCEED = 4;           // success reply
    public static final int ID_REPLY_TIMEOUT = 5;           // timeout reply
    public static final int ID_REPLY_UNSUPPORTED = 6;       // unsupported reply
    public static final int ID_REPLY_FAILED = 7;            // failed reply
}