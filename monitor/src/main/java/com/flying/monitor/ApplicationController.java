/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor;

import com.flying.monitor.model.IServer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationController {
    private final static String BEAN_ENGINE = "monitorServer";

    private IServer server = null;
    private AbstractApplicationContext appCtx = null;

    public ApplicationController(String cfgFileName) {
        this.appCtx = new ClassPathXmlApplicationContext(cfgFileName);
        this.server = appCtx.getBean(BEAN_ENGINE, IServer.class);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }

    public void exit() {
        server.stop();
        appCtx.close();
    }
}
