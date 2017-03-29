/**
 * Created by Walker.Zhang on 2015/2/15.
 * Revision History:
 * Date          Who              Version      What
 * 2015/2/15      Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.fsm;

import com.flying.framework.event.IEventListener;

import java.util.List;

public interface IStateMachine extends IEventListener<IStateEvent, IState> {
    public void setStateChangeListeners(List<IEventListener<IStateChangeEvent, ?>> stateChangeListeners);
}
