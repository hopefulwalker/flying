/*
 Created by Walker.Zhang on 2017/5/23.
 Revision History:
 Date          Who              Version      What
 2015/5/23     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

public enum OrderEvents {
    OrderRequest,
    GetAccountByIdReply
}
