/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2017/5/1      Walker.Zhang     0.3.4        Add static factory method and a common static empty data.
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.messaging.engine.IEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventInfo;

public class MsgEvent extends CommonEvent<IEngine, IMsgEventInfo> implements IMsgEvent {
    private static final byte[] emptyData = new byte[0];

    private MsgEvent(int id, IEngine source, IMsgEventInfo info) {
        super(id, source, info);
    }

    public static MsgEvent newInstance(int id, IEngine source) {
        return new MsgEvent(id, source, new MsgEventInfo(emptyData));
    }

    public static MsgEvent newInstance(int id, IEngine source, byte[] data) {
        return new MsgEvent(id, source, new MsgEventInfo(data));
    }
}