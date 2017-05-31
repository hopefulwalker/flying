/*
 Created by Walker on 2017/4/12.
 Revision History:
 Date          Who              Version      What
 2017/4/12     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerCommEngine implements IServerCommEngine {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientCommEngine.class);
    private ICommEngineConfig config;
    private EventLoopGroup acceptorGroup;
    private EventLoopGroup workerGroup;

    public NettyServerCommEngine(ICommEngineConfig config) {
        this.config = config;
    }

    @Override
    public void setConfig(ICommEngineConfig config) {
        this.config = config;
    }

    @Override
    public ICommEngineConfig getConfig() {
        return config;
    }

    @Override
    public void start() {
        acceptorGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        IServerCommEngine self = this;
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

