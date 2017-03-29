/**
 * Created by Walker.Zhang on 2015/4/2.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/2     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.service;

import com.flying.ams.model.AccountBO;
import com.flying.common.service.ServiceException;

public interface IAccountService {
    public AccountBO getAccountBO(long id) throws AccountServiceException;

    public boolean isAccountNormal(long id) throws AccountServiceException;
}
