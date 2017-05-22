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
import com.flying.oms.model.OrderBO;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;

import java.util.Map;

public class ValidateAccountGuard implements Guard<OrderStates, OrderEvents> {
    private IAccountService accountService;

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> context) {
        Map variables = context.getExtendedState().getVariables();
        OrderBO orderBO = (OrderBO) variables.get("ORDER");
        try {
            if (accountService.isAccountNormal(orderBO.getAcctId())) return true;
            orderBO.setStateId((byte) OrderStates.REJECTED.ordinal());
            orderBO.setStateEnteredCode(AccountServiceException.ACCOUNT_NOT_NORMAL);
            orderBO.setUpdateTime(System.currentTimeMillis());
        } catch (ServiceException se) {
            orderBO.setStateId((byte) OrderStates.REJECTED.ordinal());
            orderBO.setStateEnteredCode(se.getCode());
            orderBO.setUpdateTime(System.currentTimeMillis());
        }
        return false;
    }
}
