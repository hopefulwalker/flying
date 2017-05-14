/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5      Walker.Zhang     0.1.0        Created.
 2017/5/14     Walker.Zhang     0.3.5        Created for building control center.
 */
package com.flying.oms;

import com.flying.monitor.model.AbstractServerManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderServerManager extends AbstractServerManager {
    private final static String BEAN_ENGINE = "orderManageServer";
    private final static String CFG_FILE_NAME = "com/flying/oms/oms-config.xml";

    @Override
    public void initServer() {
        setContext(new ClassPathXmlApplicationContext(CFG_FILE_NAME));
        setServer(BEAN_ENGINE);
    }
}
