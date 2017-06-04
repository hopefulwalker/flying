/*
 Created by Walker on 2017/6/4.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server;

import com.flying.ams.model.AccountBO;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.service.server.fsm.PooledOrderStateMachineFactory;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.List;

public class OrderServerService {
    private static Logger logger = LoggerFactory.getLogger(OrderServerService.class);
    private AccountCenter accountCenter;
    private OrderCenter orderCenter;

    private GenericObjectPoolConfig poolConfig;
    private PooledOrderStateMachineFactory poolFactory;
    private ObjectPool<StateMachine<OrderStates, OrderEvents>> stateMachineObjectPool;
    private StateMachinePersister<OrderStates, OrderEvents, OrderStates> statesStateMachinePersister;

    public OrderServerService(PooledOrderStateMachineFactory poolFactory, GenericObjectPoolConfig poolConfig,
                              AccountCenter accountCenter, OrderCenter orderCenter,
                              StateMachinePersister<OrderStates, OrderEvents, OrderStates> statesStateMachinePersister) {
        this.accountCenter = accountCenter;
        this.orderCenter = orderCenter;

        this.poolFactory = poolFactory;
        this.poolConfig = poolConfig;
        this.stateMachineObjectPool = new GenericObjectPool<>(poolFactory, poolConfig);
        this.statesStateMachinePersister = statesStateMachinePersister;
    }

    public void init() {
        try {
            PoolUtils.prefill(stateMachineObjectPool, poolConfig.getMinIdle());
        } catch (Exception e) {
            logger.error("Failed to prefill state machine pool.", e);
        }
    }

    public void destroy() {
        stateMachineObjectPool.close();
    }

    public IMsgEventResult placeOrder(List<IEndpoint> froms, OrderBO orderBO) throws OrderServiceException {
        orderCenter.saveOrderBO(froms, orderBO);
        StateMachine<OrderStates, OrderEvents> machine = null;
        try {
            machine = stateMachineObjectPool.borrowObject();
            statesStateMachinePersister.restore(machine, orderBO.getState());
            machine.getExtendedState().getVariables().put(OrderBO.class.getName(), orderBO);
            machine.sendEvent(OrderEvents.OrderRequest);
            IMsgEventResult result = (IMsgEventResult) machine.getExtendedState().getVariables().get(IMsgEventResult.class.getName());
            machine.getExtendedState().getVariables().clear();
            statesStateMachinePersister.persist(machine, orderBO.getState());
            return result;
        } catch (Exception e) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_PLACE_ORDER, e);
        } finally {
            try {
                stateMachineObjectPool.returnObject(machine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
    }

    public IMsgEventResult placeOrder(long requestNo, AccountBO accountBO) {
        OrderBO orderBO = orderCenter.getOrderBO(accountCenter.removeOrderID(requestNo));
        StateMachine<OrderStates, OrderEvents> machine = null;
        IMsgEventResult result;
        try {
            machine = stateMachineObjectPool.borrowObject();
            statesStateMachinePersister.restore(machine, orderBO.getState());
            machine.getExtendedState().getVariables().put(OrderBO.class.getName(), orderBO);
            machine.getExtendedState().getVariables().put(AccountBO.class.getName(), accountBO);
            machine.sendEvent(OrderEvents.GetAccountByIdReply);
            result = (IMsgEventResult) machine.getExtendedState().getVariables().get(IMsgEventResult.class.getName());
            machine.getExtendedState().getVariables().clear();
            statesStateMachinePersister.persist(machine, orderBO.getState());
            // todo erase this statement later.
            orderCenter.clearOrderBO(orderBO.getOid());
        } catch (Exception e) {
            result = new MsgEventResult(orderCenter.getOrderFroms(orderBO.getOid()),
                    orderCenter.encodeOrderReply(OrderServiceException.FAILED_TO_PLACE_ORDER));
        } finally {
            try {
                stateMachineObjectPool.returnObject(machine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
        return result;
    }
}