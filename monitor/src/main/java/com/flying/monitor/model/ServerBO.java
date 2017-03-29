/*
 Created by Walker.Zhang on 2015/2/24.
 Revision History:
 Date          Who              Version      What
 2015/2/24     Walker.Zhang     0.1.0        Created.
 2015/3/4      Walker.Zhang     0.1.1        Refactor: change service -> server.
 2015/3/12     Walker.Zhang     0.1.2        Refactor: unify the naming rules
 */
package com.flying.monitor.model;

import java.util.UUID;

public class ServerBO {
    private String uuid;
    private String region;
    private short serviceType;
    private String name;
    private String endpoint;
    private int workers;
    private byte stateId;
    private long reportTime;

    public ServerBO() {
    }

    public ServerBO(String region, short serviceType, String name) {
        this.uuid = UUID.randomUUID().toString();
        this.region = region;
        this.serviceType = serviceType;
        this.name = name;
        this.stateId = IServerStateId.READY;
        this.reportTime = System.currentTimeMillis();
    }

    public ServerBO(String uuid, String region, short serviceType, String name, String endpoint, int workers, byte stateId, long reportTime) {
        this.uuid = uuid;
        this.region = region;
        this.serviceType = serviceType;
        this.name = name;
        this.endpoint = endpoint;
        this.workers = workers;
        this.stateId = stateId;
        this.reportTime = reportTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public short getServiceType() {
        return serviceType;
    }

    public void setServiceType(short serviceType) {
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public byte getStateId() {
        return stateId;
    }

    public void setStateId(byte stateId) {
        this.stateId = stateId;
    }

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long time) {
        this.reportTime = time;
    }
}
