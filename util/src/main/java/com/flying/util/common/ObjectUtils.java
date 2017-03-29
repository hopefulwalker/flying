/*
 Created by Walker.Zhang on 2015/3/3.
 Revision History:
 Date          Who              Version      What
 2015/3/3     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.util.common;

public class ObjectUtils {
    private ObjectUtils() {
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
    }
}
