/*
 File: CSDCAccount.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2015.1.9     walker  0.1.0    Create this file. 
 */

package com.flying.ams.model;

import java.util.Date;

public class CsdcAcctBO {
    private short exchId;
    private short type;
    private String no;
    private long custId;
    private byte statusId;
    private long createTime;
    private long updateTime;

    public CsdcAcctBO(short exchId, short type, String no, long custId, byte statusId, long createTime, long updateTime) {

        this.exchId = exchId;
        this.type = type;
        this.no = no;
        this.custId = custId;
        this.statusId = statusId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public short getExchId() {
        return exchId;
    }

    public short getType() {
        return type;
    }

    public String getNo() {
        return no;
    }

    public long getCustId() {
        return custId;
    }

    public byte getStatusId() {
        return statusId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}