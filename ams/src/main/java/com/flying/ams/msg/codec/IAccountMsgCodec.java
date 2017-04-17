/**
 * Created by Walker.Zhang on 2015/5/26.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/26     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.msg.codec;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.gen.GetAccountByIdRequestDecoder;
import com.flying.ams.service.AccountServiceException;
import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CnvInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;

public interface IAccountMsgCodec extends IMsgCodec {
    @Override
    @CnvInfo(type = CnvInfo.GET_HEADER_INFO,
            headerDecoderClass = "com.flying.ams.msg.gen.MessageHeaderDecoder",
            fields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = CnvInfo.ENCODE_MSG,
            headerEncoderClass = "com.flying.ams.msg.gen.MessageHeaderEncoder",
            bodyEncoderClass = "com.flying.ams.msg.gen.GetAccountByIdRequestEncoder",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public byte[] encodeGetAccountByIdRequest(@Name("aid") @Fields("aid") long aid);

    @CnvInfo(type = CnvInfo.GET_BODY_DECODER,
            headerDecoderClass = "com.flying.ams.msg.gen.MessageHeaderDecoder",
            bodyDecoderClass = "com.flying.ams.msg.gen.GetAccountByIdRequestDecoder",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public GetAccountByIdRequestDecoder getGetAccountByIdRequest(@Name("msg") byte[] msg);

    @CnvInfo(type = CnvInfo.ENCODE_MSG,
            headerEncoderClass = "com.flying.ams.msg.gen.MessageHeaderEncoder",
            bodyEncoderClass = "com.flying.ams.msg.gen.GetAccountByIdReplyEncoder",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType")
    public byte[] encodeGetAccountByIdReply(@Name("retCode") @Fields int retCode,
                                            @Name("accountBO") @Fields AccountBO accountBO);

    @CnvInfo(type = CnvInfo.DECODE_MSG_COLLECTION,
            headerDecoderClass = "com.flying.ams.msg.gen.MessageHeaderDecoder",
            bodyDecoderClass = "com.flying.ams.msg.gen.GetAccountByIdReplyDecoder",
            msgTypeClass = "com.flying.ams.msg.IAccountMsgType",
            fields = "aid, type, custId, parentId, statusId, createTime, updateTime")
    public AccountBO getGetAccountByIdReply(@Name("bytes") byte[] bytes) throws AccountServiceException;
}