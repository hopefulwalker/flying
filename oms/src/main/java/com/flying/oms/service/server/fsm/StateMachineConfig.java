/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.service.server.fsm;

import com.flying.framework.fsm.IAction;
import com.flying.framework.fsm.IGuard;
import com.flying.framework.fsm.SpringStateMachineActionLink;
import com.flying.framework.fsm.SpringStateMachineGuardLink;
import com.flying.oms.model.OrderBO;
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
    public StateMachine<OrderStates, OrderEvents> orderStateMachine(BeanFactory factory, AccountAccessor accountAccessor) throws Exception {
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
                .event(OrderEvents.OrderRequest).guard(noneAccountGuard(accountAccessor))
                .action(checkAccountAction(accountAccessor));

        return builder.build();
    }

    @Bean
    public SpringStateMachineGuardLink<OrderStates, OrderEvents, OrderBO> noneAccountGuard(AccountAccessor accountAccessor) {
        List<IGuard<OrderBO>> guards = new ArrayList<>(1);
        guards.add(validateAccountGuard(accountAccessor));
        return new SpringStateMachineGuardLink<>(guards);
    }

    @Bean
    public SpringStateMachineActionLink<OrderStates, OrderEvents, OrderBO> checkAccountAction(AccountAccessor accountAccessor) {
        List<IAction<OrderBO>> actions = new ArrayList<>(1);
        actions.add(validateAccountAction(accountAccessor));
        return new SpringStateMachineActionLink<>(actions);
    }

    @Bean
    public ValidateAccountAction validateAccountAction(AccountAccessor accessor) {
        ValidateAccountAction action = new ValidateAccountAction();
        action.setAccountAccessor(accessor);
        return action;
    }

    @Bean
    public ValidateAccountGuard validateAccountGuard(AccountAccessor accessor) {
        ValidateAccountGuard action = new ValidateAccountGuard();
        action.setAccountAccessor(accessor);
        return action;
    }

    @Bean
    public SendOrderGuard sendOrderGuard() {
        return new SendOrderGuard();
    }

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, OrderStates> stateMachinePersister() {
        return new DefaultStateMachinePersister<>(new InMemoryStateMachinePersist());
    }
}
