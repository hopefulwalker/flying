/*
  File: LoggableRuntimeException.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2014.12.25   walker  1.0.0    Create this file.
 */
package com.flying.util.exception;

import java.util.Date;

/**
 * <p>
 * Description: LoggableRuntimeException is a subclass of Exception and can be used as the base exception to throw from
 * utility tier whenever a exception occurs. It keeps track of the exception is logged or not and also stores the unique
 * id, so that it can be carried all along to the client tier and displayed to the end user. The end user can call up
 * the customer support using this number.
 */

public class LoggableRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    // logged flag , creation time, exception code.
    private boolean logged = false;
    private Date createTime = new Date();
    private short code = 0;

    // Constructors

    /**
     * Constructs a new LoggableRuntimeException with the specified code and detail message. The cause is not initialized, and my
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code the exception code for the exception.
     */
    public LoggableRuntimeException(short code) {
        super();
        this.code = code;
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified detail message. The cause is not initialized, and
     * may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public LoggableRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
     * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers for other
     * throwables (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *              value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public LoggableRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified code and detail message. The cause is not
     * initialized, and my subsequently be initialized by a call to {@link #initCause}.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public LoggableRuntimeException(short code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public LoggableRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code  the exception code for the exception.
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public LoggableRuntimeException(short code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Constructs a new LoggableRuntimeException with the specified code,detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param code    the exception code for the exception.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public LoggableRuntimeException(short code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    // Accessor and mutator

    /**
     * @return true when the exception had been logged.
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * Set the log flag to true.
     */
    public void setLogged() {
        logged = true;
    }

    /**
     * @return the exception's creation time.
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @return the exception's code.
     */
    public short getCode() {
        return code;
    }
}