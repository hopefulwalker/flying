/*
 File: Customer.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2015.1.6     walker  0.1.0    Create this file.
 2015.1.9     WALKER  0.2.0    Add name and const definition for certtype.
 */

package com.flying.cms.model;

public class Customer {
    private long cid;
    private short certType;
    private String certNO;
    private String name;
    private long createTime;
    private long updateTime;

    public Customer(long cid, short certType, String certNO, String name, long createTime, long updateTime) {

        this.cid = cid;
        this.certType = certType;
        this.certNO = certNO;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getCid() {
        return cid;
    }

    public short getCertType() {
        return certType;
    }

    public String getCertNO() {
        return certNO;
    }

    public String getName() {
        return name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
