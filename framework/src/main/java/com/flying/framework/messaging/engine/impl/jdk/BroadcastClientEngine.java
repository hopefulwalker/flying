/**
 * Created by Walker.Zhang on 2015/5/20.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/20     Walker.Zhang     0.1.0        Created.
 */
package com.flying.framework.messaging.engine.impl.jdk;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;

public class BroadcastClientEngine implements IClientEngine {
    public static final int PACKET_SIZE = 512;
    private static final Logger logger = LoggerFactory.getLogger(BroadcastClientEngine.class);
    DatagramSocket clientSocket = null;
    private List<IEndpoint> endpoints;

    public BroadcastClientEngine(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void setEndpoints(List<IEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public List<IEndpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public IMsgEvent request(IMsgEvent msgEvent, int timeout) {
        sendMsg(msgEvent);
        return recvMsg(timeout);
    }

    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        DatagramPacket packet = new DatagramPacket(msgEvent.getEventInfo().getByteArray(), msgEvent.getEventInfo().getByteArray().length);
        for (IEndpoint endpoint : endpoints) {
            try {
                packet.setAddress(InetAddress.getByName(endpoint.getAddress()));
                packet.setPort(endpoint.getPort());
                clientSocket.send(packet);
            } catch (IOException ioe) {
                logger.error("Failed to broadcast message to" + endpoint.asString(), ioe);
            }
        }
    }

    @Override
    public IMsgEvent recvMsg(int timeout) {
        DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
        IMsgEvent event;
        try {
            clientSocket.setSoTimeout(timeout);
            clientSocket.receive(packet);
            event = new MsgEvent(IMsgEvent.ID_REPLY_SUCCEED, this, new MsgEventInfo(packet.getData()));
        } catch (SocketTimeoutException ste) {
            event = new MsgEvent(IMsgEvent.ID_REPLY_TIMEOUT, this, new MsgEventInfo(new byte[0]));
        } catch (IOException ioe) {
            logger.error(ioe.toString(), ioe);
            event = new MsgEvent(IMsgEvent.ID_REPLY_FAILED, this, new MsgEventInfo(new byte[0]));
        }
        return event;
    }

    @Override
    public void start() {
        try {
            clientSocket = new DatagramSocket();
            StringBuilder sb = new StringBuilder();
            for (IEndpoint endpoint : endpoints) sb.append(endpoint.asString());
            logger.debug("BroadcastClientEngine: endpoints=" + sb.toString());
        } catch (Exception e) {
            logger.error("Exception occurs in starting engine...", e);
        }
    }

    @Override
    public void stop() {
        if (clientSocket != null) clientSocket.close();
        clientSocket = null;
    }
}
