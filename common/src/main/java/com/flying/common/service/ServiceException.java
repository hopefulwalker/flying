/**
 * Created by Walker.Zhang on 2015/3/31.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/31     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.service;

import com.flying.util.exception.LoggableException;

/**
 * Base exception class for service.
 * About code definition:
 * 1. 0 means success
 * 2. <0, means system failure.
 * 3. >0, means business logic failure.
 * 4. >=-1000, preserves for common system failure, usually define in IReturnCode.
 * 5. <=1000, preserves for common business logic failure, it should be define in this files.
 * 6. <-1000, define by exception class themselves.
 * 7. >1000, define by exception class themselves.
 * <p>
 * for service exception:
 * 8. [-3000, -1001] preserve in this files.
 * 9. [1001, 3000] preserve in this files.
 */
public class ServiceException extends LoggableException {

    public static final int EXCEPTION_DIGITS = 10000;

    public static final int ABS_MIN_CODE = 1001;

    public static final int ABS_MAX_CODE = 3000;

    public static final int FAILED_TO_LOAD_DATA_FROM_DB_SERVER = -1001;

    public static final int FAILED_TO_LOAD_DATA_FROM_MSG_SERVER = -1002;

    public static final int FAILED_TO_INITIALIZE_SERVICE = -1003;

    public static final int FAILED_TO_CLEAR_KEYEDPOOL = -1004;

    public static final int FAILED_TO_BUILD_REQUEST = -1005;

    public static final int FAILED_TO_BUILD_REPLY = -1006;

    public static final int MISMATCH_REPLY = -1007;

    public static final int SERVICE_NOT_READY = -1008;

    public static final int UNSUPPORTED_SERVICE = 1001;

    /**
     * Constructs a new ServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code the exception code for the exception.
     */
    public ServiceException(int code) {
        super(code);
    }

    /**
     * Constructs a new ServiceException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ServiceException(int code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new ServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code  the exception code for the exception.
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ServiceException(int code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Constructs a new ServiceException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *                is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ServiceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String toString() {
        return super.toString() + ";Code=[" + getCode() + "]" + ";CodeInfo=[" + SECodeAnalyzer.analyze(getCode()) + "]";
    }
}