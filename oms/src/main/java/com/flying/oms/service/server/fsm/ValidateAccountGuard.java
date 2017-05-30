/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.ams.service.AccountServiceException;
import com.flying.ams.service.IAccountService;
import com.flying.common.service.ServiceException;
import com.flying.framework.fsm.IGuard;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;

public class ValidateAccountGuard implements IGuard<OrderBO> {
    private IAccountService accountService;

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean evaluate(OrderBO orderBO) {
        try {
            if (accountService.isAccountNormal(orderBO.getAcctId())) return true;
            orderBO.setState(OrderStates.REJECTED);
            orderBO.setStateEnteredCode(AccountServiceException.ACCOUNT_NOT_NORMAL);
            orderBO.setUpdateTime(System.currentTimeMillis());
        } catch (ServiceException se) {
            orderBO.setState(OrderStates.REJECTED);
            orderBO.setStateEnteredCode(se.getCode());
            orderBO.setUpdateTime(System.currentTimeMillis());
        }
        return false;

    }
}
