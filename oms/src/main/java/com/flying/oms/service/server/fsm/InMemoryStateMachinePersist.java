/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.oms.model.OrderStates;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class InMemoryStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, OrderStates> {
    private final HashMap<OrderStates, StateMachineContext<OrderStates, OrderEvents>> contexts = new HashMap<>();

    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> context, OrderStates contextObj) throws Exception {
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(OrderStates contextObj) throws Exception {
        return contexts.get(contextObj);
    }
}