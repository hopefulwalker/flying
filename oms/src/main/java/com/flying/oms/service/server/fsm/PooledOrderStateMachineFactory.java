/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.statemachine.StateMachine;

public class PooledOrderStateMachineFactory implements PooledObjectFactory<StateMachine<OrderStates, OrderEvents>>, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public PooledObject<StateMachine<OrderStates, OrderEvents>> makeObject() throws Exception {
        StateMachine<OrderStates, OrderEvents> machine = (StateMachine<OrderStates, OrderEvents>) context.getBean("orderStateMachine");
        machine.start();
        return new DefaultPooledObject<>(machine);
    }

    @Override
    public void destroyObject(PooledObject<StateMachine<OrderStates, OrderEvents>> pooledObject) throws Exception {
        pooledObject.getObject().stop();
    }

    @Override
    public boolean validateObject(PooledObject<StateMachine<OrderStates, OrderEvents>> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<StateMachine<OrderStates, OrderEvents>> pooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<StateMachine<OrderStates, OrderEvents>> pooledObject) throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
