/*
 Created by Walker.Zhang on 2017/6/2.
 Revision History:
 Date          Who              Version      What
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.oms.model.OrderBO;
import com.flying.util.common.Dictionary;
import com.flying.util.uk.UKGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCenter {
    private static final Logger logger = LoggerFactory.getLogger(AccountCenter.class);

    private String region;
    private IEndpointFactory endpointFactory;
    private IAccountMsgCodec msgCodec;
    private Map<Long, AccountBO> accountCache;
    private List<IEndpoint> endpoints;
    private Map<Long, Long> orderIDs;

    public AccountCenter(String region, IEndpointFactory endpointFactory, IAccountMsgCodec msgCodec) {
        this.region = region;
        this.endpointFactory = endpointFactory;
        this.msgCodec = msgCodec;
        this.orderIDs = new ConcurrentHashMap<>();
        accountCache = new ConcurrentHashMap<>();
    }

    public void putOrderID(long requestNo, long orderID) {
        orderIDs.put(requestNo, orderID);
    }

    public long removeOrderID(long requestNo) {
        return orderIDs.remove(requestNo);
    }

    public AccountBO getAccountBO(long id) {
        // 1. check local cache at fist
        if (accountCache != null && accountCache.containsKey(id)) return accountCache.get(id);
        return null;
    }

    public byte[] encodeGetAccountByIdRequest(OrderBO orderBO) {
        long requestNo = UKGeneratorFactory.getUKGenerator().generate(AccountCenter.class.getName());
        putOrderID(requestNo, orderBO.getOid());
        return msgCodec.encodeGetAccountByIdRequest(requestNo, orderBO.getAcctId());
    }

    public List<IEndpoint> getEndpoints() {
        return endpoints;
    }

    public void init() {
        List<IEndpoint> endpoints = endpointFactory.getEndpoints(region, IServiceType.ACCOUNT);
        if ((endpoints == null) || endpoints.size() <= 0) {
            logger.warn("Could not get endpoints:" + getInfo());
        }
        this.endpoints = endpoints;
    }

    private String getInfo() {
        return "Region:" + region + "Service Type:" + Dictionary.getString(IServiceType.class, IServiceType.ACCOUNT);
    }
}