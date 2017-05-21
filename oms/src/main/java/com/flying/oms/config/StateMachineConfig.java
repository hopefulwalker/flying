/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.config;

import com.flying.ams.service.IAccountService;
import com.flying.oms.service.server.fsm.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Configuration
public class StateMachineConfig {
    @Bean
    @Scope(scopeName = "prototype")
    public StateMachine<OrderStates, OrderEvents> orderStateMachine(BeanFactory factory, IAccountService accountService) throws Exception {
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
                .source(OrderStates.CREATED).target(OrderStates.SENT)
                .event(OrderEvents.OrderRequest).action(placeOrderAction(accountService));
        return builder.build();
    }

    @Bean
    public SpringStateMachineActionLink<OrderStates, OrderEvents> placeOrderAction(IAccountService accountService) {
        List<Action<OrderStates, OrderEvents>> actions = new ArrayList<>(2);
        actions.add(validateAccountAction(accountService));
        actions.add(sendOrderAction());
        return new SpringStateMachineActionLink<>(actions);
    }

    @Bean
    public ValidateAccountAction validateAccountAction(IAccountService accountService) {
        ValidateAccountAction action = new ValidateAccountAction();

        action.setAccountService(accountService);
        return action;
    }

    @Bean
    public SendOrderAction sendOrderAction() {
        return new SendOrderAction();
    }

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new InMemoryStateMachinePersist());
    }
}
