/*
 Created by Walker.Zhang on 2015/2/27.
 Revision History:
 Date          Who              Version      What
 2015/2/27     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging.endpoint;

public interface IEndpoint {
    public static final String PROTOCOL_TCP = "tcp";

    public static final String PROTOCOL_UDP = "udp";

    public static final String PROTOCOL_INPROC = "inproc";

    public String getEndpoint();

    public void setEndpoint(String endpoint);

    public int getPort();

    public String getAddress();

    public String getProtocol();
}
