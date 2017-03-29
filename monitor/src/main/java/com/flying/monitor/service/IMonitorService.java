/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service;

import com.flying.monitor.model.ServerBO;

import java.util.List;

public interface IMonitorService {
    public void register(ServerBO serverBO) throws MonitorServiceException;

    public void unregister(ServerBO serverBO) throws MonitorServiceException;

    public List<ServerBO> find(String region, short type) throws MonitorServiceException;

    public List<ServerBO> find(int ttl) throws MonitorServiceException;

    public int getNumServerBO() throws MonitorServiceException;
}
