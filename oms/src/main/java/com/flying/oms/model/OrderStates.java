/*
 Created by Walker.Zhang on 2017/5/18.
 Revision History:
 Date          Who              Version      What
 2015/5/18     Walker.Zhang     0.3.6        Revamp the order state machine based on spring-state machine.
*/
package com.flying.oms.model;

public enum OrderStates {
    CREATED((byte) 1),
    CHECKING_ACCOUNT((byte) 2),
    REJECTED((byte) 3),
    SENT((byte) 4),

    UNIDENTIFIED((byte) 15),
    INITIALIZED((byte) 16),
    CONFIRMED((byte) 17),
    CANCELLED((byte) 18),
    FULL_FILLED((byte) 19),
    PARTIAL_FILLED((byte) 110),
    PARTIAL_CANCELLED((byte) 111);

    private final byte value;

    OrderStates(final byte value) {
        this.value = value;
    }

    public static OrderStates get(final byte value) {
        switch (value) {
            case 1:
                return CREATED;
            case 2:
                return CHECKING_ACCOUNT;
            case 3:
                return REJECTED;
            case 4:
                return SENT;
            case 15:
                return UNIDENTIFIED;
            case 16:
                return INITIALIZED;
            case 17:
                return CONFIRMED;
            case 18:
                return CANCELLED;
            case 19:
                return FULL_FILLED;
            case 110:
                return PARTIAL_FILLED;
            case 111:
                return PARTIAL_CANCELLED;
        }
        throw new IllegalArgumentException("Unknown OrderStates:" + value);
    }

    public byte value() {
        return value;
    }
}

