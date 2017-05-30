/*
 Created by Walker.Zhang on 2015/3/16.
 Revision History:
 Date          Who              Version      What
 2015/3/16     Walker.Zhang     0.1.0        Created. 
 */
package com.patrol.simulator;

import com.flying.common.IReturnCode;
import com.flying.common.model.IExchId;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.oms.model.IBsSideId;
import com.flying.oms.model.OrderBO;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.service.client.OrderClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class AsyncOrderClient {
    private final static Logger logger = LoggerFactory.getLogger(AsyncOrderClient.class);
    private final static String ENDPOINT_FACTORY = "endpointFactory";
    private final static String ORDER_SERVICE = "orderService";
    private final static String CFG_FILE = "com/patrol/simulator/order-cfg.xml";
    private final static long NUMBER_OF_REQUEST = Long.MAX_VALUE;

    public static void main(String args[]) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(CFG_FILE);
        IEndpointFactory endpointFactory = context.getBean(ENDPOINT_FACTORY, IEndpointFactory.class);
        List<IEndpoint> endpoints = endpointFactory.getEndpoints("SZ", IServiceType.ORDER);
        OrderClientService orderClientService = context.getBean(ORDER_SERVICE, OrderClientService.class);

        double sum = 0;
        double min = Long.MAX_VALUE;
        double max = Long.MIN_VALUE;

        HashMap<Long, Long> requests = new HashMap<>();
        long rvn = 0;
        OrderBO orderBO;
        long responseTime;
        long startTime = System.currentTimeMillis();
        for (long i = 0; i < NUMBER_OF_REQUEST || requests.size() > 0; ) {
            if ((i < NUMBER_OF_REQUEST) && (i - rvn < 10)) {
                try {
                    orderBO = new OrderBO();
                    orderBO.setExtNo(i);
                    orderBO.setAcctId(10000);
                    orderBO.setExchId(IExchId.SHANGHAI);
                    orderBO.setSectCode("600001");
                    orderBO.setBsSideId(IBsSideId.BUY);
                    orderBO.setPrice(11.1);
                    orderBO.setQty(i);
                    orderClientService.sendOrderRequest(orderBO);
                    requests.put(i, System.currentTimeMillis());
                    i++;
                } catch (OrderServiceException ope) {
                    logger.error(ope.toString());
                }
            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            try {
                if (rvn < i) {
                    orderBO = orderClientService.recvOrderReply(1);
                    if (orderBO != null) {
                        responseTime = System.currentTimeMillis() - requests.get(orderBO.getExtNo());
                        requests.remove(orderBO.getExtNo());
                        if (responseTime < min) min = responseTime;
                        if (responseTime > max) max = responseTime;
                        sum = responseTime + sum;
                        rvn++;
                    }
                }
            } catch (OrderServiceException ope) {
                if (ope.getCause() instanceof ServiceException) {
                    if (((ServiceException) ope.getCause()).getCode() != IReturnCode.TIMEOUT) {
                        logger.error(ope.toString());
                    }
                }
            }
            try {
                if (rvn % 100000 == 1) {
                    long totaltime = (System.currentTimeMillis() - startTime);
                    System.out.println("send:" + i);
                    System.out.println("rvn:" + rvn);
                    System.out.println("Time Elpase:" + totaltime);
                    System.out.println("min:" + min);
                    System.out.println("max:" + max);
                    System.out.println("avg:" + sum / rvn);
                    System.out.println("throughput:" + (rvn * 1000) / totaltime);
                }
            } catch (Exception e) {
                logger.info("exception", e);
            }
        }
        long totaltime = (System.currentTimeMillis() - startTime);
        System.out.println("rvn:" + rvn);
        System.out.println("Time Elpase:" + totaltime);
        System.out.println("throught:" + (rvn * 1000) / totaltime);
        context.close();
    }
}
