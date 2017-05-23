/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server;

import com.flying.framework.event.IEventSource;
import com.flying.oms.model.OrderBO;
import com.flying.oms.service.IOrderService;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.model.OrderEvents;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.fsm.PooledOrderStateMachineFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.Map;

public class OrderServerService implements IOrderService, IEventSource {
    private static Logger logger = LoggerFactory.getLogger(OrderServerService.class);
    private Map<Long, OrderBO> orderCache;
    private GenericObjectPoolConfig poolConfig;
    private PooledOrderStateMachineFactory poolFactory;
    private ObjectPool<StateMachine<OrderStates, OrderEvents>> stateMachineObjectPool;
    private StateMachinePersister<OrderStates, OrderEvents, OrderStates> statesStateMachinePersister;

    public void setStatesStateMachinePersister(StateMachinePersister<OrderStates, OrderEvents, OrderStates> statesStateMachinePersister) {
        this.statesStateMachinePersister = statesStateMachinePersister;
    }

    public OrderServerService(PooledOrderStateMachineFactory poolFactory, GenericObjectPoolConfig poolConfig, Map<Long, OrderBO> orderCache) {
        this.poolFactory = poolFactory;
        this.poolConfig = poolConfig;
        this.stateMachineObjectPool = new GenericObjectPool<>(poolFactory, poolConfig);
        this.orderCache = orderCache;
        try {
            PoolUtils.prefill(stateMachineObjectPool, poolConfig.getMinIdle());
        } catch (Exception e) {
            logger.error("Failed to prefill statemachine.", e);
        }
    }

    @Override
    public OrderBO placeOrder(OrderBO orderBO) throws OrderServiceException {
        StateMachine<OrderStates, OrderEvents> machine = null;
        try {
            machine = stateMachineObjectPool.borrowObject();
            statesStateMachinePersister.restore(machine, OrderStates.values()[orderBO.getState().ordinal()]);
            machine.getExtendedState().getVariables().put(OrderBO.class.getName(), orderBO);
            machine.sendEvent(OrderEvents.OrderRequest);
        } catch (Exception e) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_PLACE_ORDER, e);
        } finally {
            try {
                stateMachineObjectPool.returnObject(machine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
        return orderBO;
    }
}
