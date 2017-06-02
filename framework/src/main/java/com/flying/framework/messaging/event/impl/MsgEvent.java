/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/5/1      Walker.Zhang     0.3.4        Add static factory method and a common static empty data.
 2017/6/1      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.event.IEventSource;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventInfo;

import java.util.List;

public class MsgEvent extends CommonEvent<IEventSource, IMsgEventInfo> implements IMsgEvent {
    private static final byte[] emptyData = new byte[0];

    private MsgEvent(int id, IEventSource source, IMsgEventInfo info) {
        super(id, source, info);
    }

    public static MsgEvent newInstance(int id, IEventSource source) {
        return new MsgEvent(id, source, new MsgEventInfo(emptyData));
    }

    public static MsgEvent newInstance(int id, IEventSource source, byte[] data) {
        return new MsgEvent(id, source, new MsgEventInfo(data));
    }

    public static MsgEvent newInstance(int id, IEventSource source, byte[] data, List<IEndpoint> froms) {
        return new MsgEvent(id, source, new MsgEventInfo(data, froms));
    }
}