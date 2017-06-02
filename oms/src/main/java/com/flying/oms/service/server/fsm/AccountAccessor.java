/*
 Created by Walker.Zhang on 2017/6/2.
 Revision History:
 Date          Who              Version      What
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.util.common.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AccountAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AccountAccessor.class);

    private String region;
    private IEndpointFactory endpointFactory;
    private IAccountMsgCodec msgCodec;
    private Map<Long, AccountBO> accountCache = null;

    public AccountAccessor(String region, IEndpointFactory endpointFactory, IAccountMsgCodec msgCodec) {
        this.region = region;
        this.endpointFactory = endpointFactory;
        this.msgCodec = msgCodec;
    }

    public void setMsgCodec(IAccountMsgCodec msgCodec) {
        this.msgCodec = msgCodec;
    }

    public void setAccountCache(Map<Long, AccountBO> accountCache) {
        this.accountCache = accountCache;
    }

    public AccountBO getAccountBO(long id) {
        // 1. check local cache at fist
        if (accountCache != null && accountCache.containsKey(id)) return accountCache.get(id);
        return null;
    }

    public void setAccountBO(AccountBO accountBO) {
        // 1. check local cache at fist
        if (accountCache != null) accountCache.put(accountBO.getAid(), accountBO);
    }

    public byte[] getAccountBORequest(long id) {
        return msgCodec.encodeGetAccountByIdRequest(id);
    }

    public void init() {
        List<IEndpoint> endpoints = endpointFactory.getEndpoints(region, IServiceType.ACCOUNT);
        if ((endpoints == null) || endpoints.size() <= 0) {
            logger.warn("Could not get endpoints:" + getInfo());
        }
    }

    protected String getInfo() {
        return "Region:" + region + "Service Type:" + Dictionary.getString(IServiceType.class, IServiceType.ACCOUNT);
    }
}