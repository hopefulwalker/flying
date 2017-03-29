/**
 * Created by Walker.Zhang on 2015/2/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/2/6      Walker.Zhang     0.1.0        Created.
 * 2015.2.15     Walker.Zhang     0.1.1        change this object as the default implementation for state change event interface.
 * 2015/2/23     Walker.Zhang     0.1.2        Refactor this class.
 */
package com.flying.framework.fsm.impl;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateChangeEvent;
import com.flying.framework.fsm.IStateChangeEventInfo;

/**
 * This is the default implementation for state change event interface.
 */
public class DefaultStateChangeEvent extends CommonEvent<IState, IStateChangeEventInfo> implements IStateChangeEvent {
    public DefaultStateChangeEvent(IState source, IStateChangeEventInfo info) {
        super(IStateChangeEvent.ID_STATE_CHANGE, source, info);
    }
}
