/**
 * Created by Walker.Zhang on 2017/4/26.
 * Revision History:
 * Date          Who              Version      What
 * 2017/4/26     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.messaging.endpoint.impl;

import com.flying.framework.messaging.endpoint.IEndpoint;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EndpointTest {
    private static IEndpoint inprocEndpoint;
    private static IEndpoint tcpEndpoint;
    private static IEndpoint udpEndpoint;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        inprocEndpoint = new Endpoint("inproc://zctx-pipe-1582028874");
        tcpEndpoint = new Endpoint("tcp://localhost:28874");
        udpEndpoint = new Endpoint("udp://255.255.255.255:874");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }


    @Test
    public void testGetProtocol() throws Exception {
        assertEquals("Protocol", IEndpoint.PROTOCOL_INPROC, inprocEndpoint.getProtocol());
        assertEquals("Protocol", IEndpoint.PROTOCOL_TCP, tcpEndpoint.getProtocol());
        assertEquals("Protocol", IEndpoint.PROTOCOL_UDP, udpEndpoint.getProtocol());
    }

    @Test
    public void testGetAddress() throws Exception {
        assertEquals("Address", "zctx-pipe-1582028874", inprocEndpoint.getAddress());
        assertEquals("Address", "localhost", tcpEndpoint.getAddress());
        assertEquals("Address", "255.255.255.255", udpEndpoint.getAddress());
    }

    @Test
    public void testGetPort() throws Exception {
        assertTrue("Port", inprocEndpoint.getPort() == 0);
        assertTrue("Port", tcpEndpoint.getPort() == 28874);
        assertTrue("Port", udpEndpoint.getPort() == 874);
    }

    @Test
    public void testAsString() throws Exception {

    }

    @Test
    public void testEquals() {
        Endpoint pointA = new Endpoint();
        String endpointString = pointA.asString();
        Endpoint pointB = new Endpoint(endpointString);
        assertTrue(pointA.equals(pointB));
        assertFalse(pointA.equals(null));
    }
}