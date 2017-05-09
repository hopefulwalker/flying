/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IAsyncClientEngine;
import com.flying.framework.messaging.engine.IEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class NettyClientEngine implements IAsyncClientEngine {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientEngine.class);
    private ChannelPoolMap<IEndpoint, FixedChannelPool> poolMap;
    private IEngineConfig config;
    private EventLoopGroup eventLoopGroup;

    public NettyClientEngine(IEngineConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        IAsyncClientEngine self = this;
        poolMap = new AbstractChannelPoolMap<IEndpoint, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(IEndpoint key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key.getAddress(), key.getPort()), new AbstractChannelPoolHandler() {
                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler(self));
                    }
                }, 5);
            }
        };
    }

    @Override
    public void stop() {
        eventLoopGroup.shutdownGracefully();
    }

    @Override
    public IEngineConfig getConfig() {
        return config;
    }

    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, config.getEndpoints().size());
        FixedChannelPool pool = poolMap.get(config.getEndpoints().get(randomIndex));
        Future<Channel> future = pool.acquire();
        future.addListener((FutureListener<Channel>) f -> {
            if (f.isSuccess()) {
                Channel ch = f.getNow();
                // Do somethings
                ByteBuf msg = Unpooled.buffer(msgEvent.getInfo().getByteArray().length);
                msg.writeBytes(msgEvent.getInfo().getByteArray());
                ch.writeAndFlush(msg);
                // Release back to pool
                pool.release(ch);
            }
        });
    }
}
