/**
 Created by Walker.Zhang on 2015/2/5.
 Revision History:
 Date          Who              Version      What
 2015/2/5      Walker.Zhang     0.1.0        Created.
 2015/2/23     Walker.Zhang     0.1.1        Refactor this class.
 */
package com.flying.framework.event;

public interface IEvent<S extends IEventSource, I extends IEventInfo> {
    /**
     * @return the name of the event.
     */
    public int getId();

    /**
     * @return The object on which the Event initially occurred
     */
    public S getEventSource();

    /**
     * @return The object to describe the event information.
     */
    public I getEventInfo();
}
