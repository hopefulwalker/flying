/*
 Created by Walker on 2017/4/9.
 Revision History:
 Date          Who              Version      What
 2017/4/9      Walker           0.1.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IAsyncClientEngine;
import com.flying.framework.messaging.engine.IAsyncClientEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class NettyClientEngine implements IAsyncClientEngine {
    private IAsyncClientEngineConfig config;
    private EventLoopGroup eventLoopGroup;
    ChannelPoolMap<IEndpoint, FixedChannelPool> poolMap;

    public void setConfig(IAsyncClientEngineConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        poolMap = new AbstractChannelPoolMap<IEndpoint, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(IEndpoint key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key.getAddress(), key.getPort()), new ChannelPoolHandler() {
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                    }

                    @Override
                    public void channelAcquired(Channel ch) throws Exception {
                    }

                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        ch.pipeline().addLast();
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
    public IAsyncClientEngineConfig getConfig() {
        return config;
    }

    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, config.getEndpoints().size());
        FixedChannelPool pool = poolMap.get(config.getEndpoints().get(randomIndex));
        Future<Channel> f = pool.acquire();
        f.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> f) {
                if (f.isSuccess()) {
                    Channel ch = f.getNow();
                    // Do somethings
                    // ...
                    // ...

                    // Release back to pool
                    pool.release(ch);
                }
            }
        });
    }
}
