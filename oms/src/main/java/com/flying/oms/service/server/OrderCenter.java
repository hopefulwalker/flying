/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service.server;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.codec.IOrderMsgCodec;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderCenter {
    private Map<Long, OrderBO> orderCache;
    private Map<Long, List<IEndpoint>> orderFroms;
    private IOrderMsgCodec msgCodec;

    public OrderCenter(IOrderMsgCodec msgCodec) {
        this.orderCache = new ConcurrentHashMap<>();
        this.orderFroms = new ConcurrentHashMap<>();
        this.msgCodec = msgCodec;
    }

    public List<IEndpoint> getOrderFroms(long oid) {
        return orderFroms.get(oid);
    }

    public void clearOrderBO(long oid) {
        orderCache.remove(oid);
        orderFroms.remove(oid);
    }

    public void saveOrderBO(List<IEndpoint> froms, OrderBO orderBO) {
        orderCache.put(orderBO.getOid(), orderBO);
        orderFroms.put(orderBO.getOid(), froms);
    }

    public OrderBO getOrderBO(long oid) {
        return orderCache.get(oid);
    }

    public byte[] encodeOrderReply(int retCode, OrderBO orderBO) {
        return msgCodec.encodeOrderReply(retCode, orderBO);
    }

    public byte[] encodeOrderReply(int retCode) {
        return msgCodec.encodeOrderReply(retCode);
    }
}
