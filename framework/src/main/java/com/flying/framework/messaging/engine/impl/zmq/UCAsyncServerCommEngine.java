/*
 Created by Walker.Zhang on 2017/5/2.
 Revision History:
 Date          Who              Version      What
 2017/5/2      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/

package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

public class UCAsyncServerCommEngine extends AbstractAsyncServerCommEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncCommEngine.class);

    public UCAsyncServerCommEngine(ICommEngineConfig config) {
        super(config);
    }

    @Override
    public void run() {
        try {
            ZMQ.Socket frontend = getContext().createSocket(ZMQ.ROUTER);
            // charset == UTF-8
            frontend.setIdentity(getListenEndpoint().asString().getBytes("UTF-8"));
            frontend.bind(getListenEndpoint().asString());
            ZMQ.proxy(frontend, getPipe(), null);
        } catch (Exception e) {
            logger.error("ZMQ Service wrong exit, stop engine now", e);
            stop();
        }
    }
}
