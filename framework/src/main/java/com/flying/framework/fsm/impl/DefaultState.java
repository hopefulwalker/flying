/**
 Created by Walker.Zhang on 2015/2/12.
 Revision History:
 Date          Who              Version      What
 2015/2/12     Walker.Zhang     0.1.0        Created.
 2015/2/13     Walker.Zhang     0.1.1        add on Event implementation
 2015/2/15     Walker.Zhang     0.1.2        refactor from order state.
 */
package com.flying.framework.fsm.impl;

import com.flying.framework.event.IEventListener;
import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateEvent;

import java.util.List;
import java.util.Map;

public class DefaultState implements IState {
    private byte sId;
    private boolean finalState = false;
    // key = event name , value = list of event listeners
    private Map<Integer, List<IEventListener<IStateEvent, IState>>> listeners = null;

    public DefaultState(byte sId) {
        this.sId = sId;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public void setListeners(Map<Integer, List<IEventListener<IStateEvent, IState>>> listeners) {
        this.listeners = listeners;
    }

    @Override
    public byte getId() {
        return sId;
    }

    @Override
    public boolean isFinal() {
        return finalState;
    }

    /**
     * Because we assume that we should build the state cache, use == to judge whether the state are same.
     * If the event listener return different state, it means to the chain should be stop and return.
     *
     * @param event the event
     * @return the new state if exists, if no state change return self.
     */
    @Override
    public IState onEvent(IStateEvent event) {
        IState to;
        if (!listeners.containsKey(event.getId())) return this;
        for (IEventListener<IStateEvent, IState> listener : listeners.get(event.getId())) {
            to = listener.onEvent(event);
            if (this != to) return to;
        }
        return this;
    }
}
