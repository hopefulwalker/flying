/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.config;

import com.flying.ams.model.AccountBO;
import com.flying.ams.msg.codec.AccountMsgCodec;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.ams.service.IAccountService;
import com.flying.ams.service.server.AccountDBLoader;
import com.flying.ams.service.server.AccountServerService;
import com.flying.monitor.service.IMonitorService;
import com.flying.monitor.service.client.MonitorEndpointFactory;
import com.flying.oms.service.server.fsm.AccountAccessor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AccountServiceConfig {

    @Bean(initMethod = "init")
    public AccountAccessor accountAccessor(IMonitorService monitorService) {
        return new AccountAccessor("SZ", endpointFactory(monitorService), accountMsgCodec());
    }

    @Bean
    public IAccountMsgCodec accountMsgCodec() {
        return new AccountMsgCodec();
    }

    @Bean
    public Map<Long, AccountBO> accountCache() {
        return new HashMap<>();
    }

    @Bean
    public AccountDBLoader accountDBLoader() {
        return new AccountDBLoader();
    }

    @Bean
    public IAccountService accountServerService() {
        return new AccountServerService(accountCache(), accountDBLoader());
    }

    @Bean
    public GenericObjectPoolConfig enginePoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(1);
        return config;
    }

    @Bean
    public MonitorEndpointFactory endpointFactory(IMonitorService monitorService) {
        return new MonitorEndpointFactory(monitorService);
    }

//    @Bean(initMethod = "init", destroyMethod = "close")
//    public IAccountService accountClientService(IMonitorService monitorService) {
//        return new AccountClientService("SZ", endpointFactory(monitorService), enginePoolConfig(), accountMsgCodec());
//    }
}