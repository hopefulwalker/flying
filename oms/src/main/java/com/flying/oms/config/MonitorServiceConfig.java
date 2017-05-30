/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.config;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.engine.impl.jdk.BroadcastClientEngine;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.msg.codec.MonitorMsgCodec;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.client.BCMonitorClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MonitorServiceConfig {
    @Bean
    public IMonitorMsgCodec monitorMsgCodec() {
        return new MonitorMsgCodec();
    }

    @Bean
    public IMonitorService monitorClientService() {
        return new BCMonitorClientService(broadcastEngine(), monitorMsgCodec());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public IClientEngine broadcastEngine() {
        return new BroadcastClientEngine(broadcastEndpoints());
    }

    @Bean
    public List<IEndpoint> broadcastEndpoints() {
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(new Endpoint("udp", "255.255.255.255", 51688));
        return endpoints;
    }
}
