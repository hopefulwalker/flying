/*
 Created by Walker.Zhang on 2015/3/27.
 Revision History:
 Date          Who              Version      What
 2015/3/27     Walker.Zhang     0.1.0        Created.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.ams.msg.handler;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.ams.msg.gen.GetAccountByIdRequestDecoder;
import com.flying.ams.service.IAccountService;
import com.flying.common.IReturnCode;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAcctByIdRequestHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAcctByIdRequestHandler.class);
    private IAccountService service;
    private IAccountMsgCodec msgCodec;

    public GetAcctByIdRequestHandler(IAccountService service, IAccountMsgCodec msgCodec) {
        this.service = service;
        this.msgCodec = msgCodec;
    }

    @Override
    public IMsgEventResult handle(IMsgEvent event) {
        GetAccountByIdRequestDecoder request = msgCodec.getGetAccountByIdRequestDecoder(event.getInfo().getBytes());
        int retCode = IReturnCode.SUCCESS;
        AccountBO acctBO = null;
        try {
            acctBO = service.getAccountBO(request.aid());
        } catch (ServiceException se) {
            logger.error("Exception in getting account bo", se);
            retCode = se.getCode();
        }
        return new MsgEventResult(event.getInfo().getFroms(), msgCodec.encodeGetAccountByIdReply(request.requestNo(), retCode, acctBO));
    }
}