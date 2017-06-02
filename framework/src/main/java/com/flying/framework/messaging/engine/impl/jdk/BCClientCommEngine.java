/*
 Created by Walker.Zhang on 2015/5/20.
 Revision History:
 Date          Who              Version      What
 2015/5/20     Walker.Zhang     0.1.0        Created.
 2017/5/30     Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.framework.messaging.engine.impl.jdk;

import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientCommEngine;
import com.flying.framework.messaging.engine.IClientCommEngineConfig;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class BCClientCommEngine implements IClientCommEngine {
    private static final int PACKET_SIZE = 512;
    private static final Logger logger = LoggerFactory.getLogger(BCClientCommEngine.class);

    private DatagramSocket clientSocket = null;
    private IClientCommEngineConfig config;

    public BCClientCommEngine(IClientCommEngineConfig config) {
        setConfig(config);
    }

    /**
     * @return the config information.
     */
    @Override
    public IClientCommEngineConfig getConfig() {
        return config;
    }

    /**
     * set config for communication engine.
     *
     * @param config to be set.
     */
    @Override
    public void setConfig(IClientCommEngineConfig config) {
        this.config = config;
    }

    @Override
    public IMsgEvent request(IMsgEvent msgEvent, int timeout) {
        sendMsg(msgEvent);
        return recvMsg(timeout);
    }

    @Override
    public void sendMsg(IMsgEvent msgEvent) {
        DatagramPacket packet = new DatagramPacket(msgEvent.getInfo().getBytes(), msgEvent.getInfo().getBytes().length);
        for (IEndpoint endpoint : config.getEndpoints()) {
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
            event = MsgEvent.newInstance(IMsgEvent.ID_REPLY, this, packet.getData());
        } catch (SocketTimeoutException ste) {
            event = MsgEvent.newInstance(IMsgEvent.ID_TIMEOUT, this);
        } catch (IOException ioe) {
            logger.error(ioe.toString(), ioe);
            event = MsgEvent.newInstance(IMsgEvent.ID_FAILED, this);
        }
        return event;
    }

    @Override
    public void start() {
        try {
            clientSocket = new DatagramSocket();
            StringBuilder sb = new StringBuilder();
            for (IEndpoint endpoint : config.getEndpoints()) sb.append(endpoint.asString());
            logger.debug("BCClientCommEngine: endpoints=" + sb.toString());
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
