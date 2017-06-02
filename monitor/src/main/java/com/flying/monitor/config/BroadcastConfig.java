/*
 Created by Walker on 2017/5/12.
 Revision History:
 Date          Who              Version      What
 2017/5/12     Walker           0.3.5        Create to support java config according to spring rules.
*/
package com.flying.monitor.config;

import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.msg.handler.ServiceMsgListener;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IClientCommEngine;
import com.flying.framework.messaging.engine.IClientCommEngineConfig;
import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.engine.IServerCommEngineConfig;
import com.flying.framework.messaging.engine.impl.ClientCommEngineConfig;
import com.flying.framework.messaging.engine.impl.ServerCommEngineConfig;
import com.flying.framework.messaging.engine.impl.jdk.BCClientCommEngine;
import com.flying.framework.messaging.engine.impl.zmq.BCServerCommEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.monitor.model.IServer;
import com.flying.monitor.model.Server;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.msg.codec.MonitorMsgCodec;
import com.flying.monitor.msg.handler.ServerQueryRequestHandler;
import com.flying.monitor.msg.handler.ServerRegistryRequestHandler;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.client.BCMonitorClientService;
import com.flying.monitor.service.client.ServerReporter;
import com.flying.monitor.service.server.MonitorServerService;
import com.flying.monitor.service.server.ServerSweeper;
import com.flying.util.schedule.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class BroadcastConfig {
    @Bean
    public IServer monitorServer() {
        Server server = new Server(serverBO());
        server.setScheduler(scheduler());
        server.setServerEngine(monitorServerEngine());
        return server;
    }

    @Bean
    public IServerCommEngine monitorServerEngine() {
        return new BCServerCommEngine(serverEngineConfig());
    }

    @Bean
    public IServerCommEngineConfig serverEngineConfig() {
        return new ServerCommEngineConfig(new Endpoint("udp", "*", 51688), monitorMsgListener());
    }

    @Bean
    public IMsgEventListener monitorMsgListener() {
        Map<Short, IMsgHandler> handlers = new HashMap<>();
        handlers.put((short) 1, new ServerRegistryRequestHandler(monitorServerService(), monitorMsgCodec()));
        handlers.put((short) 3, new ServerQueryRequestHandler(monitorServerService(), monitorMsgCodec()));
        return new ServiceMsgListener(handlers, monitorMsgCodec());
    }

    @Bean
    public IMonitorService monitorServerService() {
        return new MonitorServerService();
    }

    @Bean
    public IMonitorMsgCodec monitorMsgCodec() {
        return new MonitorMsgCodec();
    }

    @Bean
    public Scheduler scheduler() {
        List<Runnable> tasks = new ArrayList<>(2);
        tasks.add(serverSweeper());
        tasks.add(serverReporter());
        return new Scheduler(tasks);
    }

    @Bean
    public ServerReporter serverReporter() {
        return new ServerReporter(bcMonitorClientService(), serverBO());
    }

    @Bean
    public IMonitorService bcMonitorClientService() {
        return new BCMonitorClientService(clientCommEngine(), monitorMsgCodec());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public IClientCommEngine clientCommEngine() {
        return new BCClientCommEngine(clientEngineConfig());
    }

    @Bean
    public ServerSweeper serverSweeper() {
        return new ServerSweeper(monitorServerService());
    }

    @Bean
    public IClientCommEngineConfig clientEngineConfig() {
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(new Endpoint("udp", "255.255.255.255", 51688));
        return new ClientCommEngineConfig(endpoints);
    }

    @Bean
    public ServerBO serverBO() {
        return new ServerBO("SZ", (short) 1, "MonitorServer-Walker");
    }
}