/*
  File: UKGenException.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    Create this file.
 */
package com.flying.util.uk;

import com.flying.util.exception.LoggableRuntimeException;

public class UKGenException extends LoggableRuntimeException {

	private static final long serialVersionUID = 1L;

	public UKGenException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
