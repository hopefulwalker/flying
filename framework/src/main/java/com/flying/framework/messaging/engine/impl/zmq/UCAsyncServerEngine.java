/*
 Created by Walker.Zhang on 2017/5/2.
 Revision History:
 Date          Who              Version      What
 2017/5/2      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
*/

package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.event.IMsgEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class UCAsyncServerEngine extends AbstractAsyncServerEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncEngine.class);

    public UCAsyncServerEngine(IEndpoint listenEndpoint) {
        super(listenEndpoint);
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
