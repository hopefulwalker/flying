/**
 * Created by Walker.Zhang on 2015/5/25.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/25     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.codec.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReplyFields {
    public String value() default "";
    /**
     * @return full name of dto class
     */
    public String msgDTOClass() default "";
}
