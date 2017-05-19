/*
 Created by Walker.Zhang on 2017/5/19.
 Revision History:
 Date          Who              Version      What
 2015/5/19     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.ArrayList;
import java.util.List;

public class SpringStateMachineActionLink<S, E> implements Action<S, E> {
    public List<Action<S, E>> actions;

    public SpringStateMachineActionLink(List<Action<S, E>> actions) {
        if (actions == null)
            actions = new ArrayList<>();
        else
            this.actions = actions;
    }

    @Override
    public void execute(StateContext<S, E> context) {
        for (Action<S, E> action : actions) {
            action.execute(context);
            // todo figout the beter way. define new interface?
            if ((Boolean) context.getExtendedState().getVariables().get("BREAK")) {
                break;
            }
        }
    }
}