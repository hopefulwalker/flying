/**
 Created by Walker.Zhang on 2015/2/6.
 Revision History:
 Date          Who              Version      What
 2015/2/6      Walker.Zhang     0.1.0        Created.
 */

package com.flying.framework.fsm;

import com.flying.framework.event.IEventInfo;

public interface IStateEventInfo extends IEventInfo {
    public byte getStateId();

    public void setStateId(byte iState);
}
