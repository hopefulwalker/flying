/*
 Created by Walker.Zhang on 2015/3/14.
 Revision History:
 Date          Who              Version      What
2015/3/14     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Dictionary {
    private static final Logger logger = LoggerFactory.getLogger(Dictionary.class);
    private static final ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<>();


    public static String getString(Class clazz, int key) {
        return getString(clazz.getName(), String.valueOf(key));
    }

    public static String getString(String baseName, int key) {
        return getString(baseName, String.valueOf(key));
    }

    public static String getString(Class clazz, String key) {
        return getString(clazz.getName(), key);

    }

    public static String getString(String baseName, String key) {
        boolean existsResource = true;
        if (!bundles.containsKey(baseName)) {
            existsResource = load(baseName);
        }
        if (existsResource && (bundles.get(baseName).containsKey(key)))
            return bundles.get(baseName).getString(key);
        else
            return key;

    }

    public static synchronized boolean load(String baseName) {
        try {
            // double checking for multithread access.
            if (!bundles.containsKey(baseName))
                bundles.put(baseName, ResourceBundle.getBundle(baseName));
        } catch (Exception e) {
            logger.error("Exception in load resource bundle!", e);
            return false;
        }
        return true;
    }
}