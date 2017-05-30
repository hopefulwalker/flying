/*
 Created by Walker.Zhang on 2017/5/22.
 Revision History:
 Date          Who              Version      What
 2015/5/22     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.framework.fsm;

public interface IGuard<V> {
    boolean evaluate(V object);
}
