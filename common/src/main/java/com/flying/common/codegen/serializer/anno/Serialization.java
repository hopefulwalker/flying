/**
 * Created by Walker.Zhang on 2015/7/21.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/21     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.codegen.serializer.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serialization {
    /**
     * @return sequence id for this field
     */
    public int id();

    /**
     * @return the number of bytes used to represent this field. only used for String.
     */
    public int bytes() default 0;
}