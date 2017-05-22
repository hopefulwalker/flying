/*
 Created by Walker.Zhang on 2017/5/19.
 Revision History:
 Date          Who              Version      What
 2015/5/19     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.framework.fsm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpringStateMachineActionLink<S, E, V> implements Action<S, E> {
    private final static String KEY_VARIABLE = "VAR_OBJECT";
    private Class<V> varType;
    private List<IAction<V>> actions;

    public SpringStateMachineActionLink(List<IAction<V>> actions, Class<V> varType) {
        if (actions == null)
            actions = new ArrayList<>();
        else
            this.actions = actions;
        this.varType = varType;
    }

    @Override
    public void execute(StateContext<S, E> context) {
        Object object = context.getExtendedState().getVariables().get(KEY_VARIABLE);
        if (object != null || varType.isAssignableFrom(object.getClass())) return;
        for (IAction<V> action : actions) {
            if (!action.execute((V) object)) return;
        }
    }
}