/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created.
 2017/5/14     Walker           0.3.5        Created for building control center.
 */
package com.flying.monitor;

import com.flying.monitor.config.BroadcastConfig;
import com.flying.monitor.model.AbstractServerManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MonitorServerManager extends AbstractServerManager {
    private final static String BEAN_ENGINE = "monitorServer";

    @Override
    public void initServer() {
        setContext(new AnnotationConfigApplicationContext(BroadcastConfig.class));
        setServer(BEAN_ENGINE);
    }
}