/*
 Created by Walker.Zhang on 2017/5/23.
 Revision History:
 Date          Who              Version      What
 2015/5/23     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.model;

public enum OrderEvents {
    OrderRequest((short) 1),
    OrderReply((short) 2);

    private final short value;

    OrderEvents(final short value) {
        this.value = value;
    }

    public short value() {
        return value;
    }

    public static OrderEvents get(final short value) {
        switch (value) {
            case 1:
                return OrderRequest;
            case 2:
                return OrderRequest;
        }
        throw new IllegalArgumentException("Unknown OrderEvents:" + value);
    }
}
