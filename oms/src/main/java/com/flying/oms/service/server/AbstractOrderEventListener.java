/**
 * Created by Walker.Zhang on 2015/4/9.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/9     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service.server;

import com.flying.framework.event.IEventListener;
import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateFactory;

public abstract class AbstractOrderEventListener implements IEventListener<OrderEvent, IState> {
    private IStateFactory stateFactory;

    public IStateFactory getStateFactory() {
        return stateFactory;
    }

    public void setStateFactory(IStateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }
}
