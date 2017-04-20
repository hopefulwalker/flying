/**
 Created by Walker.Zhang on 2015/2/9.
 Revision History:
 Date          Who              Version      What
 2015/2/9      Walker.Zhang     0.1.0        Created.
 2015/2/15     Walker.Zhang     0.1.1        move this class from parent package. treat it as a implementation.
 */
package com.flying.framework.fsm.impl;

import com.flying.framework.event.IEventListener;
import com.flying.framework.fsm.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class work as the state machine. it also work as a event listener.
 * because the event source is the stated object. so we could call the stated object's onEvent
 * to handle the event.
 * if the return state is changed, we should fire a state change event.
 */
public class DefaultStateMachine implements IStateMachine {
    private List<IEventListener<IStateChangeEvent, ?>> stateChangeListeners = new ArrayList<>();

    private void fireStateChange(IStateChangeEvent sce) {
        if (stateChangeListeners == null) return;
        for (IEventListener<IStateChangeEvent, ?> listener : stateChangeListeners) {
            listener.onEvent(sce);
        }
    }

    @Override
    public void setStateChangeListeners(List<IEventListener<IStateChangeEvent, ?>> stateChangeListeners) {
        this.stateChangeListeners.addAll(stateChangeListeners);
    }

    @Override
    public IState onEvent(IStateEvent event) {
        IStateEventInfo info = event.getInfo();
        IState from = event.getSource();
        IStateChangeEvent sce = null;
        IState to = from.onEvent(event);
        if (from != to) {
            sce = new DefaultStateChangeEvent(from, new DefaultStateChangeEventInfo(info, from, to));
            fireStateChange(sce);
        }
        return to;
    }
}
