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
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class InMemoryStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, OrderStates> {
    private final Map<OrderStates, BlockingQueue<StateMachineContext<OrderStates, OrderEvents>>> contexts = new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> context, OrderStates contextObj) throws Exception {
        if (contextObj == OrderStates.CREATED || contextObj == OrderStates.SENT) return;
        BlockingQueue<StateMachineContext<OrderStates, OrderEvents>> queue;
        if (contexts.containsKey(contextObj))
            queue = contexts.get(contextObj);
        else {
            queue = new LinkedBlockingDeque<>();
            contexts.put(contextObj, queue);
        }
        queue.add(context);
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(OrderStates contextObj) throws Exception {
        if (!contexts.containsKey(contextObj)) return null;
        return contexts.get(contextObj).remove();
    }
}