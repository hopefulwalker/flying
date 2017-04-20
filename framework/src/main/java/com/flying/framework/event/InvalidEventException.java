/*
 Created by Walker.Zhang on 2015/2/5.
 Revision History:
 Date          Who              Version      What
 2015/2/5      Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.event;


import com.flying.util.exception.LoggableException;

public final class InvalidEventException extends LoggableException {
    private static final long serialVersionUID = 1L;
    private IEvent event;

    /**
     * Constructs a new InvalidEventException with <code>IEvent</code> as its detail message.
     */
    public InvalidEventException(String message, IEvent event) {
        super(message);
        this.event = event;
    }

    /**
     * @return the invalid event.
     */
    public IEvent getEvent(){
        return event;
    }
}