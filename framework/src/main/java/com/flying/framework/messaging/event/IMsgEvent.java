/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/4/9      Walker           0.3.0        Refactor to support multi-communication library, such as netty.
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
    int ID_PING = 1;                    // ping
    int ID_CONNECT = 2;                 // connect
    // request
    int ID_REQUEST = 3;                 // request event
    // reply
    int ID_REPLY = 4;                   // reply event.

    int ID_REPLY_SUCCEED = 5;           // success reply
    int ID_REPLY_TIMEOUT = 6;           // timeout reply
    int ID_REPLY_UNSUPPORTED = 7;       // unsupported reply
    int ID_REPLY_FAILED = 8;            // failed reply

    int ID_MESSAGE = 9;                 // common type.
}