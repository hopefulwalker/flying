/**
 * Created by Walker.Zhang on 2015/3/24.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/24     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.fsm;

import com.flying.framework.event.IEvent;

public interface IStateEvent extends IEvent<IState, IStateEventInfo> {
}
