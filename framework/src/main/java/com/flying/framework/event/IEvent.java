/*
 Created by Walker.Zhang on 2015/2/5.
 Revision History:
 Date          Who              Version      What
 2015/2/5      Walker.Zhang     0.1.0        Created.
 2015/2/23     Walker.Zhang     0.1.1        Refactor this class.
 2017/4/20     Walker.Zhang     0.3.2        Add timestamp.
                                             Change the method name to be simpler.
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
    public S getSource();

    /**
     * @return The object to describe the event information.
     */
    public I getInfo();

    /**
      * @return The time at which the event was created, in milliseconds.
     */
    public long getTimeStamp();
}
