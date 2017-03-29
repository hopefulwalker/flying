/*
 File: Account.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2014.12.31   walker  0.1.0    Create this file.
 2015.1.9     walker  0.2.0    Add status.
 */

package com.flying.ams.model;

public class AccountBO {
    private long aid;
    private short type;
    private long custId;
    private long parentId;
    private byte statusId;
    private long createTime;
    private long updateTime;

    public AccountBO() {
    }

    public AccountBO(long aid, short type, long custId, long parentId, byte statusId, long createTime, long updateTime) {
        this.aid = aid;
        this.type = type;
        this.custId = custId;
        this.parentId = parentId;
        this.statusId = statusId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public byte getStatusId() {
        return statusId;
    }

    public void setStatusId(byte statusId) {
        this.statusId = statusId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}