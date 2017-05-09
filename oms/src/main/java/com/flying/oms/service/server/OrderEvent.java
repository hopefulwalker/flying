/**
 * Created by Walker.Zhang on 2015/3/24.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/24     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service.server;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.fsm.IState;
import com.flying.framework.fsm.IStateEvent;
import com.flying.framework.fsm.IStateEventInfo;
import com.flying.oms.model.OrderBO;

public class OrderEvent extends CommonEvent<IState, IStateEventInfo> implements IStateEvent {
    public static final int ID_INITIALIZE = 1;

    public OrderEvent(int id, IState source, IStateEventInfo info) {
        super(id, source, info);
    }

    @Override
    public OrderBO getInfo() {
        return (OrderBO) super.getInfo();
    }
}

