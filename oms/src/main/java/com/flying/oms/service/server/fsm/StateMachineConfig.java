/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.ams.service.IAccountService;
import com.flying.framework.fsm.IGuard;
import com.flying.framework.fsm.SpringStateMachineGuardLink;
import com.flying.oms.model.OrderBO;
import com.flying.oms.model.OrderEvents;
import com.flying.oms.model.OrderStates;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
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
                .event(OrderEvents.OrderRequest).guard(placeOrderGuard(accountService));
        return builder.build();
    }

    @Bean
    public SpringStateMachineGuardLink<OrderStates, OrderEvents, OrderBO> placeOrderGuard(IAccountService accountService) {
        List<IGuard<OrderBO>> guards = new ArrayList<>(2);
        guards.add(validateAccountGuard(accountService));
        guards.add(sendOrderAction());
        return new SpringStateMachineGuardLink<>(guards);
    }

    @Bean
    public ValidateAccountGuard validateAccountGuard(IAccountService accountService) {
        ValidateAccountGuard action = new ValidateAccountGuard();
        action.setAccountService(accountService);
        return action;
    }

    @Bean
    public SendOrderGuard sendOrderAction() {
        return new SendOrderGuard();
    }

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new InMemoryStateMachinePersist());
    }
}
