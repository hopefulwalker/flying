/*
 Created by Walker on 2017/4/17.
 Revision History:
 Date          Who              Version      What
 2017/4/17      Walker           0.1.0        Created. 
*/
package com.flying.oms.msg.codec;

public class IOrderMsgCodec2 {
    @Override
    @CnvInfo(type = 1,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            dtoFields = "msgType")
    public short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = 101,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            requestClass = "com.flying.oms.msg.gen.OrderRequest",
            msgTypeClass = "com.flying.oms.msg.IOrderMsgType")
    public byte[] getOrderRequestMsg(@Name("orderBO") @Fields("extNo,acctId,bsSideId,exchId,sectCode,price,qty") OrderBO orderBO) throws OrderServiceException;

    @CnvInfo(type = 102,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            requestClass = "com.flying.oms.msg.gen.OrderRequest",
            msgTypeClass = "com.flying.oms.msg.IOrderMsgType")
    public OrderRequest getOrderRequest(@Name("msg") byte[] msg);

    @CnvInfo(type = 201,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            replyClass = "com.flying.oms.msg.gen.OrderReply",
            msgTypeClass = "com.flying.oms.msg.IOrderMsgType")
    public byte[] getOrderReplyMsg(@Name("retCode") @ReplyFields("retCode") int retCode, @ReplyFields(value = "oid,extNo,acctId,exchId,sectCode,bsSideId,price,qty,cntrNo,bizDate,stateId,stateEnteredCode,createTime,updateTime") @Name("orderBO") OrderBO orderBO);

    @CnvInfo(type = 202,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            replyClass = "com.flying.oms.msg.gen.OrderReply",
            msgTypeClass = "com.flying.oms.msg.IOrderMsgType",
            dtoFields = "oid,extNo,acctId,exchId,sectCode,bsSideId,price,qty,cntrNo,bizDate,stateId,stateEnteredCode,createTime,updateTime")
    public OrderBO getOrderReply(@Name("bytes") byte[] bytes) throws OrderServiceException;

}
