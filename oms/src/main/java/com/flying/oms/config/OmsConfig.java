/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
                                             Add external bean as private final field to simplify the config code.
*/
package com.flying.oms.config;

import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.common.msg.handler.IMsgHandler;
import com.flying.common.msg.handler.ServiceMsgListener;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.engine.IServerCommEngineConfig;
import com.flying.framework.messaging.engine.impl.ServerCommEngineConfig;
import com.flying.framework.messaging.engine.impl.zmq.UCServerCommEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.monitor.model.IServer;
import com.flying.monitor.model.Server;
import com.flying.monitor.model.ServerBO;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.client.ServerReporter;
import com.flying.oms.model.OrderStates;
import com.flying.oms.msg.codec.IOrderMsgCodec;
import com.flying.oms.msg.codec.OrderMsgCodec;
import com.flying.oms.msg.handler.GetAccountByIdReplyHandler;
import com.flying.oms.msg.handler.OrderRequestHandler;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.OrderCenter;
import com.flying.oms.service.server.OrderServerService;
import com.flying.oms.service.server.fsm.InMemoryStateMachinePersist;
import com.flying.oms.service.server.fsm.PooledOrderStateMachineFactory;
import com.flying.oms.service.server.fsm.StateMachineConfig;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import com.flying.util.schedule.Scheduler;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Import({MonitorConfig.class, AccountConfig.class, StateMachineConfig.class})
public class OmsConfig {
    private final AccountCenter accountCenter;
    private final IAccountMsgCodec acctMsgCodec;
    private final IMonitorService monitorClientService;

    public OmsConfig(AccountCenter accountCenter,
                     IAccountMsgCodec acctMsgCodec,
                     IMonitorService monitorClientService) {
        this.accountCenter = accountCenter;
        this.acctMsgCodec = acctMsgCodec;
        this.monitorClientService = monitorClientService;
    }

    @Bean
    public IServer orderServer() {
        Server server = new Server(serverBO());
        server.setScheduler(scheduler());
        server.setServerEngine(orderServerEngine());
        return server;
    }

    @Bean
    public ServerBO serverBO() {
        return new ServerBO("SZ", (short) 3, "OS-Walker");
    }

    @Bean
    public Scheduler scheduler() {
        List<Runnable> tasks = new ArrayList<>(1);
        tasks.add(serverReporter());
        return new Scheduler(tasks);
    }

    @Bean
    public IServerCommEngine orderServerEngine() {
        return new UCServerCommEngine(omsServerConfig());
    }

    @Bean
    public IMsgEventListener omsMsgListener() {
        Map<Short, IMsgHandler> handlers = new HashMap<>();
        handlers.put((short) 1, new OrderRequestHandler(orderServerService(), orderMsgCodec()));
        return new ServiceMsgListener(handlers, orderMsgCodec());
    }

    @Bean
    public IMsgEventListener amsMsgListener() {
        Map<Short, IMsgHandler> handlers = new HashMap<>();
        handlers.put((short) 2, new GetAccountByIdReplyHandler(orderServerService(), acctMsgCodec));
        return new ServiceMsgListener(handlers, acctMsgCodec);
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public OrderServerService orderServerService() {
        return new OrderServerService(machinePoolFactory(), machinePoolConfig(), accountCenter, orderCenter(), stateMachinePersister());
    }

    @Bean
    public OrderCenter orderCenter() {
        return new OrderCenter(orderMsgCodec());
    }

    @Bean
    public IOrderMsgCodec orderMsgCodec() {
        return new OrderMsgCodec();
    }

    @Bean
    public ServerReporter serverReporter() {
        return new ServerReporter(monitorClientService, serverBO());
    }

    @Bean
    public IServerCommEngineConfig omsServerConfig() {
        ServerCommEngineConfig config = new ServerCommEngineConfig(new Endpoint(), omsMsgListener());
        HashMap<List<IEndpoint>, IMsgEventListener> clientListeners = new HashMap<>();
        clientListeners.put(accountCenter.getEndpoints(), amsMsgListener());
        config.setClientListeners(clientListeners);
        config.setWorkers(10);
        return config;
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

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new InMemoryStateMachinePersist());
    }
}
