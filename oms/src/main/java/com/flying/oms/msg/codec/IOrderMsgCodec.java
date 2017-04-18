/**
 * Created by Walker.Zhang on 2015/5/28.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/28     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.msg.codec;

import com.flying.common.msg.codec.IMsgCodec;
import com.flying.common.msg.codec.anno.CodecInfo;
import com.flying.common.msg.codec.anno.DefaultInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.gen.OrderRequestDecoder;

@DefaultInfo(headerDecoderClass = "com.flying.oms.msg.gen.MessageHeaderDecoder",
        headerEncoderClass = "com.flying.oms.msg.gen.MessageHeaderEncoder",
        msgTypeClass = "com.flying.ams.oms.IOrderMsgType",
        headerCodecPackage = "com.flying.oms.msg.gen",
        bodyCodecPackage = "com.flying.oms.msg.gen",
        msgTypePackage = "com.flying.oms.msg")
public interface IOrderMsgCodec extends IMsgCodec {
    @Override
    @CodecInfo(type = CodecInfo.GET_HEADER_INFO,
            fields = "msgType")
    short getMsgType(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "OrderRequestEncoder")
    byte[] encodeOrderRequest(@Name("orderBO") @Fields OrderBO orderBO);

    @CodecInfo(type = CodecInfo.DECODE_MSG,
            bodyDecoderClass = "OrderRequestDecoder")
    OrderRequestDecoder getOrderRequestDecoder(@Name("msg") byte[] msg);

    @CodecInfo(type = CodecInfo.ENCODE_MSG,
            bodyEncoderClass = "OrderReplyEncoder")
    public byte[] encodeOrderReply(@Name("retCode") @Fields int retCode, @Name("orderBO") @Fields OrderBO orderBO);

    @CodecInfo(type = CodecInfo.DECODE_MSG,
            bodyDecoderClass = "OrderReply")
    OrderBO getOrderReply(@Name("bytes") byte[] bytes);
}
