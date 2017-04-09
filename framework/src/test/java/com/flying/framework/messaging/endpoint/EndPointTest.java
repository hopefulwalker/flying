/**
 * Created by Walker.Zhang on 2015/5/22.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/22     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.messaging.endpoint;

import com.flying.framework.messaging.endpoint.impl.Endpoint;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EndPointTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
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
