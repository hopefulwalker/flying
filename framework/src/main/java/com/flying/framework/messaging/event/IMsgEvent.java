/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/4/9      Walker.Zhang     0.3.0        Refactor to support multi-communication library, such as netty.
 2017/4/26     Walker.Zhang     0.3.3        Refactor type of message event.
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
    int ID_PING = 1;                    // heart beat event ping.
    int ID_PONG = 2;                    // heart beat event pong.
    int ID_MESSAGE = 3;                 // MESSAGE EVENT.
    @Deprecated
    int ID_REPLY_UNSUPPORTED = 9;       // unsupported reply

    @Deprecated
    int ID_CONNECT = 4;                 // connect
    @Deprecated
    int ID_REQUEST = 5;                 // request event
    @Deprecated
    int ID_REPLY = 6;                   // reply event.
    @Deprecated
    int ID_REPLY_SUCCEED = 7;           // success reply
    @Deprecated
    int ID_REPLY_TIMEOUT = 8;           // timeout reply
    @Deprecated
    int ID_REPLY_FAILED = 10;            // failed reply
}