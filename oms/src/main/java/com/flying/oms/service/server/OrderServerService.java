/**
 * Created by Walker.Zhang on 2015/3/24.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/24     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service.server;

import com.flying.framework.event.IEventSource;
import com.flying.framework.fsm.IStateFactory;
import com.flying.framework.fsm.IStateMachine;
import com.flying.oms.model.OrderBO;
import com.flying.oms.service.IOrderService;
import com.flying.oms.service.OrderServiceException;

import java.util.Map;

public class OrderServerService implements IOrderService, IEventSource {
    private IStateMachine stateMachine;
    private IStateFactory stateFactory;
    private Map<Long, OrderBO> orderCache;

    public OrderServerService(IStateMachine stateMachine, IStateFactory stateFactory, Map<Long, OrderBO> orderCache) {
        this.stateMachine = stateMachine;
        this.stateFactory = stateFactory;
        this.orderCache = orderCache;
    }

    @Override
    public OrderBO placeOrder(OrderBO orderBO) throws OrderServiceException {
//        orderCache.put(orderBO.getOid(), orderBO);
        stateMachine.onEvent(new OrderEvent(OrderEvent.ID_INITIALIZE, stateFactory.getState(orderBO.getStateId()), orderBO));
        return orderBO;
    }
}
