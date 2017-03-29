/**
 * Created by Walker.Zhang on 2015/6/1.
 * Revision History:
 * Date          Who              Version      What
 * 2015/6/1     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.service.client;

import com.flying.common.service.IEndpointFactory;
import com.flying.framework.messaging.endpoint.IEndpoint;

import java.util.List;

public class FixedEndpointFactory implements IEndpointFactory {
    private List<IEndpoint> endpoints;

    public FixedEndpointFactory(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public List<IEndpoint> getEndpoints(String region, short type) {
        return endpoints;
    }
}