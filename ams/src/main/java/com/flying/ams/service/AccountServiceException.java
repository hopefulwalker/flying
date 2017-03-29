/**
 * Created by Walker.Zhang on 2015/3/27.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/27     Walker.Zhang     0.1.0        Created.
 */
package com.flying.ams.service;

import com.flying.common.service.IServiceType;
import com.flying.common.service.ServiceException;

public class AccountServiceException extends ServiceException {

    public static final int ACCOUNT_NOT_EXISTS = 3001;

    public static final int ACCOUNT_NOT_NORMAL = 3002;

    /**
     * Constructs a new AccountServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AccountServiceException(ServiceException cause) {
        super(cause.getCode(), cause);
    }

    /**
     * Constructs a new AccountServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code the exception code for the exception.
     */
    public AccountServiceException(int code) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ACCOUNT * EXCEPTION_DIGITS + code : code - IServiceType.ACCOUNT * EXCEPTION_DIGITS));
    }

    /**
     * Constructs a new AccountServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public AccountServiceException(int code, String message) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ACCOUNT * EXCEPTION_DIGITS + code : code - IServiceType.ACCOUNT * EXCEPTION_DIGITS), message);
    }

    /**
     * Constructs a new AccountServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code  the exception code for the exception.
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AccountServiceException(int code, Throwable cause) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ACCOUNT * EXCEPTION_DIGITS + code : code - IServiceType.ACCOUNT * EXCEPTION_DIGITS), cause);
    }

    /**
     * Constructs a new AccountServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *                is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AccountServiceException(int code, String message, Throwable cause) {
        super((Math.abs(code) >= EXCEPTION_DIGITS) ? code :
                ((code > 0) ? IServiceType.ACCOUNT * EXCEPTION_DIGITS + code : code - IServiceType.ACCOUNT * EXCEPTION_DIGITS), message, cause);
    }
}
