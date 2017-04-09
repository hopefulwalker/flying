/*
 Created by Walker.Zhang on 2015/3/11.
 Revision History:
 Date          Who              Version      What
 2015/3/11     Walker.Zhang     0.1.0        Created.
 2015/6/15     Walker.Zhang     0.2.0        Override hashCode and equals.
 2017/4/9      Walker.Zhang     0.3.0        Refactor to support multi-communication library, such as netty.
 */
package com.flying.framework.messaging.endpoint.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.util.net.CommUtils;
import org.apache.commons.lang3.StringUtils;

public class Endpoint implements IEndpoint {
    private static final String PROTOCOL_SPLITTER = "://";
    private static final String ADDRESS_SPLITTER = ":";
    private String protocol;
    private String address;
    private int port;
    private String endpoint;

    public Endpoint() {
        this(IEndpoint.PROTOCOL_TCP, CommUtils.getLocalIp4AddressString(), CommUtils.getAvailablePort());
    }

    public Endpoint(String endpoint) {
        this.endpoint = endpoint;
        splitEndpointString();
    }

    public Endpoint(String protocol, String address, int port) {
        this.protocol = protocol;
        this.address = address;
        this.port = port;
        rebuildEndpointString();
    }

    private void rebuildEndpointString() {
        this.endpoint = protocol + PROTOCOL_SPLITTER + address + ADDRESS_SPLITTER + port;
    }

    private void splitEndpointString() {
        this.protocol = StringUtils.substringBefore(endpoint, PROTOCOL_SPLITTER);
        this.address = StringUtils.substringBetween(endpoint, PROTOCOL_SPLITTER, ADDRESS_SPLITTER);
        this.port = Integer.valueOf(StringUtils.substringAfterLast(endpoint, ADDRESS_SPLITTER));
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String asString() {
        return endpoint;
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;
        return (anObject instanceof Endpoint) && endpoint.equals(((Endpoint) anObject).asString());
    }
}