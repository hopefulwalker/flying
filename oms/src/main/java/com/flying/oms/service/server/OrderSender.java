package com.flying.oms.service.server;

import com.flying.framework.fsm.IState;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderState;

public class OrderSender extends AbstractOrderEventListener {
    @Override
    public IState onEvent(OrderEvent event) {
        OrderBO orderBO = event.getInfo();
        if (event.getId() == OrderEvent.ID_INITIALIZE) {
            orderBO = event.getInfo();
            orderBO.setCntrNo("CH100001");
            orderBO.setStateId(OrderState.SENT);
        }
        return getStateFactory().getState(orderBO.getStateId());
    }
}
