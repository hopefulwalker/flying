/*
 Created by Walker on 2017/4/12.
 Revision History:
 Date          Who              Version      What
 2017/4/12     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IAsyncServerEngine;
import com.flying.framework.messaging.engine.IEngineConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerEngine implements IAsyncServerEngine {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientEngine.class);
    private IEngineConfig config;
    private EventLoopGroup acceptorGroup;
    private EventLoopGroup workerGroup;

    public NettyServerEngine(IEngineConfig config) {
        this.config = config;
    }

    @Override
    public IEngineConfig getConfig() {
        return config;
    }

    @Override
    public void start() {
        acceptorGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        IAsyncServerEngine self = this;
        bootstrap.group(acceptorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new ServerHandler(self));
                    }
                });
        for (IEndpoint endpoint : config.getEndpoints()) {
            bootstrap.bind(endpoint.getAddress(), endpoint.getPort());
        }
    }

    @Override
    public void stop() {
        acceptorGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

