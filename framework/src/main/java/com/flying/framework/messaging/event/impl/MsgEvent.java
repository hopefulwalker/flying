/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event.impl;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.messaging.engine.IEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventInfo;

public class MsgEvent extends CommonEvent<IEngine, IMsgEventInfo> implements IMsgEvent {
    public MsgEvent(int id, IEngine source, IMsgEventInfo info) {
        super(id, source, info);
    }
}