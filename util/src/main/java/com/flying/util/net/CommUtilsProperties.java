/*
 Created by Walker.Zhang on 2017/6/14.
 Revision History:
 Date          Who              Version      What
 2017/6/2      Walker.Zhang     0.3.7        Create for controlling the ip.
*/
package com.flying.util.net;

import com.flying.util.cfg.IConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommUtilsProperties {
    public static final String KEY_DOWN_PORT = "commutils.down.port";
    public static final String KEY_UP_PORT = "commutils.up.port";
    public static final String KEY_IGNORED_INTERFACES = "commutils.ignored.interfaces";
    public static final String KEY_PREFERRED_NETWORKS = "commutils.preferred.networks";
    public static final int DEFAULT_DOWN_PORT = 5001;
    public static final int DEFAULT_UP_PORT = 65536;

    /**
     * the up side of port.
     */
    private int upPort;
    /**
     * the down side of port.
     */
    private int downPort;
    /**
     * List of Java regex expressions for network interfaces that will be ignored.
     */
    private List<String> ignoredInterfaces;
    /**
     * List of Java regex expressions for network addresses that will be preferred.
     */
    private List<String> preferredNetworks;

    public CommUtilsProperties(IConfiguration config) {
        this.downPort = NumberUtils.toInt(config.getValue(KEY_DOWN_PORT), DEFAULT_DOWN_PORT);
        this.upPort = NumberUtils.toInt(config.getValue(KEY_UP_PORT), DEFAULT_UP_PORT);
        if (config.containsKey(KEY_IGNORED_INTERFACES))
            this.ignoredInterfaces = Arrays.asList(StringUtils.split(config.getValue(KEY_IGNORED_INTERFACES), ","));
        else
            this.ignoredInterfaces = new ArrayList<>(0);
        if (config.containsKey(KEY_PREFERRED_NETWORKS))
            this.preferredNetworks = Arrays.asList(StringUtils.split(config.getValue(KEY_PREFERRED_NETWORKS), ","));
        else this.preferredNetworks = new ArrayList<>(0);
    }

    public int getUpPort() {
        return upPort;
    }

    public int getDownPort() {
        return downPort;
    }

    public List<String> getIgnoredInterfaces() {
        return ignoredInterfaces;
    }

    public List<String> getPreferredNetworks() {
        return preferredNetworks;
    }
}
