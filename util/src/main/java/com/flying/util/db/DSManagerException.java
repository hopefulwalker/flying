/*
  File: DSManagerException.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.4     walker  0.1.0    Create this file.
 */
package com.flying.util.db;

import com.flying.util.exception.LoggableException;

public final class DSManagerException extends LoggableException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new DSManagerException with the specified detail message. The cause is not initialized, and may
	 * subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @param message
	 *          the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */
	public DSManagerException(String message) {
		super(message);
	}

	/**
	 * Constructs a new DSManagerException with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
	 * exception's detail message.
	 *
	 * @param message
	 *          the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause
	 *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
	 *          is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @since 1.4
	 */
	public DSManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new DSManagerException with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
	 * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers for other throwables
	 * (for example, {@link java.security.PrivilegedActionException}).
	 *
	 * @param cause
	 *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
	 *          is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @since 1.4
	 */
	public DSManagerException(Throwable cause) {
		super(cause);
	}
}
