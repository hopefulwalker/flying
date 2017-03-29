/*
 Created by Walker.Zhang on 2015/3/5.
 Revision History:
 Date          Who              Version      What
 2015/3/5     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.monitor.model;

public interface IServer {
    public ServerBO getServerBO();

    public void start();

    public void stop();
}