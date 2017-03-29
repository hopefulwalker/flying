/**
 * Created by Walker.Zhang on 2015/3/23.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/23     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.model;

import com.flying.framework.fsm.impl.DefaultState;

public class OrderState extends DefaultState {
    public static final byte UNIDENTIFIED = 0;
    public static final byte CREATED = 1;
    public static final byte INITIALIZED = 2;
    public static final byte REJECTED = 3;
    public static final byte SENT = 4;
    public static final byte CONFIRMED = 5;
    public static final byte CANCELLED = 6;
    public static final byte FULL_FILLED = 7;
    public static final byte PARTIAL_FILLED = 8;
    public static final byte PARTIAL_CANCELLED = 9;

    public OrderState(byte id) {
        super(id);
    }
}
