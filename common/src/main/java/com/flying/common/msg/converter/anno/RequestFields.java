/**
 * Created by Walker.Zhang on 2015/4/16.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/16     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.converter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestFields {
    public String value() default "";
}
