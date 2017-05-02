/**
 * Created by Walker.Zhang on 2015/5/30.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/30     Walker.Zhang     0.1.0        Created.
 * 2015/6/15     Walker.Zhang     0.2.0        Simplify the implementation of setEndpoints because Endpoint overrides hashCode and equals.
 */
package com.flying.framework.messaging.engine.impl.zmq;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.List;

public class PooledClientEngineFactory implements PooledObjectFactory<IClientEngine> {
    private List<IEndpoint> endpoints;

    public PooledClientEngineFactory(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public List<IEndpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<IEndpoint> endpoints) {
        if (endpoints == null) return;
        if (this.endpoints == null) this.endpoints = endpoints;
        for (IEndpoint newEndpoint : endpoints) {
            if (!this.endpoints.contains(newEndpoint)) {
                this.endpoints = endpoints;
                break;
            }
        }
    }


    @Override
    public PooledObject<IClientEngine> makeObject() throws Exception {
        IClientEngine engine = new AsyncClientEngine(endpoints);
        engine.start();
        return new DefaultPooledObject<>(engine);
    }

    @Override
    public void destroyObject(PooledObject<IClientEngine> pooledObject) throws Exception {
        pooledObject.getObject().stop();
    }

    @Override
    public boolean validateObject(PooledObject<IClientEngine> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<IClientEngine> pooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<IClientEngine> pooledObject) throws Exception {
        if (endpoints != pooledObject.getObject().getEndpoints()) pooledObject.getObject().setEndpoints(endpoints);
    }
}
