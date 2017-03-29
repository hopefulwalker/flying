/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.event;

import com.flying.framework.event.IEventListener;

/**
 * This class is for Convenience. we can use IEventListener<IMsgEvent, IMsgEventResult> directly
 * because the IEvent is a generic interface.
 */
public interface IMsgEventListener extends IEventListener<IMsgEvent, IMsgEventResult> {
}
