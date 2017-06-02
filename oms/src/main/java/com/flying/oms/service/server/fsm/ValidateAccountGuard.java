/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.ams.model.AccountBO;
import com.flying.framework.fsm.IGuard;
import com.flying.oms.model.OrderBO;

public class ValidateAccountGuard implements IGuard<OrderBO> {
    private AccountAccessor accessor;

    public void setAccountAccessor(AccountAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public boolean evaluate(OrderBO orderBO) {
        AccountBO accountBO = accessor.getAccountBO(orderBO.getAcctId());
        return accountBO == null;
    }
}
