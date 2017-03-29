/**
 * Created by Walker.Zhang on 2015/5/30.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/30     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.service;

import com.flying.framework.messaging.endpoint.IEndpoint;

import java.util.List;

public interface IEndpointFactory {
    public List<IEndpoint> getEndpoints(String region, short type);
}
