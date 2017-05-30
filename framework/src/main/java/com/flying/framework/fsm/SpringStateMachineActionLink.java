/*
 Created by Walker.Zhang on 2017/5/19.
 Revision History:
 Date          Who              Version      What
 2015/5/19     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.framework.fsm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpringStateMachineActionLink<S, E, V> implements Action<S, E> {
    private Class<V> varClass;
    private List<IAction<V>> actions;

    public SpringStateMachineActionLink(List<IAction<V>> actions) {
        this.actions = actions;
        if (actions != null && actions.size() >= 1) {
            this.varClass = (Class<V>) ((ParameterizedType) actions.get(0).getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        }
    }

    @Override
    public void execute(StateContext<S, E> context) {
        if (actions == null || actions.size() == 0) return;
        Object object = context.getExtendedState().getVariables().get(this.varClass.getName());
        if (object == null) return;
        if (!varClass.isAssignableFrom(object.getClass())) return;
        for (IAction<V> action : actions) {
            if (!action.execute((V) object)) return;
        }
    }
}