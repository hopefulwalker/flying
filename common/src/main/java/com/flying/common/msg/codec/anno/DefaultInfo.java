/*
 Created by Walker.Zhang on 2015/4/16.
 Revision History:
 Date          Who              Version      What
 2017/4/18     Walker.Zhang     0.3.2        Refactor for easing the interface definition.
*/
package com.flying.common.msg.codec.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultInfo {
    /**
     * @return full package name for header codec.
     */
    String headerCodecPackage() default "";

    /**
     * @return full class name of header encoder.
     */
    String headerEncoderClass() default "";

    /**
     * @return full class name of header decoder.
     */
    String headerDecoderClass() default "";
    /**
     * @return full package name for body codec.
     */
    String bodyCodecPackage() default "";

    /**
     * @return full package name for msg type class
     */
    String msgTypePackage() default "";
    /**
     * @return full class name for msg type.
     */
    String msgTypeClass() default "";

}
