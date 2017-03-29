/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
2015/2/23     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.engine;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.event.IMsgEventListener;

public interface IServerEngine extends IEngine {
    public IMsgEventListener getMsgEventListener();

    public void setMsgEventListener(IMsgEventListener msgEventListener);

    public IEndpoint getListenEndpoint();

    public void setListenEndpoint(IEndpoint listenEndpoint);

    public int getWorkers();

    public void setWorkers(int workers);
}
