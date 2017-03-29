/**
 * Created by Walker.Zhang on 2015/2/17.
 * Revision History:
 * Date          Who              Version      What
 * 2015/2/17     Walker.Zhang     0.1.0        Created.
 * 2015/6/15     Walker.Zhang     0.2.0        Add support linux platform.
 *                                             1. change the way to get local address
 *                                             2. Add local variable to store local ip4's int and string.
 */
package com.flying.util.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * utilities for communication.
 */
public final class CommUtils {
    public static final int DOWN_PORT = 5001;
    public static final int UP_PORT = 65536;
    private static final Logger logger = LoggerFactory.getLogger(CommUtils.class);
    private static int ip4int;
    private static String ip4String = null;

    static {
        InetAddress address = getLocalIp4Address();
        ip4int = ip4ToInt(address);
        ip4String = address.getHostAddress();
    }

    // IP Address related functions
    public static String getLocalIp4AddressString() {
        return ip4String;
    }

    public static int getLocalIp4AddressAsInt() {
        return ip4int;
    }

    private static InetAddress getLocalIp4Address() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) return ip;
                }
            }
            logger.warn("No non-loopback address, return the loop back address");
            return InetAddress.getLoopbackAddress();
        } catch (SocketException se) {
            logger.error("error in getting the local host's ip address, return the loop back address", se);
            return InetAddress.getLoopbackAddress();
        }
    }

    public static InetAddress intToIp4(int ipv4) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((ipv4 & 0xFF000000) >> 24);
        bytes[1] = (byte) ((ipv4 & 0x00FF0000) >> 16);
        bytes[2] = (byte) ((ipv4 & 0x0000FF00) >> 8);
        bytes[3] = (byte) (ipv4 & 0x000000FF);
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    public static int ip4ToInt(InetAddress address) {
        byte[] bytes = address.getAddress();
        int iIPv4 = 0x00;
        iIPv4 = bytes[0];
        iIPv4 = (iIPv4 << 8) | bytes[1];
        iIPv4 = (iIPv4 << 8) | bytes[2];
        iIPv4 = (iIPv4 << 8) | bytes[3];
        return iIPv4;
    }

    // Port related functions
    public static int getAvailablePort() {
        do {
            int port = ThreadLocalRandom.current().nextInt(DOWN_PORT, UP_PORT);
            if (isAvailable(port)) return port;
        }
        while (true);
    }

    private static boolean isAvailable(int port) {
        Socket s = new Socket();
        try {
            bindPort("0.0.0.0", port);
            bindPort(getLocalIp4AddressString(), port);
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    private static void bindPort(String host, int port) throws IOException {
        Socket s = new Socket();
        s.bind(new InetSocketAddress(host, port));
        s.close();
    }
}