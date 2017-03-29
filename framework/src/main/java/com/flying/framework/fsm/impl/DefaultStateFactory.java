/**
 Created by Walker.Zhang on 2015/2/12.
 Revision History:
 Date          Who              Version      What
 2015/2/12      Walker.Zhang    0.1.0        Created.
 2015/2/15     Walker.Zhang     0.1.1        refactor this class as the default implementation.
 */
package com.flying.framework.fsm.impl;

import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateFactory;

import java.util.Map;

/**
 * This is the default factory implementation for interface <code>IStateFactory</code>
 */
public class DefaultStateFactory implements IStateFactory<IState> {
    private Map<Byte, IState> states = null;

    /**
     * set the states that the factory hold.
     *
     * @param states the state map.
     */
    public void setStates(Map<Byte, IState> states) {
        this.states = states;
    }

    /**
     * @param id Id of the state.
     * @return the state.
     */
    public IState getState(byte id) {
        return states.get(id);
    }
}