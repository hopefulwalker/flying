/*
 Created by Walker.Zhang on 2017/5/23.
 Revision History:
 Date          Who              Version      What
 2015/5/23     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.event;

public enum OrderEvents {
    OrderRequest,
    GetAccountByIdReply
}
