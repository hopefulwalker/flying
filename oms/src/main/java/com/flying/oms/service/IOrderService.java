/*
 Created by Walker.Zhang on 2015/4/6.
 Revision History:
 Date          Who              Version      What
 2015/4/6      Walker.Zhang     0.1.0        Created.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.service;

import com.flying.oms.model.OrderBO;

public interface IOrderService {
    OrderBO placeOrder(OrderBO orderBO) throws OrderServiceException;
}
