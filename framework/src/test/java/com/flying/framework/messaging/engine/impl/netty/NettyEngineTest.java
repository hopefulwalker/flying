/*
 Created by Walker on 2017/4/10.
 Revision History:
 Date          Who              Version      What
 2017/4/10     Walker           0.3.0        Created.
*/
package com.flying.framework.messaging.engine.impl.netty;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.engine.IClientCommEngine;
import com.flying.framework.messaging.engine.IClientCommEngineConfig;
import com.flying.framework.messaging.engine.IServerCommEngine;
import com.flying.framework.messaging.engine.IServerCommEngineConfig;
import com.flying.framework.messaging.engine.impl.ClientCommEngineConfig;
import com.flying.framework.messaging.engine.impl.ServerCommEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NettyEngineTest {
    private static IClientCommEngine clientEngine;
    private static IServerCommEngine serverEngine;
    private static byte[] data;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        IEndpoint endpoint = new Endpoint("tcp", "127.0.0.1", 8181);
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(endpoint);
        IClientCommEngineConfig clientConfig = new ClientCommEngineConfig(endpoints);
        clientConfig.setMsgEventListener(event -> {
            assertEquals(66, event.getInfo().getBytes()[0]);
            return null;
        });
        clientEngine = new NettyClientCommEngine(clientConfig);

        IServerCommEngineConfig serverConfig = new ServerCommEngineConfig(endpoint, event -> new MockMsgEventResult());
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
            Thread.sleep(1000); // wait for the server to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MockMsgEventResult implements IMsgEventResult {
        @Override
        public List<IEndpoint> getTarget() {
            return null;
        }

        @Override
        public byte[] getBytes() {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        }
    }
}