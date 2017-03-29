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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CnvInfo {
    /**
     * @return conversion type
     * value = 0, default type, unsupported.
     * value = 1, get information from headerClass
     * value = 101, requestClass -> bytes
     * value = 102, bytes -> requestClass
     * value = 201, replyClass -> bytes
     * value = 202, bytes -> replyClass
     */
    public short type() default 0;

    /**
     * @return full name of headerClass which used by requestClass and replyClass
     */
    public String headerClass() default "";

    /**
     * @return full name of requestClass message
     */
    public String requestClass() default "";

    /**
     * @return full name of replyClass message
     */
    public String replyClass() default "";

    /**
     * @return full name of msg type class
     */
    public String msgTypeClass() default "";

    /**
     * @return replyClass's object to build dto.
     */
    public String msgDTOClass() default "";

    /**
     * @return full name of dto class
     */
    public String dtoClass() default "";

    /**
     * @return fields of dto.
     */
    public String dtoFields() default "";
}