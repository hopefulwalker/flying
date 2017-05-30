/*
 Created by Walker.Zhang on 2017/5/22.
 Revision History:
 Date          Who              Version      What
 2015/5/22     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.framework.fsm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class SpringStateMachineGuardLink<S, E, V> implements Guard<S, E> {
    private Class<V> varClass;
    private List<IGuard<V>> guards;

    public SpringStateMachineGuardLink(List<IGuard<V>> guards) {
        this.guards = guards;
        if (guards != null && guards.size() >= 1) {
            this.varClass = (Class<V>) ((ParameterizedType) guards.get(0).getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        }
    }

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        if (guards == null || guards.size() == 0) return true;
        Object object = context.getExtendedState().getVariables().get(this.varClass.getName());
        if (object == null) return false;
        if (!varClass.isAssignableFrom(object.getClass())) return false;
        for (IGuard<V> guard : guards) {
            if (!guard.evaluate((V) object)) return false;
        }
        return true;
    }
}
