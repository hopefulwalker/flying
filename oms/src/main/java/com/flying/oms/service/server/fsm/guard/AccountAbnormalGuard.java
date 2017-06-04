/*
 Created by Walker on 2017/6/3.
 Revision History:
 Date          Who              Version      What
 2017/6/3      Walker           0.1.0        Created.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.guard;

import com.flying.ams.model.AccountBO;
import com.flying.ams.model.IAcctStatusId;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class AccountAbnormalGuard implements Guard<OrderStates, OrderEvents> {
    private AccountCenter accountCenter;

    public AccountAbnormalGuard(AccountCenter accountCenter) {
        this.accountCenter = accountCenter;
    }

    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> context) {
        AccountBO accountBO = (AccountBO) context.getExtendedState().getVariables().get(AccountBO.class.getName());
        return accountBO.getStatusId() == IAcctStatusId.NORMAL;
    }
}
