/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm.guard;

import com.flying.ams.model.AccountBO;
import com.flying.ams.model.IAcctStatusId;
import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class AccountNormalGuard implements Guard<OrderStates, OrderEvents> {
    private static final Logger logger = LoggerFactory.getLogger(AccountNormalGuard.class);
    private AccountCenter accountCenter;

    public AccountNormalGuard(AccountCenter accountCenter) {
        this.accountCenter = accountCenter;
    }

    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> context) {
        AccountBO accountBO = (AccountBO) context.getExtendedState().getVariables().get(AccountBO.class.getName());
        if (accountBO == null) logger.error("accountBO==NULL");
        return accountBO.getStatusId() == IAcctStatusId.NORMAL;
    }
}
