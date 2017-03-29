/*
  File: CypherException.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.4     walker  0.1.0    Create this file.
 */
package com.flying.util.crypto;

import com.flying.util.exception.LoggableException;

public class CypherException extends LoggableException {
	private static final long serialVersionUID = 1L;

	// Constructors
	/**
	 * Constructs a new CypherException with the specified detail message and cause.
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
	public CypherException(String message, Throwable cause) {
		super(message, cause);
	}
}
