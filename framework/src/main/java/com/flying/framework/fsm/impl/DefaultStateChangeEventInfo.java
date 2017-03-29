/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.fsm.impl;

import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateChangeEventInfo;
import com.flying.framework.fsm.IStateEventInfo;

public class DefaultStateChangeEventInfo implements IStateChangeEventInfo {
    private IStateEventInfo stateEventInfo;
    private IState from;
    private IState to;

    public DefaultStateChangeEventInfo(IStateEventInfo stateEventInfo, IState from, IState to) {
        this.stateEventInfo = stateEventInfo;
        this.from = from;
        this.to = to;
    }

    @Override
    public IStateEventInfo getStateEventInfo() {
        return stateEventInfo;
    }

    @Override
    public IState getFrom() {
        return from;
    }

    @Override
    public IState getTo() {
        return to;
    }
}
