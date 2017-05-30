/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/4/9      Walker.Zhang     0.3.0        Refactor to support multi-communication library, such as netty.
 2017/4/26     Walker.Zhang     0.3.3        Refactor type of message event.
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID.
*/
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEvent;
import com.flying.framework.messaging.engine.ICommEngine;

/**
 * This class is for Convenience. we can use IEvent<IServerCommEngine, IMsgEventInfo> directly because the IEvent is a generic
 * interface.
 * This class define 3 kinds of message id. currently it used for inside engine.
 */
public interface IMsgEvent extends IEvent<ICommEngine, IMsgEventInfo> {
    // control command
    int ID_PING = 1;                   // heart beat event ping.
    int ID_PONG = 11;                  // heart beat event pong.
    int ID_REQUEST = 2;                // request event
    int ID_REPLY = 21;                 // reply event.
    int ID_TIMEOUT = 22;               // timeout reply
    int ID_FAILED = 23;                // failed reply
    int ID_MESSAGE = 3;                // MESSAGE EVENT.
}