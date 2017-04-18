/*
 Created by Walker.Zhang on 2015/2/18.
 Revision History:
 Date          Who              Version      What
2015/2/18     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.msg.handler;

import com.flying.common.msg.handler.IMsgHandler;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.msg.gen.ServerRegistryRequestDecoder;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.MonitorServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRegistryRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerRegistryRequestHandler.class);
    private IMonitorService service;
    private IMonitorMsgCodec msgCodec;

    public ServerRegistryRequestHandler(IMonitorService service, IMonitorMsgCodec msgCodec) {
        this.service = service;
        this.msgCodec = msgCodec;
    }

    public byte[] handle(byte[] msg) {
        try {
            ServerRegistryRequestDecoder request = msgCodec.getServerRegistryRequestDecoder(msg);
            ServerBO serviceBO = new ServerBO(request.uuid(), request.region(),
                    request.serviceType(), request.name(), request.endpoint(),
                    request.workers(), request.stateId(), request.reportTime());
            service.register(serviceBO);
        } catch (MonitorServiceException mse) {
            logger.error(mse.toString(), mse);
        }
        return null;
    }
}