package com.flying.framework.fsm;/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */

import com.flying.framework.event.IEventInfo;

public interface IStateChangeEventInfo extends IEventInfo {
    public IStateEventInfo getStateEventInfo();

    public IState getFrom();

    public IState getTo();
}
