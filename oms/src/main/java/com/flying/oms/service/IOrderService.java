/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.oms.model.OrderBO;

import java.util.List;

public interface IOrderService {
    OrderBO placeOrder(List<IEndpoint> froms, OrderBO orderBO) throws OrderServiceException;
}
