/*
 Created by Walker.Zhang on 2015/5/30.
 Revision History:
 Date          Who              Version      What
 2015/5/30     Walker.Zhang     0.1.0        Created.
 2015/6/15     Walker.Zhang     0.2.0        Simplify the implementation of setEndpoints because Endpoint overrides hashCode and equals.
 2017/5/1      Walker.Zhang     0.3.4        Redefine the message event ID and refactor the engine implementation.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.engine.IClientCommEngine;
import com.flying.framework.messaging.engine.IClientCommEngineConfig;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PooledClientEngineFactory implements PooledObjectFactory<IClientCommEngine> {
    private IClientCommEngineConfig config;

    public PooledClientEngineFactory(IClientCommEngineConfig config) {
        this.config = config;
    }

    @Override
    public PooledObject<IClientCommEngine> makeObject() throws Exception {
        IClientCommEngine engine = new ClientCommEngine(config);
        engine.start();
        return new DefaultPooledObject<>(engine);
    }

    @Override
    public void destroyObject(PooledObject<IClientCommEngine> pooledObject) throws Exception {
        pooledObject.getObject().stop();
    }

    @Override
    public boolean validateObject(PooledObject<IClientCommEngine> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<IClientCommEngine> pooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<IClientCommEngine> pooledObject) throws Exception {
        if (config != pooledObject.getObject().getConfig()) pooledObject.getObject().setConfig(config);
    }
}
