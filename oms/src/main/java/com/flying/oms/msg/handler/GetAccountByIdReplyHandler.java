/*
 Created by Walker on 2017/6/3.
 Revision History:
 Date          Who              Version      What
 2017/6/3      Walker           0.1.0        Created.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.msg.handler;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.ams.msg.gen.GetAccountByIdReplyDecoder;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.oms.service.server.OrderCenter;
import com.flying.oms.service.server.OrderServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAccountByIdReplyHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAccountByIdReplyHandler.class);
    private OrderServerService serverService;
    private IAccountMsgCodec msgCodec;

    public GetAccountByIdReplyHandler(OrderServerService serverService, IAccountMsgCodec msgCodec) {
        this.serverService = serverService;
        this.msgCodec = msgCodec;
    }

    @Override
    public IMsgEventResult handle(IMsgEvent event) {
        AccountBO accountBO = msgCodec.getGetAccountByIdReply(event.getInfo().getBytes());
        GetAccountByIdReplyDecoder decoder = msgCodec.getGetAccountByIdReplyDecoder(event.getInfo().getBytes());
        return serverService.placeOrder(decoder.requestNo(), accountBO);
    }
}
