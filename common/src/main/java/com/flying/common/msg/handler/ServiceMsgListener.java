/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
2015/2/24     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.common.msg.handler;

import com.flying.common.msg.codec.IMsgCodec;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventListener;
import com.flying.framework.messaging.event.IMsgEventResult;

import java.util.Map;

public class ServiceMsgListener implements IMsgEventListener {
    private Map<Short, IMsgHandler> handlers;
    private IMsgCodec msgCodec;

    public ServiceMsgListener(Map<Short, IMsgHandler> handlers, IMsgCodec msgCodec) {
        this.handlers = handlers;
        this.msgCodec = msgCodec;
    }

    public IMsgHandler getHandler(short msgType) {
        return handlers.get(msgType);
    }

    @Override
    public IMsgEventResult onEvent(IMsgEvent event) {
        IMsgEventResult result = null;
        IMsgHandler handler = getHandler(msgCodec.getMsgType(event.getInfo().getBytes()));
        if (handler != null) {
            result = handler.handle(event);
        }
        return result;
    }
}
