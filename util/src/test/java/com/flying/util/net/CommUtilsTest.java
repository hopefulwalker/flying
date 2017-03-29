/**
 * Created by Walker.Zhang on 2015/6/15.
 * Revision History:
 * Date          Who              Version      What
 * 2015/6/15     Walker.Zhang     0.1.0        Created.
 */
package com.flying.util.net;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommUtilsTest {
    @Before
    public void setUp() throws Exception {
        CommUtils.getLocalIp4AddressAsInt();
    }

    @Test
    public void testGetString() throws Exception {
        System.out.println(CommUtils.getLocalIp4AddressString());
        System.out.println(CommUtils.getLocalIp4AddressAsInt());
    }

    @Test
    public void testGetAvailablePort() throws Exception {
        int port = CommUtils.getAvailablePort();
        assertTrue(port >= CommUtils.DOWN_PORT); //inclusive
        assertTrue(port < CommUtils.UP_PORT);    //exclusive
    }

    @Test(timeout = 1)
    public void testGetLocalIp4AddressAsIntPerformance() throws Exception {
        for (int i = 0; i < 10000; i++) {
            CommUtils.getLocalIp4AddressAsInt();
        }
    }
}
