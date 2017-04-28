/*
 Created by Walker.Zhang on 2015/2/23.
 Revision History:
 Date          Who              Version      What
 2015/2/23     Walker.Zhang     0.1.0        Created.

 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

public class ZMQUCServerEngine extends AbstractZMQServerEngine {
    private static final Logger logger = LoggerFactory.getLogger(ZMQUCServerEngine.class);

    public ZMQUCServerEngine(IEndpoint listenEndpoint) {
        super(listenEndpoint);
    }

    @Override
    public void run() {
        try {
            ZMQ.Socket frontend = getContext().createSocket(ZMQ.ROUTER);
            // charset == UTF-8
            // client side should also using UTF-8, currently ClientEngine using ZFrame to build message, and
            // ZFrame using UTF-8 as default charset for string.
            frontend.setIdentity(getListenEndpoint().asString().getBytes("UTF-8"));
            frontend.bind(getListenEndpoint().asString());
            ZMQ.Socket backend = getContext().createSocket(ZMQ.DEALER);
            backend.bind(getWorkerURL());
            for (int i = 0; i < getWorkers(); i++) {
                List<IEndpoint> froms = new ArrayList<>(1);
                froms.add(new Endpoint(getWorkerURL()));
                Dispatcher dispatcher = new Dispatcher(this, ZContext.shadow(getContext()));
                dispatcher.addMsgEventListener(froms, ZMQ.DEALER, false, getMsgEventListener());
                getThreadPool().submit(dispatcher);
//                getThreadPool().submit(new ZMQWorker(this));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("ZMQ Service is started, listening to: " + getListenEndpoint().asString());
            }
            ZMQ.proxy(frontend, backend, null);
        } catch (Exception e) {
            logger.error("ZMQ Service wrong exit, stop engine now", e);
            stop();
        }
    }
}
