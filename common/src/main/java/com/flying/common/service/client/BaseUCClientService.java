/**
 * Created by Walker.Zhang on 2015/5/30.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/30     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.service.client;

import com.flying.common.service.IEndpointFactory;
import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.engine.impl.zmq.PooledZMQClientEngineFactory;
import com.flying.util.common.Dictionary;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseUCClientService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BaseUCClientService.class);
    private static final int DEFAULT_TIMEOUT = 1500;
    private String region;
    private short serviceType;
    private int timeout;

    private IEndpointFactory endpointFactory;
    private GenericObjectPoolConfig poolConfig;
    private PooledZMQClientEngineFactory poolFactory;
    private ObjectPool<IClientEngine> enginePool;

    public BaseUCClientService(String region, short serviceType, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig) {
        this(region, serviceType, endpointFactory, poolConfig, DEFAULT_TIMEOUT);
    }

    public BaseUCClientService(String region, short serviceType, IEndpointFactory endpointFactory, GenericObjectPoolConfig poolConfig, int timeout) {
        this.region = region;
        this.serviceType = serviceType;
        this.endpointFactory = endpointFactory;
        this.poolConfig = poolConfig;
        this.timeout = timeout;
        this.poolFactory = null;
        this.enginePool = null;
    }

    public void init() {
        List<IEndpoint> endpoints = endpointFactory.getEndpoints(region, serviceType);
        if ((endpoints == null) || endpoints.size() <= 0) {
            logger.warn("Could not get endpoints:" + getInfo());
            return;
        }
        this.poolFactory = new PooledZMQClientEngineFactory(endpoints);
        this.enginePool = new GenericObjectPool<IClientEngine>(poolFactory, poolConfig);
        try {
            PoolUtils.prefill(enginePool, poolConfig.getMinIdle());
            //Wait timeout millis for the client engine to finish connect.
            Thread.sleep(this.timeout);
        } catch (Exception e) {
            logger.error("FAILED_TO_INITIALIZE_SERVICE" + getInfo(), e);
        }
    }

    @Override
    public void run() {
        try {
            if (poolFactory == null && enginePool == null)
                init();
            else {
                List<IEndpoint> endpoints = endpointFactory.getEndpoints(region, serviceType);
                if ((endpoints == null) || endpoints.size() <= 0)
                    logger.warn("Could not get endpoints:" + getInfo());
                else
                    poolFactory.setEndpoints(endpoints);
            }
        } catch (Exception e) {
            logger.error("Error occurs when refreshing endpoints:" + getInfo(), e);
        }
    }

    public IClientEngine borrowEngine() throws Exception {
        if (enginePool != null)
            return enginePool.borrowObject();
        else
            throw new ServiceException(ServiceException.SERVICE_NOT_READY, getInfo());
    }

    public void returnEngine(IClientEngine engine) throws Exception {
        enginePool.returnObject(engine);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void close() {
        this.poolFactory = null;
        this.enginePool.close();
    }

    protected String getInfo() {
        return "Region:" + region + "Service Type:" + Dictionary.getString(IServiceType.class, serviceType);
    }
}