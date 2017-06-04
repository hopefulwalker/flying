/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.guard;

import com.flying.ams.model.AccountBO;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class NoneAccountGuard implements Guard<OrderStates, OrderEvents> {
    private AccountCenter accountCenter;

    public NoneAccountGuard(AccountCenter accountCenter) {
        this.accountCenter = accountCenter;
    }

    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> context) {
        OrderBO orderBO = (OrderBO) context.getExtendedState().getVariables().get(OrderBO.class.getName());
        AccountBO accountBO = accountCenter.getAccountBO(orderBO.getAcctId());
        return accountBO == null;
    }
}
