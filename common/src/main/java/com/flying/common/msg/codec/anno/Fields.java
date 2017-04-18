/*
 Created by Walker.Zhang on 2015/4/16.
 Revision History:
 Date          Who              Version      What
 2015/4/16     Walker.Zhang     0.1.0        Created.
 2017/4/14     Walker.Zhang     0.3.2        Refactor to support SBE 1.6.2.
*/
package com.flying.common.msg.codec.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fields {
    String value() default "";

    String elementEncoderClass() default "";

    String SPLIT_CHAR = ",";
}