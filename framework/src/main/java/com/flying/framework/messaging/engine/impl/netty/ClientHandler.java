/*
 Created by Walker on 2017/4/10.
 Revision History:
 Date          Who              Version      What
 2017/4/10     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.engine.IAsyncClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientHandler extends ChannelDuplexHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private IAsyncClientEngine engine;

    public ClientHandler(IAsyncClientEngine engine) {
        this.engine = engine;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] request = new byte[buf.readableBytes()];
        buf.readBytes(request);
        if (engine.getConfig().getMsgEventListener() != null) {
            engine.getConfig().getMsgEventListener().onEvent(new MsgEvent(IMsgEvent.ID_REPLY, engine, new MsgEventInfo(request)));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error occurs in client handler" + ctx.channel().remoteAddress().toString(), cause);
        ctx.close();
    }
}
