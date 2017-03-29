/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service.server;

import com.flying.monitor.model.ServerBO;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.MonitorServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorServerService implements IMonitorService {
    private Map<String, ServerBO> servers = new ConcurrentHashMap<>();

    @Override
    public void register(ServerBO serverBO){
        serverBO.setReportTime(System.currentTimeMillis());
        servers.put(serverBO.getUuid(), serverBO);
    }

    @Override
    public void unregister(ServerBO serverBO) {
        servers.remove(serverBO.getUuid());
    }

    @Override
    public List<ServerBO> find(String region, short serviceType) {
        List<ServerBO> serverBOs = new ArrayList<>();
        for (ServerBO serverBO : servers.values()) {
            if (serverBO.getRegion().equals(region) && serverBO.getServiceType() == serviceType) {
                serverBOs.add(serverBO);
            }
        }
        return serverBOs;
    }

    @Override
    public List<ServerBO> find(int ttl) {
        List<ServerBO> serverBOs = new ArrayList<>();
        for (ServerBO serverBO : servers.values()) {
            if (System.currentTimeMillis() - serverBO.getReportTime() > ttl) {
                serverBOs.add(serverBO);
            }
        }
        return serverBOs;
    }

    @Override
    public int getNumServerBO() {
        return servers.size();
    }
}
