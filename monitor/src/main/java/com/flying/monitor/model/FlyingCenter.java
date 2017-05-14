/*
 Created by Walker on 2017/5/14.
 Revision History:
 Date          Who              Version      What
 2017/5/14     Walker           0.3.5        Created for building control center.
*/
package com.flying.monitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FlyingCenter {
    private final static Logger logger = LoggerFactory.getLogger(FlyingCenter.class);

    private Map<String, IServerManager> managers;

    public FlyingCenter() {
        this.managers = new HashMap<>(1);
    }

    private void initManagers(String[] fullNames) {
        for (final String managerName : fullNames) {
            Class clazz = null;
            try {
                clazz = Class.forName(managerName);
            } catch (ClassNotFoundException cnfe) {
                logger.warn(String.format("Usage: %s class not found...\n", managerName), cnfe);
                continue;
            }
            if (!IServerManager.class.isAssignableFrom(clazz)) {
                logger.warn(String.format("%s is not a server manager...\n", managerName));
                continue;
            }
            IServerManager manager = null;
            try {
                manager = (IServerManager) clazz.newInstance();
                manager.initServer();
            } catch (InstantiationException | IllegalAccessException ie) {
                logger.warn(String.format("Failed to new instance for %s...\n", managerName), ie);
                continue;
            }
            managers.put(managerName, manager);
        }
        if (managers.size() <= 0) {
            System.out.format("No manager could be initialized...\n");
            System.out.format("Usage: %s <The fully qualified name of the IServerManager> ...\n", FlyingCenter.class.getName());
            System.exit(-1);
        }
    }

    private void startManagers() {
        for (IServerManager manager : managers.values()) {
            manager.startServer();
        }
    }

    private void stopManagers() {
        for (IServerManager manager : managers.values()) {
            manager.stopServer();
        }
    }

    private void destroyManagers() {
        for (IServerManager manager : managers.values()) {
            manager.destroyServer();
        }
    }

    private void runManagers() {
        startManagers();
        do {
            printInfo();
            Scanner in = new Scanner(System.in);
            String input = in.next().trim();
            if (input.equals("1")) {
                startManagers();
            } else if (input.equals("2")) {
                stopManagers();
            } else if (input.equals("3")) {
                destroyManagers();
                break;
            }
        }
        while (true);
    }

    public static void main(String args[]) {
        try {
            FlyingCenter center = new FlyingCenter();
            center.initManagers(args);
            center.runManagers();
        } catch (Exception e) {
            logger.error("Exception occurs when running center...", e);
        }
    }

    private void printInfo() {
        logger.info("Please input your directive:");
        logger.info("1=start");
        logger.info("2=stop");
        logger.info("3=exit");
    }
}
