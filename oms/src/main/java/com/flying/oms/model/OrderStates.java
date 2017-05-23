/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.model;

public enum OrderStates {
    CREATED((byte) 1),
    REJECTED((byte) 2),
    SENT((byte) 3),

    UNIDENTIFIED((byte) 4),
    INITIALIZED((byte) 5),
    CONFIRMED((byte) 6),
    CANCELLED((byte) 7),
    FULL_FILLED((byte) 8),
    PARTIAL_FILLED((byte) 9),
    PARTIAL_CANCELLED((byte) 10);

    private final byte value;

    OrderStates(final byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public static OrderStates get(final byte value) {
        switch (value) {
            case 1:
                return CREATED;
            case 2:
                return REJECTED;
            case 3:
                return SENT;
            case 4:
                return UNIDENTIFIED;
            case 5:
                return INITIALIZED;
            case 6:
                return CONFIRMED;
            case 7:
                return CANCELLED;
            case 8:
                return FULL_FILLED;
            case 9:
                return PARTIAL_FILLED;
            case 10:
                return PARTIAL_CANCELLED;
        }
        throw new IllegalArgumentException("Unknown order state:" + value);
    }
}

