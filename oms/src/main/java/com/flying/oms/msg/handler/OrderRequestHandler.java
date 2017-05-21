/*
 File: OrderRequestHandler.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2014.12.21   walker  0.1.0    Create this file.
 */
package com.flying.oms.msg.handler;

import com.flying.common.IReturnCode;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderState;
import com.flying.oms.msg.codec.IOrderMsgCodec;
import com.flying.oms.msg.gen.OrderRequestDecoder;
import com.flying.oms.service.IOrderService;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.service.server.OrderServerService;
import com.flying.util.math.IntegerUtils;
import com.flying.util.uk.UKGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


public class OrderRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRequestHandler.class);
    private IOrderService service;
    private IOrderMsgCodec msgCodec;

    public OrderRequestHandler(IOrderService service, IOrderMsgCodec msgCodec) {
        this.service = service;
        this.msgCodec = msgCodec;
    }

    @Override
    public byte[] handle(byte[] msg) {
        OrderRequestDecoder requestDecoder = msgCodec.getOrderRequestDecoder(msg);
        int retCode = IReturnCode.SUCCESS;
        OrderBO orderBO = null;
        try {
            orderBO = buildOrderBO(requestDecoder);
            orderBO = service.placeOrder(orderBO);
        } catch (OrderServiceException ose) {
            logger.error("Error in placing order", ose);
            retCode = ose.getCode();
        }
        if (retCode == IReturnCode.SUCCESS) retCode = orderBO.getStateEnteredCode();
        // end of to do
        return msgCodec.encodeOrderReply(retCode, orderBO);
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
        orderBO.setStateId(OrderState.CREATED);
        //contract number should be set by the state machine.
        return orderBO;
    }
}