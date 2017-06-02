/*
 Created by Walker.Zhang on 2015/2/18.
 Revision History:
 Date          Who              Version      What
2015/2/18     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.msg.handler;

import com.flying.common.IReturnCode;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.msg.gen.ServerQueryRequestDecoder;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerQueryRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerQueryRequestHandler.class);
    private IMonitorService service;
    private IMonitorMsgCodec msgCodec;

    public ServerQueryRequestHandler(IMonitorService service, IMonitorMsgCodec msgCodec) {
        this.service = service;
        this.msgCodec = msgCodec;
    }

    public byte[] handle(byte[] msg) {
        ServerQueryRequestDecoder request = msgCodec.getServerQueryRequestDecoder(msg);
        int retCode = IReturnCode.SUCCESS;
        List<ServerBO> serverBOs = null;
        try {
            serverBOs = service.find(request.region(), request.serviceType());
        } catch (ServiceException se) {
            logger.error("Error in finding serverBO", se);
            retCode = se.getCode();
        }
        return msgCodec.encodeServerQueryReply(retCode, serverBOs);
    }

    @Override
    public IMsgEventResult handle(IMsgEvent event) {
        ServerQueryRequestDecoder request = msgCodec.getServerQueryRequestDecoder(event.getInfo().getBytes());
        int retCode = IReturnCode.SUCCESS;
        List<ServerBO> serverBOs = null;
        try {
            serverBOs = service.find(request.region(), request.serviceType());
        } catch (ServiceException se) {
            logger.error("Error in finding serverBO", se);
            retCode = se.getCode();
        }
        return new MsgEventResult(event.getInfo().getFroms(), msgCodec.encodeServerQueryReply(retCode, serverBOs));
    }
}