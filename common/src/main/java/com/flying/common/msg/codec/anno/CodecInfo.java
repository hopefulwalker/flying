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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodecInfo {
    short GET_HEADER_INFO = 1;
    short ENCODE_MSG = 101;
    short GET_BODY_DECODER = 102;
    short DECODE_MSG = 202;

    short type() default 0;

    /**
     * @return full name of headerClass which used by requestClass and replyClass
     */
    String headerDecoderClass() default "";

    /**
     * @return full name of headerClass which used by requestClass and replyClass
     */
    String headerEncoderClass() default "";

    /**
     * @return enocder of request.
     */
    String bodyEncoderClass() default "";

    /**
     * @return enocder of request.
     */
    String bodyDecoderClass() default "";

    /**
     * @return full name of msg type class
     */
    String msgTypeClass() default "";

    /**
     * @return fields of dto.
     */
    String fields() default "";

    /**
     * @return fields of dto.
     */
    String elementDecoderClass() default "";
}