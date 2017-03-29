/**
 * Created by Walker.Zhang on 2015/4/6.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/6     Walker.Zhang     0.1.0        Created.
 */
package com.flying.oms.service;

import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;

public class OrderServiceException extends ServiceException {

    public static final int FAILED_TO_PLACE_ORDER = -3001;

    public static final int FAILED_TO_SEND_ORDER_REQUEST = -3002;

    public static final int FAILED_TO_RECV_ORDER_REPLY = -3003;

    /**
     * Constructs a new OrderServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public OrderServiceException(ServiceException cause) {
        super(cause.getCode(), cause);
    }

    /**
     * Constructs a new OrderServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code the exception code for the exception.
     */
    public OrderServiceException(int code) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ORDER * EXCEPTION_DIGITS + code : code - IServiceType.ORDER * EXCEPTION_DIGITS));
    }

    /**
     * Constructs a new OrderServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public OrderServiceException(int code, String message) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ORDER * EXCEPTION_DIGITS + code : code - IServiceType.ORDER * EXCEPTION_DIGITS), message);
    }

    /**
     * Constructs a new OrderServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code  the exception code for the exception.
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public OrderServiceException(int code, Throwable cause) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ORDER * EXCEPTION_DIGITS + code : code - IServiceType.ORDER * EXCEPTION_DIGITS), cause);
    }

    /**
     * Constructs a new OrderServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *                is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public OrderServiceException(int code, String message, Throwable cause) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ORDER * EXCEPTION_DIGITS + code : code - IServiceType.ORDER * EXCEPTION_DIGITS), message, cause);
    }
}
