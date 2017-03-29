/**
 * Created by Walker.Zhang on 2015/5/26.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/26     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.msg.converter;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.gen.GetAccountByIdRequest;
import com.flying.ams.service.AccountServiceException;
import com.flying.common.msg.converter.IMsgConverter;
import com.flying.common.msg.converter.anno.CnvInfo;
import com.flying.common.msg.converter.anno.Name;
import com.flying.common.msg.converter.anno.ReplyFields;
import com.flying.common.msg.converter.anno.RequestFields;

public interface IAccountMsgConverter extends IMsgConverter {
    @Override
    @CnvInfo(type = 1,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            dtoFields = "msgType")
    public short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = 101,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            requestClass = "com.flying.ams.msg.gen.GetAccountByIdRequest",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public byte[] getGetAccountByIdRequestMsg(@Name("aid") @RequestFields("aid") long aid) throws AccountServiceException;

    @CnvInfo(type = 102,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            requestClass = "com.flying.ams.msg.gen.GetAccountByIdRequest",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public GetAccountByIdRequest getGetAccountByIdRequest(@Name("msg") byte[] msg);

    @CnvInfo(type = 201,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            replyClass = "com.flying.ams.msg.gen.GetAccountByIdReply",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public byte[] getGetAccountByIdReplyMsg(@Name("retCode") @ReplyFields("retCode") int retCode,
                                            @ReplyFields(value = "aid, type, custId, parentId, statusId, createTime, updateTime") @Name("accountBO") AccountBO accountBO);

    @CnvInfo(type = 202,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            replyClass = "com.flying.ams.msg.gen.GetAccountByIdReply",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType",
            dtoFields = "aid, type, custId, parentId, statusId, createTime, updateTime")
    public AccountBO getGetAccountByIdReply(@Name("bytes") byte[] bytes) throws AccountServiceException;
}