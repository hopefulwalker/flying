/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.action;

import com.flying.common.IReturnCode;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.OrderCenter;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class SendOrderAction implements Action<OrderStates, OrderEvents> {
    private OrderCenter orderCenter;

    public SendOrderAction(OrderCenter orderCenter) {
        this.orderCenter = orderCenter;
    }

    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        OrderBO orderBO = (OrderBO) context.getExtendedState().getVariables().get(OrderBO.class.getName());
        orderBO.setCntrNo("CH100001");
        orderBO.setState(OrderStates.SENT);
        context.getExtendedState().getVariables().put(IMsgEventResult.class.getName(),
                new MsgEventResult(orderCenter.getOrderFroms(orderBO.getOid()), orderCenter.encodeOrderReply(IReturnCode.SUCCESS, orderBO)));
    }
}