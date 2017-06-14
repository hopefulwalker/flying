/*
 Created by Walker.Zhang on 2015/2/17.
 Revision History:
 Date          Who              Version      What
 2015/2/17     Walker.Zhang     0.1.0        Created.
 2015/6/15     Walker.Zhang     0.2.0        Add support linux platform.
                                             1. change the way to get local address
                                             2. Add local variable to store local ip4's int and string.
 2017/6/2      Walker.Zhang     0.3.7        Refactor for controlling the ip.
*/
package com.flying.util.net;

import com.flying.util.cfg.DynamicConfiguration;
import com.flying.util.cfg.PropertiesFileConfiguration;
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
    public static final String DEFAULT_CFG_FILE_NAME = "commutils";
    private static final Logger logger = LoggerFactory.getLogger(CommUtils.class);
    private static int ip4int;
    private static String ip4String = null;
    private static CommUtilsProperties properties = new CommUtilsProperties(new PropertiesFileConfiguration(DEFAULT_CFG_FILE_NAME));

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
                if (ignoreInterface(ni)) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ignoreAddress(ip)) return ip;
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
            int port = ThreadLocalRandom.current().nextInt(properties.getDownPort(), properties.getUpPort());
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

    private static boolean ignoreAddress(InetAddress address) {
        if (!(address instanceof Inet4Address) || address.isLoopbackAddress()) return true;
        for (String regex : properties.getPreferredNetworks()) {
            String hostAddress = address.getHostAddress();
            if (!hostAddress.matches(regex) && !hostAddress.startsWith(regex)) return true;
        }
        return false;
    }

    private static boolean ignoreInterface(NetworkInterface ni) throws SocketException {
        if (!ni.isUp() || ni.isLoopback() || ni.isPointToPoint() || ni.isVirtual()) return true;
        for (String regex : properties.getIgnoredInterfaces()) {
            if (ni.getDisplayName().matches(regex)) {
                return true;
            }
        }
        return false;
    }
}