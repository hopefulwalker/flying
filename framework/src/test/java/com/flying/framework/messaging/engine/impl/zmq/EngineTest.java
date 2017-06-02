/*
 Created by Walker.Zhang on 2015/3/3.
 Revision History:
 Date          Who              Version      What
 2015/3/3      Walker.Zhang     0.1.0        Created.
 2017/5/1      Walker.Zhang     0.3.4        redefine the message event id.
 */
package com.flying.framework.messaging.engine.impl.zmq;

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

public class EngineTest {
    private static IServerCommEngine serverEngine;
    private static IClientCommEngine clientEngine;
    private static byte[] data;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        IEndpoint serverEndpoint = new Endpoint();
        IServerCommEngineConfig serverConfig = new ServerCommEngineConfig(serverEndpoint, event -> new MockMsgEventResult());
        serverEngine = new UCServerCommEngine(serverConfig);
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(serverEndpoint);
        IClientCommEngineConfig clientConfig = new ClientCommEngineConfig(endpoints);
        clientEngine = new ClientCommEngine(clientConfig);
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
    public void testStart() {
        try {
            Thread.sleep(50); // wait for the server to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverEngine.stop();
        try {
            Thread.sleep(50); // wait for the server to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientEngine.stop();
        try {
            Thread.sleep(50); // wait for the client to stop.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverEngine.start();
        try {
            Thread.sleep(50); // wait for the server to start.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientEngine.start();
        try {
            Thread.sleep(50); // wait for the server to start.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendMsgAndRecvMsg() {
        IMsgEvent request = MsgEvent.newInstance(IMsgEvent.ID_REQUEST, clientEngine, data);
        try {
            Thread.sleep(50); // wait for the client to finish the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientEngine.sendMsg(request);
        IMsgEvent reply = clientEngine.recvMsg(1000);
        Assert.assertEquals(IMsgEvent.ID_REPLY, reply.getId());
        Assert.assertEquals(66, reply.getInfo().getBytes()[0]);
    }

    @Test
    public void testSendMsgWithListener() {
        clientEngine.stop();
        clientEngine.getConfig().setMsgEventListener(event -> {
            assertEquals(66, event.getInfo().getBytes()[0]);
            return null;
        });
        clientEngine.start();
        IMsgEvent request = MsgEvent.newInstance(IMsgEvent.ID_REQUEST, clientEngine, data);
        try {
            Thread.sleep(50); // wait for the client to finish the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientEngine.sendMsg(request);
        IMsgEvent reply = clientEngine.recvMsg(50);
        Assert.assertEquals(IMsgEvent.ID_TIMEOUT, reply.getId());
        clientEngine.stop();
        clientEngine.getConfig().setMsgEventListener(null);
    }

    @Test
    public void testRequest() {
        IMsgEvent request = MsgEvent.newInstance(IMsgEvent.ID_REQUEST, clientEngine, data);
        try {
            Thread.sleep(50); // wait for the client to finish the connectiion.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IMsgEvent reply = clientEngine.request(request, 100);
        Assert.assertEquals(IMsgEvent.ID_REPLY, reply.getId());
        Assert.assertEquals(66, reply.getInfo().getBytes()[0]);
    }

    @Test
    public void testPerfermance() {
        IMsgEvent request = MsgEvent.newInstance(IMsgEvent.ID_REQUEST, clientEngine, data);
        try {
            Thread.sleep(50); // wait for the client to finish the connectiion.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long total = 1000;
        long startMillis = System.currentTimeMillis();
        long replyCnt = total;
        IMsgEvent reply;
        int requestCnt = 0;
        while (requestCnt < total || replyCnt > 0) {
            if (requestCnt < total) {
                clientEngine.sendMsg(request);
                requestCnt++;
            }
            reply = clientEngine.recvMsg(0);
            if (reply.getId() == IMsgEvent.ID_REPLY) replyCnt--;
        }
        long period = System.currentTimeMillis() - startMillis;
        System.out.println("Message processed:" + requestCnt + " Time used in millis:" + period);
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
