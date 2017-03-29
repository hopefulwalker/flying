/**
 * This file is generated by code generation tools.
 * Generation time is Tue Jun 02 21:27:28 CST 2015
 */
package com.flying.monitor.msg.converter;

import com.flying.common.IReturnCode;
import com.flying.util.net.CommUtils;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flying.monitor.msg.gen.ServerQueryRequest;
import java.util.List;
import com.flying.monitor.service.MonitorServiceException;
import com.flying.monitor.msg.gen.MessageHeader;
import com.flying.monitor.msg.IServerMsgType;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.gen.ServerRegistryRequest;
import com.flying.monitor.msg.gen.ServerQueryReply;

public class MonitorMsgConverter implements IMonitorMsgConverter {
    private static Logger logger = LoggerFactory.getLogger(MonitorMsgConverter.class);
    public byte[] getServerQueryRequestMsg(String region,short serviceType) throws MonitorServiceException{
        // headerClass, varHeader, requestClass, varRequest, msgTypeClass, stringContained, varField, exception
		MessageHeader header = new MessageHeader();
        ServerQueryRequest request = new ServerQueryRequest();
        DirectBuffer requestBuffer = new DirectBuffer(new byte[header.size() + request.sbeBlockLength()]);
        // Build the report message, header first and then body.
        header.wrap(requestBuffer, 0, request.sbeSchemaVersion()).blockLength(request.sbeBlockLength())
                .templateId(request.sbeTemplateId()).schemaId(request.sbeSchemaId()).version(request.sbeSchemaVersion())
                .timestamp(System.currentTimeMillis()).sourceIP(CommUtils.getLocalIp4AddressAsInt())
                .msgType(IServerMsgType.ServerQueryRequest);
        try { 
            request.wrapForEncode(requestBuffer, header.size()).putRegion(region).serviceType(serviceType);
        } catch (UnsupportedEncodingException uee) {
            throw new MonitorServiceException(MonitorServiceException.FAILED_TO_BUILD_REQUEST, uee);
        }
        return requestBuffer.array();
    }
    public ServerRegistryRequest getServerRegistryRequest(byte[] msg) throws MonitorServiceException{
        // headerClass, varHeader, varBytes, requestClass, varRequest
        MessageHeader header = new MessageHeader();
        DirectBuffer requestBuffer = new DirectBuffer(msg);
        header.wrap(requestBuffer, 0, 0);
        ServerRegistryRequest request = new ServerRegistryRequest();
        request.wrapForDecode(requestBuffer, header.size(), header.blockLength(), header.version());
        return request;
    }
    public byte[] getServerRegistryRequestMsg(ServerBO serverBO) throws MonitorServiceException{
        // headerClass, varHeader, requestClass, varRequest, msgTypeClass, stringContained, varField, exception
		MessageHeader header = new MessageHeader();
        ServerRegistryRequest request = new ServerRegistryRequest();
        DirectBuffer requestBuffer = new DirectBuffer(new byte[header.size() + request.sbeBlockLength()]);
        // Build the report message, header first and then body.
        header.wrap(requestBuffer, 0, request.sbeSchemaVersion()).blockLength(request.sbeBlockLength())
                .templateId(request.sbeTemplateId()).schemaId(request.sbeSchemaId()).version(request.sbeSchemaVersion())
                .timestamp(System.currentTimeMillis()).sourceIP(CommUtils.getLocalIp4AddressAsInt())
                .msgType(IServerMsgType.ServerRegistryRequest);
        try { 
            request.wrapForEncode(requestBuffer, header.size()).putUuid(serverBO.getUuid()).putRegion(serverBO.getRegion()).serviceType(serverBO.getServiceType()).putName(serverBO.getName()).putEndpoint(serverBO.getEndpoint()).workers(serverBO.getWorkers()).stateId(serverBO.getStateId()).reportTime(serverBO.getReportTime());
        } catch (UnsupportedEncodingException uee) {
            throw new MonitorServiceException(MonitorServiceException.FAILED_TO_BUILD_REQUEST, uee);
        }
        return requestBuffer.array();
    }
    public byte[] getServerQueryReplyMsg(int retCode,List<ServerBO> serverBOs){
        // headerClass, varHeader, replyClass, varReply, varDTOs, msgDTOClass, msgTypeClass, varFields, varMsgDTO
        // varDTO, dtoClass, varDTOFields
        MessageHeader header = new MessageHeader();
        ServerQueryReply reply = new ServerQueryReply();
        int numInGroup = (serverBOs == null) ? 0 : serverBOs.size();
        byte[] replyBytes = new byte[header.size() + reply.sbeBlockLength() +
                ServerQueryReply.Servers.sbeHeaderSize() + ServerQueryReply.Servers.sbeBlockLength() * numInGroup];
        DirectBuffer replyBuffer = new DirectBuffer(replyBytes);
        header.wrap(replyBuffer, 0, reply.sbeSchemaVersion()).blockLength(reply.sbeBlockLength())
                .templateId(reply.sbeTemplateId()).schemaId(reply.sbeSchemaId()).version(reply.sbeSchemaVersion())
                .sourceIP(CommUtils.getLocalIp4AddressAsInt()).timestamp(System.currentTimeMillis())
                .msgType(IServerMsgType.ServerQueryReply);
        reply.wrapForEncode(replyBuffer, header.size()).retCode(retCode); //requestNo(requestNo).retCode(retCode);
        if (retCode!=IReturnCode.SUCCESS) return replyBytes;
        ServerQueryReply.Servers servers = reply.serversCount(numInGroup);
        if (numInGroup > 0) {
             try { 
                for (ServerBO serverBO : serverBOs) {
                    servers.next().putUuid(serverBO.getUuid()).putRegion(serverBO.getRegion()).serviceType(serverBO.getServiceType()).putName(serverBO.getName()).putEndpoint(serverBO.getEndpoint()).workers(serverBO.getWorkers()).stateId(serverBO.getStateId()).reportTime(serverBO.getReportTime());
                            //putUuid(serverBO.getUuid())
                            //.putRegion(serverBO.getRegion())
                            //.serviceType(serverBO.getServiceType())
                            //.putName(serverBO.getName())
                            //.putEndpoint(serverBO.getEndpoint())
                            //.reportTime(serverBO.getReportTime());
                }
            } catch (UnsupportedEncodingException uee) {
                logger.error("Error in building reply", uee);
                reply.retCode(IReturnCode.UNSUPPORTED_ENCODING);
            }
        }
        return replyBytes;
    }
    public List<ServerBO> getServerQueryReply(byte[] bytes) throws MonitorServiceException{
        // headerClass, varHeader, varBytes, msgTypeClass, replyClass, exception, varReply, msgDTOClass, varMsgDTO, dtoClass,
        // stringContained, varDTOFields, varDTOs, retKindName
        // Send and Receive the report message before timeout
        MessageHeader header = new MessageHeader();
        DirectBuffer replyBuffer = new DirectBuffer(bytes);
        header.wrap(replyBuffer, 0, 0);
        if (header.msgType() != IServerMsgType.ServerQueryReply) {
            throw new MonitorServiceException(MonitorServiceException.MISMATCH_REPLY, "expected:" + IServerMsgType.ServerQueryReply + "actual:" + header.msgType());
        }
        ServerQueryReply reply = new ServerQueryReply();
        reply.wrapForDecode(replyBuffer, header.size(), header.blockLength(), header.version());
        if (reply.retCode() != IReturnCode.SUCCESS) {
            throw new MonitorServiceException(reply.retCode());
        }
        // Analyze the reply and build the list of server bos.
        ServerQueryReply.Servers servers = reply.servers();
        if (servers.count() <= 0) return null;
        List<ServerBO> serverBOs = new ArrayList<>();
        ServerBO serverBO;
        while (servers.hasNext()) {
             try { 
                servers.next();
                serverBO = new ServerBO();
                                serverBO.setUuid(servers.getUuid());
                serverBO.setRegion(servers.getRegion());
                serverBO.setServiceType(servers.serviceType());
                serverBO.setName(servers.getName());
                serverBO.setEndpoint(servers.getEndpoint());
                serverBO.setWorkers(servers.workers());
                serverBO.setStateId(servers.stateId());
                serverBO.setReportTime(servers.reportTime());

                serverBOs.add(serverBO);
            } catch (UnsupportedEncodingException uee) {
                throw new MonitorServiceException(MonitorServiceException.FAILED_TO_BUILD_REPLY, uee);
            }
        }
        return serverBOs;
    }
    public ServerQueryRequest getServerQueryRequest(byte[] msg){
        // headerClass, varHeader, varBytes, requestClass, varRequest
        MessageHeader header = new MessageHeader();
        DirectBuffer requestBuffer = new DirectBuffer(msg);
        header.wrap(requestBuffer, 0, 0);
        ServerQueryRequest request = new ServerQueryRequest();
        request.wrapForDecode(requestBuffer, header.size(), header.blockLength(), header.version());
        return request;
    }
    public short getMsgType(byte[] msg){
        // headerClass, varHeader, varBytes, varFields
        MessageHeader header = new MessageHeader();
        DirectBuffer buffer = new DirectBuffer(msg);
        header.wrap(buffer, 0, 0);
        return header.msgType();
    }

}
