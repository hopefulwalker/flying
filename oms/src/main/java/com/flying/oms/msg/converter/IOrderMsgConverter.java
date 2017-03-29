/**
 * Created by Walker.Zhang on 2015/5/28.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/28     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.msg.converter;

import com.flying.common.msg.converter.IMsgConverter;
import com.flying.common.msg.converter.anno.CnvInfo;
import com.flying.common.msg.converter.anno.Name;
import com.flying.common.msg.converter.anno.ReplyFields;
import com.flying.common.msg.converter.anno.RequestFields;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.gen.OrderRequest;
import com.flying.oms.service.OrderServiceException;

public interface IOrderMsgConverter extends IMsgConverter {
    @Override
    @CnvInfo(type = 1,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            dtoFields = "msgType")
    public short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = 101,
            headerClass = "com.flying.oms.msg.gen.MessageHeader",
            requestClass = "com.flying.oms.msg.gen.OrderRequest",
            msgTypeClass = "com.flying.oms.msg.IOrderMsgType")
    public byte[] getOrderRequestMsg(@Name("orderBO") @RequestFields("extNo,acctId,bsSideId,exchId,sectCode,price,qty") OrderBO orderBO) throws OrderServiceException;

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
