/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5      Walker.Zhang     0.1.0        Created.
 2017/5/13     Walker.Zhang     0.3.5        Change dependencies from IServer to ServerBO.
*/
package com.flying.monitor.service.client;

import com.flying.monitor.model.ServerBO;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerReporter implements IServerReporter, Runnable {
    // logger
    private static final Logger logger = LoggerFactory.getLogger(ServerReporter.class);
    // report server
    private ServerBO serverBO;
    private IMonitorService service;

    public ServerReporter(IMonitorService service, ServerBO serverBO) {
        this.service = service;
        this.serverBO = serverBO;
    }

    @Override
    public void report() {
        try {
            service.register(serverBO);
        } catch (Exception e) {
            logger.info(e.toString(), e);
        }
    }

    @Override
    public void run() {
        report();
    }
}