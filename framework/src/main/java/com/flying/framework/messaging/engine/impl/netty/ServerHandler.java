/*
 Created by Walker on 2017/4/12.
 Revision History:
 Date          Who              Version      What
 2017/4/12     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    private IServerCommEngine engine;

    public ServerHandler(IServerCommEngine engine) {
        this.engine = engine;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] request = new byte[buf.readableBytes()];
        buf.readBytes(request);
        if (engine.getConfig().getServerListener() != null) {
            IMsgEventResult result = engine.getConfig().getServerListener().onEvent(MsgEvent.newInstance(IMsgEvent.ID_REPLY, engine, request));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(result.getBytes()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error occurs in server handler" + ctx.channel().remoteAddress().toString(), cause);
        ctx.close();
    }
}