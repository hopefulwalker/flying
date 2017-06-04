/*
 Created by Walker.Zhang on 2017/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
                                             Add external bean as private final field to simplify the config code.
*/
package com.flying.oms.config;

import com.flying.ams.msg.codec.AccountMsgCodec;
import com.flying.ams.msg.codec.IAccountMsgCodec;
import com.flying.common.service.IEndpointFactory;
import com.flying.oms.service.server.AccountCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
    @Bean(initMethod = "init")
    public AccountCenter accountCenter(IEndpointFactory factory) {
        return new AccountCenter("SZ", factory, accountMsgCodec());
    }

    @Bean
    public IAccountMsgCodec accountMsgCodec() {
        return new AccountMsgCodec();
    }
}