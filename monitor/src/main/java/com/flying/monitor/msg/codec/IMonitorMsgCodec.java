/**
 * Created by Walker.Zhang on 2015/5/20.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/20     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.msg.codec;

import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CodecInfo;
import com.flying.common.msg.codec.anno.DefaultInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.gen.ServerQueryRequestDecoder;
import com.flying.monitor.msg.gen.ServerRegistryRequestDecoder;

import java.util.List;

@DefaultInfo(headerDecoderClass = "com.flying.monitor.msg.gen.MessageHeaderDecoder",
        headerEncoderClass = "com.flying.monitor.msg.gen.MessageHeaderEncoder",
        msgTypeClass = "com.flying.monitor.msg.IServerMsgType",
        headerCodecPackage = "com.flying.monitor.msg.gen",
        bodyCodecPackage = "com.flying.monitor.msg.gen",
        msgTypePackage = "com.flying.monitor.msg")
public interface IMonitorMsgCodec extends IMsgCodec {
    @Override
    @CodecInfo(type = CodecInfo.GET_HEADER_INFO,
            fields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "ServerRegistryRequestEncoder")
    byte[] encodeServerRegistryRequest(@Name("serverBO") @Fields ServerBO serverBO);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "ServerQueryRequestEncoder")
    byte[] encodeServerQueryRequest(@Name("region") @Fields String region, @Name("serviceType") @Fields short serviceType);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "ServerQueryReplyEncoder")
    byte[] encodeServerQueryReply(@Name("retCode") @Fields int retCode,
                                  @Name("serverBOs")
                                  @Fields(elementEncoderClass = "ServerBOsEncoder") List<ServerBO> serverBOs);

    @CodecInfo(type = CodecInfo.GET_BODY_DECODER,
            bodyDecoderClass = "ServerRegistryRequestDecoder")
    ServerRegistryRequestDecoder getServerRegistryRequestDecoder(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.GET_BODY_DECODER,
            bodyDecoderClass = "ServerQueryRequestDecoder")
    ServerQueryRequestDecoder getServerQueryRequestDecoder(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.DECODE_MSG,
            bodyDecoderClass = "ServerQueryReplyDecoder",
            elementDecoderClass = "ServerBOsDecoder")
    public List<ServerBO> getServerQueryReply(@Name("bytes") byte[] bytes);
}