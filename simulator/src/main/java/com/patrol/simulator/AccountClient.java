/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;

import com.flying.ams.model.AccountBO;
import com.flying.ams.service.client.AccountClientService;
import com.flying.common.service.IEndpointFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountClient {
    private final static Logger logger = LoggerFactory.getLogger(AccountClient.class);
    private final static String ENDPOINT_FACTORY = "endpointFactory";
    private final static String ACCOUNT_SERVICE = "accountService";
    private final static String CFG_FILE = "com/patrol/simulator/acct-cfg.xml";
    private final static long NUMBER_OF_REQUEST = Long.MAX_VALUE;

    public static void main(String args[]) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(CFG_FILE);
        IEndpointFactory endpointFactory = context.getBean(ENDPOINT_FACTORY, IEndpointFactory.class);
        AccountClientService accountClientService = context.getBean(ACCOUNT_SERVICE, AccountClientService.class);

        long startTime = System.currentTimeMillis();
        AccountBO accountBO;
        for (long i = 0; i < NUMBER_OF_REQUEST; i++) {
            try {
                accountBO = accountClientService.getAccountBO(10000L);
                if (i % 10000000 == 0) System.out.println(BeanUtils.describe(accountBO));
            } catch (Exception ope) {
                logger.error(ope.toString(), ope);
            }
        }
        System.out.println("Time Elapse:" + (System.currentTimeMillis() - startTime));
        context.close();
    }
}
