/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.framework.fsm.IGuard;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;

public class SendOrderGuard implements IGuard<OrderBO> {
    @Override
    public boolean evaluate(OrderBO orderBO) {
        orderBO.setCntrNo("CH100001");
        orderBO.setState(OrderStates.SENT);
        return true;
    }
}