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
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.converter.IMonitorMsgConverter;
import com.flying.monitor.msg.gen.ServerQueryRequest;
import com.flying.monitor.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServerQueryRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerQueryRequestHandler.class);
    private IMonitorService service;
    private IMonitorMsgConverter msgConverter;

    public ServerQueryRequestHandler(IMonitorService service, IMonitorMsgConverter msgConverter) {
        this.service = service;
        this.msgConverter = msgConverter;
    }

    public byte[] handle(byte[] msg) {
        ServerQueryRequest request = msgConverter.getServerQueryRequest(msg);
        int retCode = IReturnCode.SUCCESS;
        List<ServerBO> serverBOs = null;
        try {
            serverBOs = service.find(request.getRegion(), request.serviceType());
        } catch (UnsupportedEncodingException uee) {
            logger.error("Error in finding serverBO", uee);
            retCode = IReturnCode.UNSUPPORTED_ENCODING;
        } catch (ServiceException se) {
            logger.error("Error in finding serverBO", se);
            retCode = se.getCode();
        }
        return msgConverter.getServerQueryReplyMsg(retCode, serverBOs);
    }
}