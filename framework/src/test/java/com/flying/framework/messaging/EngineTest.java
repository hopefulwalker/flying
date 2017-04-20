/*
 Created by Walker.Zhang on 2015/3/3.
 Revision History:
 Date          Who              Version      What
 2015/3/3     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.framework.messaging;

import com.flying.framework.messaging.endpoint.impl.Endpoint;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.engine.IServerEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.IMsgEventResult;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.engine.impl.zmq.ZMQUCClientEngine;
import com.flying.framework.messaging.engine.impl.zmq.ZMQUCServerEngine;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EngineTest {
    private static IServerEngine serverEngine;
    private static IClientEngine clientEngine;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        serverEngine = new ZMQUCServerEngine(new Endpoint());
        serverEngine.setMsgEventListener(event -> new MockMsgEventResult());
        List<IEndpoint> endpoints = new ArrayList<>();
        endpoints.add(serverEngine.getListenEndpoint());
        clientEngine = new ZMQUCClientEngine(endpoints);
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
        IMsgEvent request = new MsgEvent(IMsgEvent.ID_REQUEST, clientEngine, () -> {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        });
        try {
            Thread.sleep(500); // wait for the client to finish the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientEngine.sendMsg(request);
        IMsgEvent reply = clientEngine.recvMsg(100);
        assertEquals(IMsgEvent.ID_REPLY_SUCCEED, reply.getId());
        assertEquals(66, reply.getInfo().getByteArray()[0]);
    }

    @Test
    public void testRequest() {
        IMsgEvent request = new MsgEvent(IMsgEvent.ID_REQUEST, clientEngine, () -> {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        });
        try {
            Thread.sleep(500); // wait for the client to finish the connectiion.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IMsgEvent reply = clientEngine.request(request, 100);
        assertEquals(IMsgEvent.ID_REPLY_SUCCEED, reply.getId());
        assertEquals(66, reply.getInfo().getByteArray()[0]);
    }

    @Test
    public void testPerfermance() {
        IMsgEvent request = new MsgEvent(IMsgEvent.ID_REQUEST, clientEngine, () -> {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        });
        try {
            Thread.sleep(500); // wait for the client to finish the connectiion.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long total = 10000;
        long startMillis = System.currentTimeMillis();
        long replyCnt = total;
        IMsgEvent reply;
        for (int requestCnt = 0; requestCnt < total || replyCnt > 0; requestCnt++) {
            clientEngine.sendMsg(request);
            reply = clientEngine.recvMsg(0);
            if (reply.getId() == IMsgEvent.ID_REPLY_SUCCEED) replyCnt--;
        }
        long period = System.currentTimeMillis() - startMillis;
        System.out.println("Message processed:" + total + " Time used in millis:" + period);
    }

    private static class MockMsgEventResult implements IMsgEventResult {
        @Override
        public byte[] getByteArray() {
            byte[] array = new byte[1];
            array[0] = 66;   // char = 'B'
            return array;
        }

        @Override
        public boolean isReplyRequired() {
            return true;
        }
    }
}
