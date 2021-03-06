/**
 * This file is generated by code generation tools.
 * Generation time is Thu Apr 20 11:42:56 CST 2017
 */
package com.flying.ams.msg.codec;

import com.flying.common.IReturnCode;
import com.flying.util.net.CommUtils;
import org.agrona.concurrent.UnsafeBuffer;
import java.util.ArrayList;
import com.flying.ams.msg.gen.GetAccountByIdReplyDecoder;
import com.flying.ams.service.AccountServiceException;
import com.flying.ams.msg.gen.GetAccountByIdRequestDecoder;
import com.flying.ams.msg.gen.GetAccountByIdReplyEncoder;
import com.flying.ams.msg.gen.MessageHeaderDecoder;
import com.flying.ams.msg.IAccountMsgType;
import com.flying.ams.msg.gen.MessageHeaderEncoder;
import com.flying.ams.msg.gen.GetAccountByIdRequestEncoder;
import com.flying.ams.model.AccountBO;

public class AccountMsgCodec implements IAccountMsgCodec {
    public short getMsgType(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, varField
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        return headerDecoder.msgType();
    }
    public byte[] encodeGetAccountByIdRequest(long aid){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        GetAccountByIdRequestEncoder bodyEncoder = new GetAccountByIdRequestEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IAccountMsgType.GetAccountByIdRequest);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).aid(aid);
        
        return replyBytes;
    }
    public GetAccountByIdRequestDecoder getGetAccountByIdRequestDecoder(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        GetAccountByIdRequestDecoder bodyDecoder = new GetAccountByIdRequestDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        return bodyDecoder;
    }
    public byte[] encodeGetAccountByIdReply(int retCode,AccountBO accountBO){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        GetAccountByIdReplyEncoder bodyEncoder = new GetAccountByIdReplyEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IAccountMsgType.GetAccountByIdReply);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).retCode(retCode).aid(accountBO.getAid()).type(accountBO.getType()).custId(accountBO.getCustId()).parentId(accountBO.getParentId()).statusId(accountBO.getStatusId()).createTime(accountBO.getCreateTime()).updateTime(accountBO.getUpdateTime());
        
        return replyBytes;
    }
    public AccountBO getGetAccountByIdReply(byte[] bytes) throws AccountServiceException{
        // varMsg, headerDecoderClass, varHeaderDecoder, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(bytes);
        headerDecoder.wrap(buffer, 0);
        GetAccountByIdReplyDecoder bodyDecoder = new GetAccountByIdReplyDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        
        
        // elementClass, varElementFields, varElement
        // Analyze the reply and build dto.
        AccountBO accountBO = new AccountBO();
        accountBO.setAid(bodyDecoder.aid());accountBO.setType(bodyDecoder.type());accountBO.setCustId(bodyDecoder.custId());accountBO.setParentId(bodyDecoder.parentId());accountBO.setStatusId(bodyDecoder.statusId());accountBO.setCreateTime(bodyDecoder.createTime());accountBO.setUpdateTime(bodyDecoder.updateTime());
        return accountBO;
        
    }

}
