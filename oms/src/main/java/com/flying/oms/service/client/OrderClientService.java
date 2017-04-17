/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service.client;

import com.flying.common.msg.codec.Helper;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.client.BaseUCClientService;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.codec.IOrderMsgCodec;
import com.flying.oms.service.IOrderService;
import com.flying.oms.service.OrderServiceException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderClientService extends BaseUCClientService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderClientService.class);
    private IOrderMsgCodec msgConverter;

    public OrderClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, IOrderMsgCodec msgConverter) {
        super(region, IServiceType.ORDER, endpointFactory, poolConfig);
        this.msgConverter = msgConverter;
    }

    public OrderClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, int timeout, IOrderMsgCodec msgConverter) {
        super(region, IServiceType.ORDER, endpointFactory, poolConfig, timeout);
        this.msgConverter = msgConverter;
    }

    @Override
    public OrderBO placeOrder(OrderBO orderBO) throws OrderServiceException {
        IClientEngine engine = null;
        // send register message to monitor server.
        OrderBO replyOrder;
        try {
            engine = borrowEngine();
            replyOrder = msgConverter.getOrderReply(Helper.request(engine, getTimeout(), msgConverter.getOrderRequestMsg(orderBO)));
        } catch (OrderServiceException ose) {
            throw ose;
        } catch (Exception e) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_PLACE_ORDER, e);
        } finally {
            try {
                if (null != engine) returnEngine(engine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
        return replyOrder;
    }

    public void sendOrderRequest(OrderBO orderBO) throws OrderServiceException {
        IClientEngine engine = null;
        // send register message to monitor server.
        OrderBO replyOrder;
        try {
            engine = borrowEngine();
            Helper.sendMsg(engine, msgConverter.getOrderRequestMsg(orderBO));
        } catch (OrderServiceException ose) {
            throw ose;
        } catch (Exception e) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_SEND_ORDER_REQUEST, e);
        } finally {
            try {
                if (null != engine) returnEngine(engine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
    }

    public OrderBO recvOrderReply() throws OrderServiceException {
        return recvOrderReply(getTimeout());
    }

    public OrderBO recvOrderReply(int timeout) throws OrderServiceException {
        IClientEngine engine = null;
        // send register message to monitor server.
        OrderBO replyOrder = null;
        try {
            engine = borrowEngine();
            return msgConverter.getOrderReply(Helper.recvMsg(engine, timeout));
        } catch (OrderServiceException ose) {
            throw ose;
        } catch (Exception e) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_RECV_ORDER_REPLY, e);
        } finally {
            try {
                if (null != engine) returnEngine(engine);
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
    }
}