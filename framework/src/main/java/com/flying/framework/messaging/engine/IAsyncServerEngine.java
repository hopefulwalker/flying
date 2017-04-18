/*
 Created by Walker on 2017/4/12.
 Revision History:
 Date          Who              Version      What
 2017/4/12     Walker           0.3.0        Created.
                                             Refactor to support multi-communication library, such as netty.
*/
package com.flying.framework.messaging.engine;

public interface IAsyncServerEngine extends IEngine {
    /**
     * @return the config information.
     */
    IEngineConfig getConfig();
}
