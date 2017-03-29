/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service.client;

import com.flying.common.service.IEndpointFactory;
import com.flying.framework.messaging.endpoint.Endpoint;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MonitorEndpointFactory implements IEndpointFactory {
    private static Logger logger = LoggerFactory.getLogger(MonitorEndpointFactory.class);
    private IMonitorService service;

    public MonitorEndpointFactory(IMonitorService service) {
        this.service = service;
    }

    public List<IEndpoint> getEndpoints(String region, short type) {
        List<IEndpoint> endpoints = null;
        try {
            List<ServerBO> serverBOs = service.find(region, type);
            if (serverBOs != null) {
                endpoints = new ArrayList<>();
                for (ServerBO serverBO : serverBOs) endpoints.add(new Endpoint(serverBO.getEndpoint()));
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return endpoints;
    }
}