/**
 Created by Walker.Zhang on 2015/2/6.
 Revision History:
 Date          Who              Version      What
 2015/2/6      Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.fsm;

import com.flying.framework.event.IEventListener;
import com.flying.framework.event.IEventResult;
import com.flying.framework.event.IEventSource;

public interface IState extends IEventSource, IEventResult, IEventListener<IStateEvent, IState> {
    public byte getId();
    public boolean isFinal();
}
