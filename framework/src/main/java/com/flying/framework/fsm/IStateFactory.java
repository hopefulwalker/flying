/**
 Created by Walker.Zhang on 2015/2/15.
 Revision History:
 Date          Who              Version      What
 2015/2/15      Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.fsm;

public interface IStateFactory<T extends IState> {
    public T getState(byte id);
}
