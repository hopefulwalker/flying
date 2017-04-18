/*
 Created by Walker.Zhang on 2015/5/26.
 Revision History:
 Date          Who              Version      What
 2015/5/26     Walker.Zhang     0.1.0        Created.
 2017/4/18     Walker.Zhang     0.3.2        Refactor to support SBE 1.6.2.
*/
package com.flying.ams.msg.codec;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.gen.GetAccountByIdRequestDecoder;
import com.flying.ams.service.AccountServiceException;
import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CodecInfo;
import com.flying.common.msg.codec.anno.DefaultInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;

@DefaultInfo(headerDecoderClass = "com.flying.ams.msg.gen.MessageHeaderDecoder",
        headerEncoderClass = "com.flying.ams.msg.gen.MessageHeaderEncoder",
        msgTypeClass = "com.flying.ams.msg.IAccountMsgType",
        headerCodecPackage = "com.flying.ams.msg.gen",
        bodyCodecPackage = "com.flying.ams.msg.gen",
        msgTypePackage = "com.flying.ams.msg")
public interface IAccountMsgCodec extends IMsgCodec {
    @Override
    @CodecInfo(type = CodecInfo.GET_HEADER_INFO,
            fields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "GetAccountByIdRequestEncoder")
    byte[] encodeGetAccountByIdRequest(@Name("aid") @Fields long aid);

    @CodecInfo(type = CodecInfo.GET_BODY_DECODER,
            bodyDecoderClass = "GetAccountByIdRequestDecoder")
    GetAccountByIdRequestDecoder getGetAccountByIdRequestDecoder(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "GetAccountByIdReplyEncoder")
    byte[] encodeGetAccountByIdReply(@Name("retCode") @Fields int retCode,
                                     @Name("accountBO") @Fields AccountBO accountBO);

    @CodecInfo(type = CodecInfo.DECODE_MSG,
            bodyDecoderClass = "GetAccountByIdReplyDecoder")
    AccountBO getGetAccountByIdReply(@Name("bytes") byte[] bytes) throws AccountServiceException;
}