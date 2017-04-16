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
import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CnvInfo;
import com.flying.common.msg.codec.anno.Name;
import com.flying.common.msg.codec.anno.ReplyFields;
import com.flying.common.msg.codec.anno.Fields;

public interface IAccountMsgCodec extends IMsgCodec {
    @Override
    @CnvInfo(type = 1,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            dtoFields = "msgType")
    public short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = 101,
            headerClass = "com.flying.ams.msg.gen.MessageHeader",
            requestClass = "com.flying.ams.msg.gen.GetAccountByIdRequest",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public byte[] getGetAccountByIdRequestMsg(@Name("aid") @Fields("aid") long aid) throws AccountServiceException;

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