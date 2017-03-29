/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.service.client;

import com.flying.monitor.model.IServer;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerReporter implements IServerReporter, Runnable {
    // logger
    private static final Logger logger = LoggerFactory.getLogger(ServerReporter.class);
    // report server
    private IServer server;
    private IMonitorService service;

    public void setService(IMonitorService service) {
        this.service = service;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    @Override
    public void report() {
        try {
            service.register(server.getServerBO());
        } catch (Exception e) {
            logger.info(e.toString(), e);
        }
    }

    @Override
    public void run() {
        report();
    }
}