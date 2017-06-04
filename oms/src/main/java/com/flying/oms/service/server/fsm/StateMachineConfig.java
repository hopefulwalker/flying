/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
 2017/6/4      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
                                             Add external bean as private final field to simplify the config code.
*/
package com.flying.oms.service.server.fsm;

import com.flying.oms.model.OrderStates;
import com.flying.oms.service.server.AccountCenter;
import com.flying.oms.service.server.OrderCenter;
import com.flying.oms.service.server.fsm.action.SendOrderAction;
import com.flying.oms.service.server.fsm.action.ValidateAccountAction;
import com.flying.oms.service.server.fsm.event.OrderEvents;
import com.flying.oms.service.server.fsm.guard.AccountNormalGuard;
import com.flying.oms.service.server.fsm.guard.NoneAccountGuard;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

@Configuration
public class StateMachineConfig {
    private final AccountCenter accountCenter;
    private final OrderCenter orderCenter;

    public StateMachineConfig(AccountCenter accountCenter, OrderCenter orderCenter) {
        this.accountCenter = accountCenter;
        this.orderCenter = orderCenter;
    }

    @Bean
    @Scope(scopeName = "prototype")
    public StateMachine<OrderStates, OrderEvents> orderStateMachine(BeanFactory factory) throws Exception {
        StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();
        builder.configureConfiguration().withConfiguration()
                .autoStartup(true).machineId(StateMachineConfig.class.getSimpleName()).beanFactory(factory);
        builder.configureStates()
                .withStates()
                .initial(OrderStates.CREATED)
                .states(EnumSet.allOf(OrderStates.class))
                .end(OrderStates.REJECTED).end(OrderStates.SENT);
        builder.configureTransitions()
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.CHECKING_ACCOUNT)
                .event(OrderEvents.OrderRequest).guard(noneAccountGuard())
                .action(validateAccountAction())
                .and()
                .withExternal()
                .source(OrderStates.CHECKING_ACCOUNT).target(OrderStates.SENT)
                .event(OrderEvents.GetAccountByIdReply).guard(accountNormalGuard())
                .action(sendOrderAction());
        return builder.build();
    }

    @Bean
    public NoneAccountGuard noneAccountGuard() {
        return new NoneAccountGuard(accountCenter);
    }

    @Bean
    public AccountNormalGuard accountNormalGuard() {
        return new AccountNormalGuard(accountCenter);
    }

    @Bean
    public ValidateAccountAction validateAccountAction() {
        return new ValidateAccountAction(accountCenter);
    }

    @Bean
    public SendOrderAction sendOrderAction() {
        return new SendOrderAction(orderCenter);
    }
}
