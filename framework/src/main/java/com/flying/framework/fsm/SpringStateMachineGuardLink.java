/*
 Created by Walker.Zhang on 2017/5/22.
 Revision History:
 Date          Who              Version      What
 2015/5/22     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.framework.fsm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.ArrayList;
import java.util.List;

public class SpringStateMachineGuardLink<S, E> implements Guard<S, E> {
    private List<Guard<S, E>> guards;

    public SpringStateMachineGuardLink(List<Guard<S, E>> guards) {
        if (guards == null)
            this.guards = new ArrayList<>();
        else
            this.guards = guards;
    }

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        for (Guard<S, E> guard : guards) {
            if (!guard.evaluate(context)) return false;
        }
        return true;
    }
}
