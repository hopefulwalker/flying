/*
 Created by Walker.Zhang on 2015/3/20.
 Revision History:
 Date          Who              Version      What
 2015/3/20     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.service.server;

import com.flying.monitor.model.ServerBO;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerSweeper implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServerSweeper.class);
    private IMonitorService service;
    private int serverTTL = 15000;

    public ServerSweeper(IMonitorService service) {
        this.service = service;
    }

    public void setServerTTL(int serverTTL) {
        this.serverTTL = serverTTL;
    }

    @Override
    public void run() {
        List<ServerBO> serverBOs = null;
        try {
            // find expired serverBOs.
            serverBOs = service.find(serverTTL);
            // ping server, if do not response in serverTTL milliseconds, unregister it.
            for (ServerBO serverBO : serverBOs) service.unregister(serverBO);
            System.out.println("Registered service:" + service.getNumServerBO());
        } catch (Exception ie) {
            logger.error("Exception occurs when sweeping...", ie);
        }
    }
}