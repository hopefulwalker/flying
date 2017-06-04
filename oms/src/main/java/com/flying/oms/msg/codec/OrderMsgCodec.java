/**
 * This file is generated by code generation tools.
 * Generation time is Fri Jun 02 23:00:51 CST 2017
 */
package com.flying.oms.msg.codec;

import com.flying.common.IReturnCode;
import com.flying.util.net.CommUtils;
import org.agrona.concurrent.UnsafeBuffer;
import java.util.ArrayList;
import com.flying.oms.msg.gen.OrderRequestEncoder;
import com.flying.oms.msg.gen.MessageHeaderDecoder;
import com.flying.oms.msg.gen.OrderReplyDecoder;
import com.flying.oms.msg.IOrderMsgType;
import com.flying.oms.msg.gen.MessageHeaderEncoder;
import com.flying.oms.model.OrderStates;
import com.flying.oms.msg.gen.OrderReplyEncoder;
import com.flying.oms.msg.gen.OrderRequestDecoder;
import com.flying.oms.model.OrderBO;

public class OrderMsgCodec implements IOrderMsgCodec {
    public OrderRequestDecoder getOrderRequestDecoder(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        OrderRequestDecoder bodyDecoder = new OrderRequestDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        return bodyDecoder;
    }
    public byte[] encodeOrderRequest(OrderBO orderBO){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        OrderRequestEncoder bodyEncoder = new OrderRequestEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IOrderMsgType.OrderRequest);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).extNo(orderBO.getExtNo()).acctId(orderBO.getAcctId()).exchId(orderBO.getExchId()).sectCode(orderBO.getSectCode()).bsSideId(orderBO.getBsSideId()).price(orderBO.getPrice()).qty(orderBO.getQty());
        
        return replyBytes;
    }
    public short getMsgType(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, varField
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        return headerDecoder.msgType();
    }
    public OrderBO getOrderReply(byte[] bytes){
        // varMsg, headerDecoderClass, varHeaderDecoder, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(bytes);
        headerDecoder.wrap(buffer, 0);
        OrderReplyDecoder bodyDecoder = new OrderReplyDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        
        
        // elementClass, varElementFields, varElement
        // Analyze the reply and build dto.
        OrderBO orderBO = new OrderBO();
        orderBO.setOid(bodyDecoder.oid());orderBO.setExtNo(bodyDecoder.extNo());orderBO.setAcctId(bodyDecoder.acctId());orderBO.setExchId(bodyDecoder.exchId());orderBO.setSectCode(bodyDecoder.sectCode());orderBO.setBsSideId(bodyDecoder.bsSideId());orderBO.setPrice(bodyDecoder.price());orderBO.setQty(bodyDecoder.qty());orderBO.setCntrNo(bodyDecoder.cntrNo());orderBO.setBizDate(bodyDecoder.bizDate());orderBO.setState(OrderStates.get(bodyDecoder.state()));orderBO.setStateEnteredCode(bodyDecoder.stateEnteredCode());orderBO.setCreateTime(bodyDecoder.createTime());orderBO.setUpdateTime(bodyDecoder.updateTime());
        return orderBO;
        
    }
    public byte[] encodeOrderReply(int retCode){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        OrderReplyEncoder bodyEncoder = new OrderReplyEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IOrderMsgType.OrderReply);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).retCode(retCode);
        
        return replyBytes;
    }
    public byte[] encodeOrderReply(int retCode,OrderBO orderBO){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        OrderReplyEncoder bodyEncoder = new OrderReplyEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IOrderMsgType.OrderReply);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).retCode(retCode).oid(orderBO.getOid()).extNo(orderBO.getExtNo()).acctId(orderBO.getAcctId()).exchId(orderBO.getExchId()).sectCode(orderBO.getSectCode()).bsSideId(orderBO.getBsSideId()).price(orderBO.getPrice()).qty(orderBO.getQty()).cntrNo(orderBO.getCntrNo()).bizDate(orderBO.getBizDate()).state(orderBO.getState().value()).stateEnteredCode(orderBO.getStateEnteredCode()).createTime(orderBO.getCreateTime()).updateTime(orderBO.getUpdateTime());
        
        return replyBytes;
    }

}
