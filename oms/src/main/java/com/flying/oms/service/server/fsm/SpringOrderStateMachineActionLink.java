/*
 Created by Walker on 2017/5/23.
 Revision History:
 Date          Who              Version      What
 2017/5/23      Walker           0.1.0        Created. 
*/
package com.flying.oms.service.server.fsm;

import com.flying.framework.fsm.IAction;
import com.flying.framework.fsm.SpringStateMachineActionLink;
import com.flying.oms.model.OrderBO;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class SpringOrderStateMachineActionLink implements Action<OrderStates, OrderEvents> {
    private List<IAction<OrderBO>> actions;

    public SpringOrderStateMachineActionLink(List<IAction<OrderBO>> actions) {
        this.actions = actions;
    }

    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        if (actions == null || actions.size() == 0) return;
        Object object = context.getExtendedState().getVariables().get(OrderBO.class.getName());
        if (!(object instanceof OrderBO)) return;
        for (IAction<OrderBO> action : actions) {
            if (!action.execute((OrderBO) object)) return;
        }
    }
}
