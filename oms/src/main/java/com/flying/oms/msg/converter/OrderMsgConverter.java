/**
 * This file is generated by code generation tools.
 * Generation time is Tue Jun 02 21:27:57 CST 2015
 */
package com.flying.oms.msg.converter;

import com.flying.common.IReturnCode;
import com.flying.util.net.CommUtils;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flying.oms.msg.gen.MessageHeader;
import com.flying.oms.msg.IOrderMsgType;
import com.flying.oms.msg.gen.OrderRequest;
import com.flying.oms.service.OrderServiceException;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.gen.OrderReply;

public class OrderMsgConverter implements IOrderMsgConverter {
    private static Logger logger = LoggerFactory.getLogger(OrderMsgConverter.class);
    public byte[] getOrderRequestMsg(OrderBO orderBO) throws OrderServiceException{
        // headerClass, varHeader, requestClass, varRequest, msgTypeClass, stringContained, varField, exception
		MessageHeader header = new MessageHeader();
        OrderRequest request = new OrderRequest();
        DirectBuffer requestBuffer = new DirectBuffer(new byte[header.size() + request.sbeBlockLength()]);
        // Build the report message, header first and then body.
        header.wrap(requestBuffer, 0, request.sbeSchemaVersion()).blockLength(request.sbeBlockLength())
                .templateId(request.sbeTemplateId()).schemaId(request.sbeSchemaId()).version(request.sbeSchemaVersion())
                .timestamp(System.currentTimeMillis()).sourceIP(CommUtils.getLocalIp4AddressAsInt())
                .msgType(IOrderMsgType.OrderRequest);
        try { 
            request.wrapForEncode(requestBuffer, header.size()).extNo(orderBO.getExtNo()).acctId(orderBO.getAcctId()).bsSideId(orderBO.getBsSideId()).exchId(orderBO.getExchId()).putSectCode(orderBO.getSectCode()).price(orderBO.getPrice()).qty(orderBO.getQty());
        } catch (UnsupportedEncodingException uee) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_BUILD_REQUEST, uee);
        }
        return requestBuffer.array();
    }
    public OrderRequest getOrderRequest(byte[] msg){
        // headerClass, varHeader, varBytes, requestClass, varRequest
        MessageHeader header = new MessageHeader();
        DirectBuffer requestBuffer = new DirectBuffer(msg);
        header.wrap(requestBuffer, 0, 0);
        OrderRequest request = new OrderRequest();
        request.wrapForDecode(requestBuffer, header.size(), header.blockLength(), header.version());
        return request;
    }
    public byte[] getOrderReplyMsg(int retCode,OrderBO orderBO){
        // headerClass, varHeader, replyClass, varReply, msgTypeClass, varFields, varDTOFields, stringContained.
        MessageHeader header = new MessageHeader();
        OrderReply reply = new OrderReply();
        byte[] replyBytes = new byte[header.size() + reply.sbeBlockLength()];
        DirectBuffer replyBuffer = new DirectBuffer(replyBytes);
        header.wrap(replyBuffer, 0, reply.sbeSchemaVersion()).blockLength(reply.sbeBlockLength())
                .templateId(reply.sbeTemplateId()).schemaId(reply.sbeSchemaId()).version(reply.sbeSchemaVersion())
                .timestamp(System.currentTimeMillis()).sourceIP(CommUtils.getLocalIp4AddressAsInt())
                .msgType(IOrderMsgType.OrderReply);
        reply.wrapForEncode(replyBuffer, header.size()).retCode(retCode);
        if (retCode!=IReturnCode.SUCCESS) return replyBytes;
        if (orderBO != null) {
             try { 
            reply.oid(orderBO.getOid()).extNo(orderBO.getExtNo()).acctId(orderBO.getAcctId()).exchId(orderBO.getExchId()).putSectCode(orderBO.getSectCode()).bsSideId(orderBO.getBsSideId()).price(orderBO.getPrice()).qty(orderBO.getQty()).putCntrNo(orderBO.getCntrNo()).bizDate(orderBO.getBizDate()).stateId(orderBO.getStateId()).stateEnteredCode(orderBO.getStateEnteredCode()).createTime(orderBO.getCreateTime()).updateTime(orderBO.getUpdateTime());
            } catch (UnsupportedEncodingException uee) {
                logger.error("Error in building reply", uee);
                reply.retCode(IReturnCode.UNSUPPORTED_ENCODING);
            }
        }
        return replyBytes;
    }
    public OrderBO getOrderReply(byte[] bytes) throws OrderServiceException{
        // headerClass, varHeader, varBytes, replyClass, varReply, dtoClass, varDTO, stringContained, varDTOFields, exception
        MessageHeader header = new MessageHeader();
        DirectBuffer replyBuffer = new DirectBuffer(bytes);
        header.wrap(replyBuffer, 0, 0);
        if (header.msgType() != IOrderMsgType.OrderReply) {
            throw new OrderServiceException(OrderServiceException.MISMATCH_REPLY, "expected:" + IOrderMsgType.OrderReply + "actual:" + header.msgType());
        }
        OrderReply reply = new OrderReply();
        reply.wrapForDecode(replyBuffer, header.size(), header.blockLength(), header.version());
        if (reply.retCode() != IReturnCode.SUCCESS) {
            throw new OrderServiceException(reply.retCode());
        }
        // Analyze the reply and build dto.
        OrderBO orderBO = new OrderBO();
         try { 
                orderBO.setOid(reply.oid());
        orderBO.setExtNo(reply.extNo());
        orderBO.setAcctId(reply.acctId());
        orderBO.setExchId(reply.exchId());
        orderBO.setSectCode(reply.getSectCode());
        orderBO.setBsSideId(reply.bsSideId());
        orderBO.setPrice(reply.price());
        orderBO.setQty(reply.qty());
        orderBO.setCntrNo(reply.getCntrNo());
        orderBO.setBizDate(reply.bizDate());
        orderBO.setStateId(reply.stateId());
        orderBO.setStateEnteredCode(reply.stateEnteredCode());
        orderBO.setCreateTime(reply.createTime());
        orderBO.setUpdateTime(reply.updateTime());

        } catch (UnsupportedEncodingException uee) {
            throw new OrderServiceException(OrderServiceException.FAILED_TO_BUILD_REPLY, uee);
        }
        return orderBO;
    }
    public short getMsgType(byte[] msg){
        // headerClass, varHeader, varBytes, varFields
        MessageHeader header = new MessageHeader();
        DirectBuffer buffer = new DirectBuffer(msg);
        header.wrap(buffer, 0, 0);
        return header.msgType();
    }

}
