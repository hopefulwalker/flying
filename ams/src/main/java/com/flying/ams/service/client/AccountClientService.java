/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6      Walker.Zhang     0.1.0        Created.
 * 2015/5/27     Walker.Zhang     0.1.1        Refactor to support msg codec. don't use proxy any more.
 */
package com.flying.ams.service.client;

import com.flying.ams.model.AccountBO;
import com.flying.ams.model.IAcctStatusId;
import com.flying.ams.msg.converter.IAccountMsgCodec;
import com.flying.ams.service.AccountServiceException;
import com.flying.ams.service.IAccountService;
import com.flying.common.msg.codec.Helper;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.client.BaseUCClientService;
import com.flying.framework.messaging.engine.IClientEngine;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AccountClientService extends BaseUCClientService implements IAccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountClientService.class);
    private Map<Long, AccountBO> accountCache = null;
    private IAccountMsgCodec msgConverter;

    public AccountClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, IAccountMsgCodec msgConverter) {
        super(region, IServiceType.ACCOUNT, endpointFactory, poolConfig);
        this.msgConverter = msgConverter;
    }

    public AccountClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, int timeout, IAccountMsgCodec msgConverter) {
        super(region, IServiceType.ACCOUNT, endpointFactory, poolConfig, timeout);
        this.msgConverter = msgConverter;
    }

    public void setMsgConverter(IAccountMsgCodec msgConverter) {
        this.msgConverter = msgConverter;
    }

    public void setAccountCache(Map<Long, AccountBO> accountCache) {
        this.accountCache = accountCache;
    }

    @Override
    public boolean isAccountNormal(long id) throws AccountServiceException {
        AccountBO accountBO = getAccountBO(id);
        return accountBO.getStatusId() == IAcctStatusId.NORMAL;
    }

    @Override
    public AccountBO getAccountBO(long id) throws AccountServiceException {
        // 1. check local cache at fist
        if (accountCache != null && accountCache.containsKey(id)) return accountCache.get(id);
        // 2. send get message to account server.
        AccountBO accountBO = null;
        IClientEngine engine = null;
        try {
            engine = borrowEngine();
            accountBO = msgConverter.getGetAccountByIdReply(Helper.request(engine, getTimeout(), msgConverter.getGetAccountByIdRequestMsg(id)));
        } catch (AccountServiceException ase) {
            throw ase;
        } catch (Exception e) {
            throw new AccountServiceException(AccountServiceException.FAILED_TO_LOAD_DATA_FROM_MSG_SERVER, e);
        } finally {
            try {
                if (null != engine) {
                    returnEngine(engine);
                }
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
        // 3. put account to local cache.
        if (accountCache != null) accountCache.put(id, accountBO);
        return accountBO;
    }
}