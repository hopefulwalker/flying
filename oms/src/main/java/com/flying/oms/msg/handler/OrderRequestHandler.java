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
import com.flying.oms.msg.converter.IOrderMsgCodec;
import com.flying.oms.msg.gen.OrderRequest;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.service.server.OrderServerService;
import com.flying.util.math.IntegerUtils;
import com.flying.util.uk.UKGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


public class OrderRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRequestHandler.class);
    private OrderServerService service;
    private IOrderMsgCodec msgConverter;

    public OrderRequestHandler(OrderServerService service, IOrderMsgCodec msgConverter) {
        this.service = service;
        this.msgConverter = msgConverter;
    }

    @Override
    public byte[] handle(byte[] msg) {
        OrderRequest request = msgConverter.getOrderRequest(msg);
        int retCode = IReturnCode.SUCCESS;
        OrderBO orderBO = null;
        try {
            orderBO = buildOrderBO(request);
            orderBO = service.placeOrder(orderBO);
        } catch (UnsupportedEncodingException uee) {
            logger.error("Error in handling requestClass", uee);
            retCode = IReturnCode.UNSUPPORTED_ENCODING;
        } catch (OrderServiceException ose) {
            logger.error("Error in placing order", ose);
            retCode = ose.getCode();
        }
        if (retCode == IReturnCode.SUCCESS) retCode = orderBO.getStateEnteredCode();
        // end of to do
        return msgConverter.getOrderReplyMsg(retCode, orderBO);
    }

    private OrderBO buildOrderBO(OrderRequest request) throws UnsupportedEncodingException {
        OrderBO orderBO = new OrderBO();
        // from request.
        orderBO.setExtNo(request.extNo());
        orderBO.setAcctId(request.acctId());
        orderBO.setExchId(request.exchId());
        orderBO.setSectCode(request.getSectCode());
        orderBO.setBsSideId(request.bsSideId());
        orderBO.setPrice(request.price());
        orderBO.setQty(request.qty());
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