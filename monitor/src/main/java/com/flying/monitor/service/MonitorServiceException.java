/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 */
package com.flying.monitor.service;

import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;

public class MonitorServiceException extends ServiceException {

    public static final short FAILED_TO_REGISTER = -3001;

    /**
     * Constructs a new MonitorServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public MonitorServiceException(ServiceException cause) {
        super(cause.getCode(), cause);
    }

    /**
     * Constructs a new MonitorServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code the exception code for the exception.
     */
    public MonitorServiceException(int code) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.MONITOR * EXCEPTION_DIGITS + code : code - IServiceType.MONITOR * EXCEPTION_DIGITS));
    }

    /**
     * Constructs a new MonitorServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public MonitorServiceException(int code, String message) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.MONITOR * EXCEPTION_DIGITS + code : code - IServiceType.MONITOR * EXCEPTION_DIGITS), message);
    }

    /**
     * Constructs a new MonitorServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code  the exception code for the exception.
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public MonitorServiceException(int code, Throwable cause) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.MONITOR * EXCEPTION_DIGITS + code : code - IServiceType.MONITOR * EXCEPTION_DIGITS), cause);
    }
}