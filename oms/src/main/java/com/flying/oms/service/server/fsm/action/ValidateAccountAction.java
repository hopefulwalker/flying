/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.action;

import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ValidateAccountAction implements Action<OrderStates, OrderEvents> {
    private AccountCenter accountCenter;

    public ValidateAccountAction(AccountCenter accountCenter) {
        this.accountCenter = accountCenter;
    }

    @Override
    public void execute(StateContext context) {
        OrderBO orderBO = (OrderBO) context.getExtendedState().getVariables().get(OrderBO.class.getName());
        byte[] bytes = accountCenter.encodeGetAccountByIdRequest(orderBO);
        context.getExtendedState().getVariables().put(IMsgEventResult.class.getName(),
                new MsgEventResult(accountCenter.getEndpoints(), bytes));
        orderBO.setState(OrderStates.CHECKING_ACCOUNT);
    }
}