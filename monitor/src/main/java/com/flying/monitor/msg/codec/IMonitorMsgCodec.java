/**
 * Created by Walker.Zhang on 2015/5/20.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/20     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.msg.codec;

import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CnvInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.gen.ServerQueryRequestDecoder;
import com.flying.monitor.msg.gen.ServerRegistryRequestDecoder;

import java.util.List;

public interface IMonitorMsgCodec extends IMsgCodec {
    @Override
    @CnvInfo(type = CnvInfo.GET_HEADER_INFO,
            headerDecoderClass = "com.flying.monitor.msg.gen.MessageHeaderDecoder",
            fields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CnvInfo(type = CnvInfo.ENCODE_MSG,
            headerEncoderClass = "com.flying.monitor.msg.gen.MessageHeaderEncoder",
            bodyEncoderClass = "com.flying.monitor.msg.gen.ServerRegistryRequestEncoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    byte[] encodeServerRegistryRequest(@Name("serverBO") @Fields ServerBO serverBO);

    @CnvInfo(type = CnvInfo.ENCODE_MSG,
            headerEncoderClass = "com.flying.monitor.msg.gen.MessageHeaderEncoder",
            bodyEncoderClass = "com.flying.monitor.msg.gen.ServerQueryRequestEncoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    byte[] encodeServerQueryRequest(@Name("region") @Fields String region, @Name("serviceType") @Fields short serviceType);

    @CnvInfo(type = CnvInfo.ENCODE_MSG,
            headerEncoderClass = "com.flying.monitor.msg.gen.MessageHeaderEncoder",
            bodyEncoderClass = "com.flying.monitor.msg.gen.ServerQueryReplyEncoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    byte[] encodeServerQueryReply(@Name("retCode") @Fields int retCode,
                                  @Name("serverBOs")
                                  @Fields(elementEncoderClass = "ServerBOsEncoder") List<ServerBO> serverBOs);

    @CnvInfo(type = CnvInfo.GET_BODY_DECODER,
            headerDecoderClass = "com.flying.monitor.msg.gen.MessageHeaderDecoder",
            bodyDecoderClass = "com.flying.monitor.msg.gen.ServerRegistryRequestDecoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    ServerRegistryRequestDecoder getServerRegistryRequestDecoder(@Name("msg") byte[] msg);

    @CnvInfo(type = CnvInfo.GET_BODY_DECODER,
            headerDecoderClass = "com.flying.monitor.msg.gen.MessageHeaderDecoder",
            bodyDecoderClass = "com.flying.monitor.msg.gen.ServerQueryRequestDecoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType")
    ServerQueryRequestDecoder getServerQueryRequest(@Name("msg") byte[] msg);

    @CnvInfo(type = CnvInfo.DECODE_MSG_COLLECTION,
            headerDecoderClass = "com.flying.monitor.msg.gen.MessageHeaderDecoder",
            bodyDecoderClass = "com.flying.monitor.msg.gen.ServerQueryReplyDecoder",
            msgTypeClass = "com.flying.monitor.msg.IServerMsgType",
            elementDecoderClass = "ServerBOsDecoder")
    public List<ServerBO> getServerQueryReply(@Name("bytes") byte[] bytes);
}