/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5      Walker.Zhang     0.1.0        Created.
 2017/5/14     Walker           0.3.5        Refactor for building control center.
*/
package com.flying.ams;

import com.flying.monitor.model.AbstractServerManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountServerManager extends AbstractServerManager {
    private final static String BEAN_ENGINE = "accountManageServer";
    private final static String CFG_FILE_NAME = "com/flying/ams/ams-config.xml";

    @Override
    public void initServer() {
        setContext(new ClassPathXmlApplicationContext(CFG_FILE_NAME));
        setServer(BEAN_ENGINE);
    }
}