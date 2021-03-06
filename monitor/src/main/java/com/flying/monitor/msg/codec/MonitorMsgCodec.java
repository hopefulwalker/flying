/**
 * This file is generated by code generation tools.
 * Generation time is Thu Apr 20 11:42:42 CST 2017
 */
package com.flying.monitor.msg.codec;

import com.flying.common.IReturnCode;
import com.flying.util.net.CommUtils;
import org.agrona.concurrent.UnsafeBuffer;
import java.util.ArrayList;
import com.flying.monitor.msg.gen.ServerQueryReplyDecoder;
import java.util.List;
import com.flying.monitor.msg.gen.ServerRegistryRequestDecoder;
import com.flying.monitor.msg.gen.MessageHeaderDecoder;
import com.flying.monitor.msg.gen.ServerQueryRequestDecoder;
import com.flying.monitor.msg.gen.ServerQueryReplyEncoder;
import com.flying.monitor.msg.gen.ServerRegistryRequestEncoder;
import com.flying.monitor.msg.gen.MessageHeaderEncoder;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.IServerMsgType;
import com.flying.monitor.msg.gen.ServerQueryRequestEncoder;

public class MonitorMsgCodec implements IMonitorMsgCodec {
    public short getMsgType(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, varField
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        return headerDecoder.msgType();
    }
    public byte[] encodeServerRegistryRequest(ServerBO serverBO){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        ServerRegistryRequestEncoder bodyEncoder = new ServerRegistryRequestEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IServerMsgType.ServerRegistryRequest);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).uuid(serverBO.getUuid()).region(serverBO.getRegion()).serviceType(serverBO.getServiceType()).name(serverBO.getName()).endpoint(serverBO.getEndpoint()).workers(serverBO.getWorkers()).stateId(serverBO.getStateId()).reportTime(serverBO.getReportTime());
        
        return replyBytes;
    }
    public byte[] encodeServerQueryRequest(String region,short serviceType){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        ServerQueryRequestEncoder bodyEncoder = new ServerQueryRequestEncoder();
        
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength()];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IServerMsgType.ServerQueryRequest);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).region(region).serviceType(serviceType);
        
        return replyBytes;
    }
    public byte[] encodeServerQueryReply(int retCode,List<ServerBO> serverBOs){
        // headerEncoderClass, varHeaderEncoder, bodyEncoderClass, varBodyEncoder, msgTypeClass, bodyClass, varFields
        // elementExists, elementEncoderClass, varElementEncoder, elementClass, varElements, varElement, varElementFields
        MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        ServerQueryReplyEncoder bodyEncoder = new ServerQueryReplyEncoder();
        
        int numInGroup = (serverBOs == null) ? 0 : serverBOs.size();
        byte[] replyBytes = new byte[headerEncoder.encodedLength() + bodyEncoder.sbeBlockLength() +
                ServerQueryReplyEncoder.ServerBOsEncoder.sbeHeaderSize() + ServerQueryReplyEncoder.ServerBOsEncoder.sbeBlockLength() * numInGroup];
        
        UnsafeBuffer buffer = new UnsafeBuffer(replyBytes);
        // Build the encoded message, header first and then body.
        headerEncoder.wrap(buffer, 0).blockLength(bodyEncoder.sbeBlockLength())
                .templateId(bodyEncoder.sbeTemplateId()).schemaId(bodyEncoder.sbeSchemaId()).version(bodyEncoder.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IServerMsgType.ServerQueryReply);
        bodyEncoder.wrap(buffer, headerEncoder.encodedLength()).retCode(retCode);
        
        if (numInGroup > 0) {
            ServerQueryReplyEncoder.ServerBOsEncoder serverBOsEncoder = bodyEncoder.serverBOsCount(numInGroup);
            for (ServerBO serverBO : serverBOs) {
                serverBOsEncoder.next().uuid(serverBO.getUuid()).region(serverBO.getRegion()).serviceType(serverBO.getServiceType()).name(serverBO.getName()).endpoint(serverBO.getEndpoint()).workers(serverBO.getWorkers()).stateId(serverBO.getStateId()).reportTime(serverBO.getReportTime());
            }
        }
        
        return replyBytes;
    }
    public ServerRegistryRequestDecoder getServerRegistryRequestDecoder(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        ServerRegistryRequestDecoder bodyDecoder = new ServerRegistryRequestDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        return bodyDecoder;
    }
    public ServerQueryRequestDecoder getServerQueryRequestDecoder(byte[] msg){
        // headerDecoderClass, varHeaderDecoder, varMsg, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(msg);
        headerDecoder.wrap(buffer, 0);
        ServerQueryRequestDecoder bodyDecoder = new ServerQueryRequestDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        return bodyDecoder;
    }
    public List<ServerBO> getServerQueryReply(byte[] bytes){
        // varMsg, headerDecoderClass, varHeaderDecoder, bodyDecoderClass, varBodyDecoder
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        UnsafeBuffer buffer = new UnsafeBuffer(bytes);
        headerDecoder.wrap(buffer, 0);
        ServerQueryReplyDecoder bodyDecoder = new ServerQueryReplyDecoder();
        bodyDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
        
        // elementDecoderClass, varElementDecoder, elementClass, varElementFields, varElements, varElement, retTypeString
        // Analyze the reply and build the list of server bos.
        ServerQueryReplyDecoder.ServerBOsDecoder serverBOsDecoder = bodyDecoder.serverBOs();
        if (serverBOsDecoder.count() <= 0) return null;
        List<ServerBO> serverBOs = new ArrayList<>();
        ServerBO serverBO;
        while (serverBOsDecoder.hasNext()) {
            serverBOsDecoder.next();
            serverBO = new ServerBO();
            serverBO.setUuid(serverBOsDecoder.uuid());serverBO.setRegion(serverBOsDecoder.region());serverBO.setServiceType(serverBOsDecoder.serviceType());serverBO.setName(serverBOsDecoder.name());serverBO.setEndpoint(serverBOsDecoder.endpoint());serverBO.setWorkers(serverBOsDecoder.workers());serverBO.setStateId(serverBOsDecoder.stateId());serverBO.setReportTime(serverBOsDecoder.reportTime());
            serverBOs.add(serverBO);
        }
        return serverBOs;
        
        
    }

}
