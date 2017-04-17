/**
 * Created by Walker.Zhang on 2015/3/27.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/27     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.msg.handler;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.ams.msg.gen.GetAccountByIdRequestDecoder;
import com.flying.ams.service.IAccountService;
import com.flying.common.IReturnCode;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAcctByIdHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAcctByIdHandler.class);
    private IAccountService service;
    private IAccountMsgCodec msgCodec;

    public GetAcctByIdHandler(IAccountService service, IAccountMsgCodec msgCodec) {
        this.service = service;
        this.msgCodec = msgCodec;
    }

    @Override
    public byte[] handle(byte[] msg) {
        GetAccountByIdRequestDecoder request = msgCodec.getGetAccountByIdRequest(msg);
        int retCode = IReturnCode.SUCCESS;
        AccountBO acctBO = null;
        try {
            acctBO = service.getAccountBO(request.aid());
        } catch (ServiceException se) {
            logger.error("Exception in getting account bo", se);
            retCode = se.getCode();
        }
        return msgCodec.encodeGetAccountByIdReply(retCode, acctBO);
    }

}