/**
 * Created by Walker.Zhang on 2015/3/27.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/27     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.msg.handler;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.converter.IAccountMsgCodec;
import com.flying.ams.msg.gen.GetAccountByIdRequest;
import com.flying.ams.service.IAccountService;
import com.flying.common.IReturnCode;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAcctByIdHandler implements IMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAcctByIdHandler.class);
    private IAccountService service;
    private IAccountMsgCodec msgConverter;

    public GetAcctByIdHandler(IAccountService service, IAccountMsgCodec msgConverter) {
        this.service = service;
        this.msgConverter = msgConverter;
    }

    @Override
    public byte[] handle(byte[] msg) {
        GetAccountByIdRequest request = msgConverter.getGetAccountByIdRequest(msg);
        int retCode = IReturnCode.SUCCESS;
        AccountBO acctBO = null;
        try {
            acctBO = service.getAccountBO(request.aid());
        } catch (ServiceException se) {
            logger.error("Exception in getting account bo", se);
            retCode = se.getCode();
        }
        return msgConverter.getGetAccountByIdReplyMsg(retCode, acctBO);
    }

}