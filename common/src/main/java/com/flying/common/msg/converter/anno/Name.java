/**
 * Created by Walker.Zhang on 2015/4/16.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/16     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.converter.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    /**
     * @return name
     */
    public String value() default "";
}
