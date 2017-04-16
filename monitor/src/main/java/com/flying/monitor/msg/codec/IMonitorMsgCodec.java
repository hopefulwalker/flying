/**
 * Created by Walker.Zhang on 2015/5/20.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/20     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.msg.codec;

import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CnvInfo;
import com.flying.common.msg.codec.anno.Name;
import com.flying.common.msg.codec.anno.ReplyFields;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.gen.ServerQueryRequestDecoder;
import com.flying.monitor.msg.gen.ServerRegistryRequestDecoder;
import com.flying.monitor.service.MonitorServiceException;

import java.util.List;

public interface IMonitorMsgCodec extends IMsgCodec {
    @Override
    @CnvInfo(type = 1,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            dtoFields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = 101,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            requestClass = "com.flying.monitor.msg.gen.ServerRegistryRequest",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    public byte[] getServerRegistryRequestMsg(@Name("serverBO") @Fields("uuid, region, serviceType, name, endpoint, workers, stateId, reportTime") ServerBO serverBO) throws MonitorServiceException;

    @CnvInfo(type = 102,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            requestClass = "com.flying.monitor.msg.gen.ServerRegistryRequest",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    public ServerRegistryRequestDecoder getServerRegistryRequest(@Name("msg") byte[] msg) throws MonitorServiceException;

    @CnvInfo(type = 101,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            requestClass = "com.flying.monitor.msg.gen.ServerQueryRequest",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    public byte[] getServerQueryRequestMsg(@Name("region") @Fields("region") String region, @Name("serviceType") @Fields("serviceType") short serviceType) throws MonitorServiceException;

    @CnvInfo(type = 102,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            requestClass = "com.flying.monitor.msg.gen.ServerQueryRequest",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    public ServerQueryRequestDecoder getServerQueryRequest(@Name("msg") byte[] msg);

    @CnvInfo(type = 201,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            replyClass = "com.flying.monitor.msg.gen.ServerQueryReply",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    public byte[] getServerQueryReplyMsg(@Name("retCode") @ReplyFields("retCode") int retCode,
                                         @ReplyFields(value = "uuid, region, serviceType, name, endpoint, workers, stateId, reportTime",
                                                 msgDTOClass = "com.flying.monitor.msg.gen.ServerQueryReply.Servers") @Name("serverBOs") List<ServerBO> serverBOs);

    @CnvInfo(type = 202,
            headerClass = "com.flying.monitor.msg.gen.MessageHeader",
            replyClass = "com.flying.monitor.msg.gen.ServerQueryReply",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType",
            dtoFields = "uuid, region, serviceType, name, endpoint, workers, stateId, reportTime",
            msgDTOClass = "com.flying.monitor.msg.gen.ServerQueryReply.Servers")
    public List<ServerBO> getServerQueryReply(@Name("bytes") byte[] bytes) throws MonitorServiceException;
}