/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
 2015/2/23     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.event;

public class CommonEvent<S extends IEventSource, I extends IEventInfo> implements IEvent<S, I> {
    private int id;
    private S source;
    private I info;
    private long timestamp;

    public CommonEvent(int id, S source, I info) {
        this.id = id;
        this.source = source;
        this.info = info;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * @return The time at which the event was created, in milliseconds.
     */
    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public I getInfo() {
        return info;
    }
}