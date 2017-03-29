/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.common.msg.handler;

import com.flying.common.msg.converter.IMsgConverter;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEventResult;

import java.util.Map;

public class ServiceMsgListener implements IMsgEventListener {
    private Map<Short, IMsgHandler> handlers;
    private IMsgConverter msgConverter;

    public ServiceMsgListener(Map<Short, IMsgHandler> handlers, IMsgConverter msgConverter) {
        this.handlers = handlers;
        this.msgConverter = msgConverter;
    }

    public IMsgHandler getHandler(short msgType) {
        return handlers.get(msgType);
    }

    @Override
    public IMsgEventResult onEvent(IMsgEvent event) {
        IMsgEventResult result = null;
        byte[] msg = event.getEventInfo().getByteArray();
        IMsgHandler handler = getHandler(msgConverter.getMsgType(msg));
        if (handler != null) {
            result = new MsgEventResult(handler.handle(msg));
        }
        return result;
    }
}
