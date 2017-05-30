/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5      Walker.Zhang     0.1.0        Created.
 2017/5/14     Walker.Zhang     0.3.5        Created for building control center.
 */
package com.flying.oms;

import com.flying.monitor.model.AbstractServerManager;
import com.flying.oms.config.OmsConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderServerManager extends AbstractServerManager {
    private final static String BEAN_ENGINE = "orderServer";

    @Override
    public void initServer() {
        setContext(new AnnotationConfigApplicationContext(OmsConfig.class));
        setServer(BEAN_ENGINE);
    }
}
