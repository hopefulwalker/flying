/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.oms.model.OrderBO;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Map;

public class SendOrderAction implements Action<OrderStates, OrderEvents> {
    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        if (context.getEvent() == OrderEvents.OrderRequest) {
            Map variables = context.getExtendedState().getVariables();
            OrderBO orderBO = (OrderBO) variables.get("ORDER");
//            orderBO.setCntrNo("CH100001");
            orderBO.setStateId((byte) OrderStates.SENT.ordinal());
        }
    }
}