/*
 Created by Walker on 2017/5/14.
 Revision History:
 Date          Who              Version      What
 2017/5/14     Walker           0.3.5        Created for building control center.
*/
package com.flying.monitor.model;

public interface IServerManager {
    void initServer();

    void startServer();

    void stopServer();

    void destroyServer();
}
