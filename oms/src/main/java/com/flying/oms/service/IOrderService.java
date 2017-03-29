/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service;

import com.flying.common.service.ServiceException;
import com.flying.oms.model.OrderBO;

public interface IOrderService {
    public OrderBO placeOrder(OrderBO orderBO) throws OrderServiceException;
}
