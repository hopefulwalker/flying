/*
 Created by Walker.Zhang on 2015/3/6.
 Revision History:
 Date          Who              Version      What
 2015/3/6     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.common;

/**
 * Define the common return for all system.
 * It is divided into two types.
 * 1. 0 means success.
 * 2. negative number means system failure.
 * 3. all constance here should be [-1000, 0]
 * we didn't use positive number, because it should be defined in the exception class and belongs to different service.
 */
public class IReturnCode {
    public static final int ABS_MIN_CODE = 0;

    public static final int ABS_MAX_CODE = 1000;

    public static final int SUCCESS = 0;

    public static final int UNKNOWN_FAILURE = -1;

    public static final int TIMEOUT = -2;

    public static final int UNSUPPORTED_ENCODING = -3;

    public static final int INVALID_EVENT = -4;
}