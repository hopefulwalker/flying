/**
 * Created by Walker.Zhang on 2015/4/9.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/9     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service.server;

import com.flying.ams.service.AccountServiceException;
import com.flying.ams.service.IAccountService;
import com.flying.common.service.ServiceException;
import com.flying.framework.fsm.IState;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderState;

public class AccountValidator extends AbstractOrderEventListener {
    private IAccountService accountService;

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public IState onEvent(OrderEvent event) {
        OrderBO orderBO = event.getInfo();
        try {
            if (accountService.isAccountNormal(orderBO.getAcctId()))
                return event.getSource();
            else {
                orderBO.setStateId(OrderState.REJECTED);
                orderBO.setStateEnteredCode(AccountServiceException.ACCOUNT_NOT_NORMAL);
                orderBO.setUpdateTime(System.currentTimeMillis());
                return getStateFactory().getState(orderBO.getStateId());
            }
        } catch (ServiceException se) {
            orderBO.setStateId(OrderState.REJECTED);
            orderBO.setStateEnteredCode(se.getCode());
            orderBO.setUpdateTime(System.currentTimeMillis());
            return getStateFactory().getState(orderBO.getStateId());
        }
    }
}
