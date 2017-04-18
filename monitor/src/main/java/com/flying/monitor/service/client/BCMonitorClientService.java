/**
 * Created by Walker.Zhang on 2015/5/18.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/18     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service.client;

import com.flying.common.msg.codec.Helper;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.MonitorServiceException;

import java.util.List;

public class BCMonitorClientService implements IMonitorService {
    private static final int DEFAULT_TIMEOUT = 1000;
    private IClientEngine engine;
    private IMonitorMsgCodec msgCodec;

    public BCMonitorClientService(IClientEngine engine, IMonitorMsgCodec msgCodec) {
        this.engine = engine;
        this.msgCodec = msgCodec;
    }

    @Override
    public void register(ServerBO serverBO) throws MonitorServiceException {
        byte[] requestBytes = msgCodec.encodeServerRegistryRequest(serverBO);
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(requestBytes));
        engine.sendMsg(requestEvent);
    }

    @Override
    public void unregister(ServerBO serverBO) throws MonitorServiceException {
        throw new MonitorServiceException(MonitorServiceException.UNSUPPORTED_SERVICE, "method don't implement");
    }

    @Override
    public List<ServerBO> find(String region, short type) throws MonitorServiceException {
        byte[] requestBytes = msgCodec.encodeServerQueryRequest(region, type);
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(requestBytes));
        engine.sendMsg(requestEvent);
        IMsgEvent replyEvent = engine.recvMsg(DEFAULT_TIMEOUT);
        // we need to analyze the replyClass event here.
        byte[] replyBytes;
        try {
            replyBytes = Helper.buildReplyBytes(engine, DEFAULT_TIMEOUT, replyEvent);
        } catch (ServiceException se) {
            throw new MonitorServiceException(se.getCode(), se.getMessage());
        }
        return msgCodec.getServerQueryReply(replyBytes);
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