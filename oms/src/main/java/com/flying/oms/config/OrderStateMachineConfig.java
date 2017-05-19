/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.config;

import com.flying.oms.model.OrderStates;
import com.flying.oms.msg.OrderEvents;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

@Configuration
public class OrderStateMachineConfig {
    @Bean
    @Scope(scopeName = "prototype")
    public StateMachine<OrderStates, OrderEvents> orderStateMachine(BeanFactory factory) throws Exception {
        StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();
        builder.configureConfiguration().withConfiguration()
                .autoStartup(true).machineId(OrderStateMachineConfig.class.getSimpleName()).beanFactory(factory);
        builder.configureStates()
                .withStates()
                .initial(OrderStates.CREATED)
                .states(EnumSet.allOf(OrderStates.class))
                .end(OrderStates.REJECTED).end(OrderStates.SENT);
        builder.configureTransitions()
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.REJECTED)
                .event(OrderEvents.OrderRequest).action(action())
                .and()
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.SENT)
                .event(OrderEvents.OrderRequest).action(action());
        return builder.build();
    }

    @Bean
    public Action<OrderStates, OrderEvents> action() {
        return new Action<OrderStates, OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates, OrderEvents> context) {
                System.out.println(context.getEvent());
            }
        };
    }
}
