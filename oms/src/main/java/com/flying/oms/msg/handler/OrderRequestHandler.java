/*
 File: OrderRequestHandler.java
 Originally written by Walker.
 Rivision History:
 Date          Who              Version      What
 2014.12.21    walker           0.1.0        Create this file.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
 */
package com.flying.oms.msg.handler;

import com.flying.common.msg.handler.IMsgHandler;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderStates;
import com.flying.oms.msg.codec.IOrderMsgCodec;
import com.flying.oms.msg.gen.OrderRequestDecoder;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.service.server.OrderCenter;
import com.flying.oms.service.server.OrderServerService;
import com.flying.util.math.IntegerUtils;
import com.flying.util.uk.UKGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRequestHandler.class);
    private OrderServerService serverService;
    private IOrderMsgCodec msgCodec;

    public OrderRequestHandler(OrderServerService serverService, IOrderMsgCodec msgCodec) {
        this.serverService = serverService;
        this.msgCodec = msgCodec;
    }

    @Override
    public IMsgEventResult handle(IMsgEvent event) {
        IMsgEventResult result;
        OrderRequestDecoder requestDecoder = msgCodec.getOrderRequestDecoder(event.getInfo().getBytes());
        try {
            OrderBO orderBO = buildOrderBO(requestDecoder);
            result = serverService.placeOrder(event.getInfo().getFroms(), orderBO);
        } catch (OrderServiceException ose) {
            logger.error("Error in placing order", ose);
            result = new MsgEventResult(event.getInfo().getFroms(), msgCodec.encodeOrderReply(ose.getCode()));
        }
        return result;
    }

    private OrderBO buildOrderBO(OrderRequestDecoder requestDecoder) {
        OrderBO orderBO = new OrderBO();
        // from request.
        orderBO.setExtNo(requestDecoder.extNo());
        orderBO.setAcctId(requestDecoder.acctId());
        orderBO.setExchId(requestDecoder.exchId());
        orderBO.setSectCode(requestDecoder.sectCode());
        orderBO.setBsSideId(requestDecoder.bsSideId());
        orderBO.setPrice(requestDecoder.price());
        orderBO.setQty(requestDecoder.qty());
        // set by handlers;
        orderBO.setOid(UKGeneratorFactory.getUKGenerator().generate(OrderBO.class.getName()));
        orderBO.setBizDate(IntegerUtils.getDate(System.currentTimeMillis()));
        long currentTime = System.currentTimeMillis();
        orderBO.setCreateTime(currentTime);
        orderBO.setUpdateTime(currentTime);
        orderBO.setState(OrderStates.CREATED);
        //contract number should be set by the state machine.
        return orderBO;
    }
}