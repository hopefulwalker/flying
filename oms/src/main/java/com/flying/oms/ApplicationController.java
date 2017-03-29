/*
 Created by Walker.Zhang on 2015/2/25.
 Revision History:
 Date          Who              Version      What
 2015/2/25     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.oms;

import com.flying.monitor.model.IServer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationController {
    //    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final static String BEAN_ENGINE = "orderManageServer";

    private IServer server = null;
    private AbstractApplicationContext appCtx = null;
    private String cfgFileName = null;

    public ApplicationController(String cfgFileName) {
        this.cfgFileName = cfgFileName;
    }

    public void start() {
        if (appCtx == null) appCtx = new ClassPathXmlApplicationContext(cfgFileName);
        if (server == null) server = appCtx.getBean(BEAN_ENGINE, IServer.class);
        server.start();
    }

    public void stop() {
        if (server != null) server.stop();
    }

    public void exit() {
        if (server != null) server.stop();
        if (appCtx != null) appCtx.close();
    }
}
