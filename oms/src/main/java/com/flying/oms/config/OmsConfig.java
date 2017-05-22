/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.config;

import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.msg.handler.ServiceMsgListener;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.engine.impl.zmq.UCAsyncServerEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.monitor.model.IServer;
import com.flying.monitor.model.Server;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.msg.codec.IMonitorMsgCodec;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.client.ServerReporter;
import com.flying.oms.model.OrderBO;
import com.flying.oms.msg.codec.IOrderMsgCodec;
import com.flying.oms.msg.codec.OrderMsgCodec;
import com.flying.oms.msg.handler.OrderRequestHandler;
import com.flying.oms.service.IOrderService;
import com.flying.oms.service.server.OrderServerService2;
import com.flying.oms.service.server.fsm.PooledOrderStateMachineFactory;
import com.flying.oms.service.server.fsm.OrderEvents;
import com.flying.oms.service.server.fsm.OrderStates;
import com.flying.util.schedule.Scheduler;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Import({MonitorServiceConfig.class, AccountServiceConfig.class, StateMachineConfig.class})
public class OmsConfig {
    @Bean
    public IServer orderServer(IMonitorMsgCodec monitorMsgCodec, IMonitorService monitorClientService,
                               StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister) {
        Server server = new Server(serverBO());
        server.setScheduler(scheduler(monitorClientService));
        server.setServerEngine(orderServerEngine(monitorMsgCodec, stateMachinePersister));
        return server;
    }

    @Bean
    public ServerBO serverBO() {
        return new ServerBO("SZ", (short) 3, "OS-Walker");
    }

    @Bean
    public Scheduler scheduler(IMonitorService monitorClientService) {
        List<Runnable> tasks = new ArrayList<>(1);
        tasks.add(serverReporter(monitorClientService));
        return new Scheduler(tasks);
    }

    @Bean
    public IServerEngine orderServerEngine(IMonitorMsgCodec monitorMsgCodec, StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister) {
        IServerEngine engine = new UCAsyncServerEngine(omsListenEndpoint());
        engine.setMsgEventListener(omsMsgListener(monitorMsgCodec, stateMachinePersister));
        engine.setWorkers(10);
        return engine;
    }

    @Bean
    public IMsgEventListener omsMsgListener(IMonitorMsgCodec monitorMsgCodec, StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister) {
        Map<Short, IMsgHandler> handlers = new HashMap<>();
        handlers.put((short) 1, new OrderRequestHandler(orderServerService2(stateMachinePersister), orderMsgCodec()));
        return new ServiceMsgListener(handlers, monitorMsgCodec);
    }

    @Bean
    public IOrderService orderServerService2(StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister) {
        OrderServerService2 service = new OrderServerService2(machinePoolFactory(), machinePoolConfig(), orderCache());
        service.setStatesStateMachinePersister(stateMachinePersister);
        return service;
    }

    @Bean
    public Map<Long, OrderBO> orderCache() {
        return new HashMap<>();
    }

    @Bean
    public IOrderMsgCodec orderMsgCodec() {
        return new OrderMsgCodec();
    }

    @Bean
    public ServerReporter serverReporter(IMonitorService monitorClientService) {
        return new ServerReporter(monitorClientService, serverBO());
    }

    @Bean
    public IEndpoint omsListenEndpoint() {
        return new Endpoint();
    }

    @Bean
    public GenericObjectPoolConfig machinePoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMinIdle(10);
        return config;
    }

    @Bean
    public PooledOrderStateMachineFactory machinePoolFactory() {
        return new PooledOrderStateMachineFactory();
    }
}
