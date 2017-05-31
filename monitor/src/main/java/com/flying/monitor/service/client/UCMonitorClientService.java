/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service.client;

import com.flying.common.msg.codec.Helper;
import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.client.BaseUCClientService;
import com.flying.framework.messaging.engine.IClientCommEngine;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.MonitorServiceException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UCMonitorClientService extends BaseUCClientService implements IMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(UCMonitorClientService.class);
    private IMonitorMsgCodec msgCodec = null;

    public UCMonitorClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, IMonitorMsgCodec msgCodec) {
        super(region, IServiceType.MONITOR, endpointFactory, poolConfig);
        this.msgCodec = msgCodec;
    }

    public UCMonitorClientService(String region, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, int timeout, IMonitorMsgCodec msgCodec) {
        super(region, IServiceType.MONITOR, endpointFactory, poolConfig, timeout);
        this.msgCodec = msgCodec;
    }

    @Override
    public void register(ServerBO serverBO) throws MonitorServiceException {
        IClientCommEngine engine = null;
        // send register message to monitor server.
        try {
            engine = borrowEngine();
            Helper.sendMsg(engine, msgCodec.encodeServerRegistryRequest(serverBO));
        } catch (MonitorServiceException mse) {
            throw mse;
        } catch (Exception e) {
            throw new MonitorServiceException(MonitorServiceException.FAILED_TO_REGISTER, e);
        } finally {
            try {
                if (null != engine) {
                    returnEngine(engine);
                }
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
    }

    @Override
    public void unregister(ServerBO serverBO) throws MonitorServiceException {
        throw new MonitorServiceException(MonitorServiceException.UNSUPPORTED_SERVICE, "method don't implement");
    }

    @Override
    public List<ServerBO> find(String region, short type) throws MonitorServiceException {
        List<ServerBO> serverBOs = null;
        IClientCommEngine engine = null;
        // send find message to account server.
        try {
            engine = borrowEngine();
            serverBOs = msgCodec.getServerQueryReply(Helper.request(engine, getTimeout(), msgCodec.encodeServerQueryRequest(region, type)));
        } catch (MonitorServiceException mse) {
            throw mse;
        } catch (Exception e) {
            throw new MonitorServiceException(MonitorServiceException.FAILED_TO_LOAD_DATA_FROM_MSG_SERVER, e);
        } finally {
            try {
                if (null != engine) {
                    returnEngine(engine);
                }
            } catch (Exception e) {
                logger.error("Failed to release object to pool!", e);
            }
        }
        return serverBOs;
    }

    @Override
    public List<ServerBO> find(int ttl) throws MonitorServiceException {
        throw new MonitorServiceException(MonitorServiceException.UNSUPPORTED_SERVICE, "method don't implement");
    }

    @Override
    public int getNumServerBO() throws MonitorServiceException {
        throw new MonitorServiceException(MonitorServiceException.UNSUPPORTED_SERVICE, "method don't implement");
    }
}