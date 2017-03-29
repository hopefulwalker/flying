/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;

import com.flying.common.model.IExchId;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.oms.model.IBsSideId;
import com.flying.oms.model.OrderBO;
import com.flying.oms.service.client.OrderClientService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class OrderClient {
    private final static Logger logger = LoggerFactory.getLogger(OrderClient.class);
    private final static String ENDPOINT_FACTORY = "endpointFactory";
    private final static String ORDER_SERVICE = "orderService";
    private final static String CFG_FILE = "com/patrol/simulator/order-cfg.xml";
    private final static long NUMBER_OF_REQUEST = Long.MAX_VALUE;

    public static void main(String args[]) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(CFG_FILE);
        IEndpointFactory endpointFactory = context.getBean(ENDPOINT_FACTORY, IEndpointFactory.class);
        List<IEndpoint> endpoints = endpointFactory.getEndpoints("SZ", IServiceType.ORDER);
        OrderClientService orderClientService = context.getBean(ORDER_SERVICE, OrderClientService.class);

        long startTime = System.currentTimeMillis();
        OrderBO orderBO;
        long rvn = 0;
        for (long i = 0; i < NUMBER_OF_REQUEST; i++) {
            try {
//                long start= System.currentTimeMillis();
                orderBO = new OrderBO();
                orderBO.setExtNo(i);
                orderBO.setAcctId(10000);
                orderBO.setExchId(IExchId.SHANGHAI);
                orderBO.setSectCode("600001");
                orderBO.setBsSideId(IBsSideId.BUY);
                orderBO.setPrice(11.1);
                orderBO.setQty(i);
                if (i % 25000 == 0) System.out.println("Before:" + BeanUtils.describe(orderBO));
                orderBO = orderClientService.placeOrder(orderBO);
//                System.out.println(System.currentTimeMillis() - start);
                rvn++;
                if (i % 25000 == 0) System.out.println("After:" + BeanUtils.describe(orderBO));
                if (i % 25000 == 0) {
                    long totaltime = (System.currentTimeMillis() - startTime);
                    System.out.println("rvn:" + rvn);
                    System.out.println("Time Elpase:" + totaltime);
                    System.out.println("throught:" + (rvn * 1000) / totaltime);
                }
            } catch (Exception ope) {
                logger.error(ope.toString(), ope);
            }
        }
        long totaltime = (System.currentTimeMillis() - startTime);
        System.out.println("rvn:" + rvn);
        System.out.println("Fuck Time Elpase:" + totaltime);
        System.out.println("throught:" + (rvn * 1000) / totaltime);
        context.close();
    }
}