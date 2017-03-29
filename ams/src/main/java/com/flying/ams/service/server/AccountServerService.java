/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.service.server;

import com.flying.ams.model.AccountBO;
import com.flying.ams.model.IAcctStatusId;
import com.flying.ams.service.AccountServiceException;
import com.flying.ams.service.IAccountService;
import com.flying.util.db.DSManagerException;

import java.util.Map;

public class AccountServerService implements IAccountService {
    private Map<Long, AccountBO> accountCache;
    private AccountDBLoader loader;

    public AccountServerService(Map<Long, AccountBO> accountCache, AccountDBLoader loader) {
        this.accountCache = accountCache;
        this.loader = loader;
    }

    @Override
    public boolean isAccountNormal(long id) throws AccountServiceException {
        AccountBO accountBO = getAccountBO(id);
        return accountBO.getStatusId() == IAcctStatusId.NORMAL;
    }

    @Override
    public AccountBO getAccountBO(long id) throws AccountServiceException {
        // 1. check local cache at fist
        if (accountCache.containsKey(id)) return accountCache.get(id);
        AccountBO accountBO;
        try {
            accountBO = loader.load(id);
        } catch (DSManagerException dsme) {
            throw new AccountServiceException(AccountServiceException.FAILED_TO_LOAD_DATA_FROM_DB_SERVER, dsme);
        }
        if (accountBO != null) {
            accountCache.put(id, accountBO);
        } else {
            throw new AccountServiceException(AccountServiceException.ACCOUNT_NOT_EXISTS, "Account=[" + id + "]");
        }
        return accountBO;
    }
}