/*
 Created by Walker on 2017/4/10.
 Revision History:
 Date          Who              Version      What
 2017/4/10     Walker           0.3.0        Created.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IAsyncClientEngine;
import com.flying.framework.messaging.engine.IAsyncClientEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NettyClientEngineTest {
    private static IAsyncClientEngine clientEngine;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(new Endpoint("tcp", "192.168.6.210", 8181));
        IAsyncClientEngineConfig config = new NettyClientEngineConfig(endpoints);
        config.setMsgEventListener(event -> {
            assertEquals(66, event.getEventInfo().getByteArray()[0]);
            return null;
        });
        clientEngine = new NettyClientEngine(config);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        clientEngine.start();
    }

    @After
    public void tearDown() throws Exception {
        clientEngine.stop();
    }

    @Test
    public void sendMsg() throws Exception {
        IMsgEvent request = new MsgEvent(IMsgEvent.ID_REQUEST, clientEngine, () -> {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        });
        clientEngine.sendMsg(request);
        try {
            Thread.sleep(5000); // wait for the server to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}