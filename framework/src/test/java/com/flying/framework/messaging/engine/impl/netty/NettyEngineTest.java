/*
 Created by Walker on 2017/4/10.
 Revision History:
 Date          Who              Version      What
 2017/4/10     Walker           0.3.0        Created.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IAsyncClientCommEngine;
import com.flying.framework.messaging.engine.IAsyncServerCommEngine;
import com.flying.framework.messaging.engine.ICommEngineConfig;
import com.flying.framework.messaging.engine.impl.CommEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NettyEngineTest {
    private static IAsyncClientCommEngine clientEngine;
    private static IAsyncServerCommEngine serverEngine;
    private static byte[] data;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(new Endpoint("tcp", "127.0.0.1", 8181));
        ICommEngineConfig clientConfig = new CommEngineConfig(endpoints);
        clientConfig.setMsgEventListener(event -> {
            assertEquals(66, event.getInfo().getByteArray()[0]);
            return null;
        });
        clientEngine = new NettyClientCommEngine(clientConfig);
        ICommEngineConfig serverConfig = new CommEngineConfig(endpoints);
        serverConfig.setMsgEventListener(event -> new MockMsgEventResult());
        serverEngine = new NettyServerCommEngine(serverConfig);
        data = new byte[1];
        data[0] = 66;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        serverEngine.start();
        clientEngine.start();
    }

    @After
    public void tearDown() throws Exception {
        clientEngine.stop();
        serverEngine.stop();
    }

    @Test
    public void sendMsg() throws Exception {
        IMsgEvent request = MsgEvent.newInstance(IMsgEvent.ID_REQUEST, clientEngine, data);
        clientEngine.sendMsg(request);
        try {
            Thread.sleep(5000); // wait for the server to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MockMsgEventResult implements IMsgEventResult {
        @Override
        public byte[] getByteArray() {
            byte[] array = new byte[1];
            array[0] = 65;   // char = 'B'
            return array;
        }

        @Override
        public boolean isReplyRequired() {
            return true;
        }
    }
}