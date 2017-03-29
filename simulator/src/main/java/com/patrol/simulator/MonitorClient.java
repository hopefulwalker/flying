/**
 * Created by Walker.Zhang on 2015/4/5.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/5     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;

import com.flying.common.service.IEndpointFactory;
import com.flying.framework.messaging.endpoint.IEndpoint;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MonitorClient {
    private final static Logger logger = LoggerFactory.getLogger(MonitorClient.class);
    private final static String ENDPOINT_FACTORY = "endpointFactory";
    private final static String CFG_FILE = "com/patrol/simulator/acct-cfg.xml";
    private final static long NUMBER_OF_REQUEST = Long.MAX_VALUE;

    public static void main(String args[]) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(CFG_FILE);
        IEndpointFactory endpointFactory = context.getBean(ENDPOINT_FACTORY, IEndpointFactory.class);
        long startTime = System.currentTimeMillis();
        long cnt = 0;
        for (long i = 0; i < NUMBER_OF_REQUEST; i++) {
            List<IEndpoint> endpoints = endpointFactory.getEndpoints("SZ", (short) 1);
            if (endpoints != null) cnt++;
            try {
                if (i % 10000 == 0) {
                    System.out.println(BeanUtils.describe(endpoints.get(0)));
                    long totaltime = System.currentTimeMillis() - startTime;
                    System.out.println("Sent:" + i + "Received:" + cnt);
                    System.out.println("Time Elpase:" + totaltime);
                    System.out.println("throught:" + (cnt * 1000) / totaltime);
                }
            } catch (Exception ope) {
                logger.error(ope.toString(), ope);
            }
        }

        System.out.println("Time Elapse:" + (System.currentTimeMillis() - startTime));
        context.close();
    }
}
