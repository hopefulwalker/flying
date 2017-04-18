/*
 Created by Walker.Zhang on 2015/4/16.
 Revision History:
 Date          Who              Version      What
 2015/4/16     Walker.Zhang     0.1.0        Created.
 2017/4/14     Walker.Zhang     0.3.2        Refactor to support SBE 1.6.2.
*/
package com.flying.common.msg.codec.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    /**
     * @return name
     */
    public String value() default "";
}
