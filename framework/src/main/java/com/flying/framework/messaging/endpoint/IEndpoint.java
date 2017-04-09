/*
 Created by Walker.Zhang on 2015/2/27.
 Revision History:
 Date          Who              Version      What
 2015/2/27     Walker.Zhang     0.1.0        Created.
 2017/4/9      Walker.Zhang     0.3.0        Refactor to support multi-communication library, such as netty.
 */
package com.flying.framework.messaging.endpoint;

public interface IEndpoint {
    String PROTOCOL_TCP = "tcp";

    String PROTOCOL_UDP = "udp";

    String PROTOCOL_INPROC = "inproc";

    String asString();

    String getProtocol();

    String getAddress();

    int getPort();
}