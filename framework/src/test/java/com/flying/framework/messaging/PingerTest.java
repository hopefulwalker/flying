/*
 Created by Walker.Zhang on 2015/3/20.
 Revision History:
 Date          Who              Version      What
 2015/3/20     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging;

import com.flying.framework.messaging.endpoint.Endpoint;
import com.flying.framework.messaging.engine.IPinger;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.engine.impl.ZMQPinger;
import com.flying.framework.messaging.engine.impl.ZMQUCServerEngine;
import org.junit.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PingerTest {
    private static IServerEngine serverEngine;
    private static IPinger pinger;
    private static final int TIMEOUT = 1000;
    private static final int CONCURRENT_USERS = 100;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        serverEngine = new ZMQUCServerEngine(new Endpoint());
        pinger = new ZMQPinger();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        serverEngine.start();
        pinger.start();
        try {
            Thread.sleep(500); // wait for the client to finish the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws Exception {
        serverEngine.stop();
        pinger.stop();
    }

    @Test
    public void testPing() {
        long before = System.currentTimeMillis();
        assertTrue(pinger.ping(serverEngine.getListenEndpoint(), TIMEOUT));
        assertTrue(System.currentTimeMillis() - before <= TIMEOUT);
        before = System.currentTimeMillis();
        assertFalse(pinger.ping(new Endpoint(), TIMEOUT));
        assertFalse(System.currentTimeMillis() - before <= TIMEOUT);
    }

    @Test(timeout = 5000L)
    public void testPerfermance() {
        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_USERS);
        for (int i = 0; i < CONCURRENT_USERS; i++)
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    assertTrue(pinger.ping(serverEngine.getListenEndpoint(), TIMEOUT));
                }
            });
        pool.shutdown();
        try {
            boolean succeed = pool.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
            assertTrue(succeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
