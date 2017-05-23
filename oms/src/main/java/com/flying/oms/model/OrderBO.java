/*
 File: Order.java
 Originally written by Walker.
 Revision History:
 Date         Who     Version  What
 2014.12.21   walker  0.1.0    Create this file.
 */
package com.flying.oms.model;

import com.flying.common.codegen.serializer.anno.Serialization;
import com.flying.framework.fsm.IStateEventInfo;

import java.io.Serializable;

public class OrderBO implements Serializable {
    @Serialization(id = 1)
    private long oid;
    @Serialization(id = 2)
    private long extNo;
    @Serialization(id = 3)
    private long acctId;
    @Serialization(id = 4)
    private short exchId;
    @Serialization(id = 5, bytes = 6)
    private String sectCode;
    @Serialization(id = 6)
    private byte bsSideId;
    @Serialization(id = 7)
    private double price;
    @Serialization(id = 8)
    private long qty;
    @Serialization(id = 9, bytes = 8)
    private String cntrNo;
    @Serialization(id = 10)
    private int bizDate;
    @Serialization(id = 11)
    private OrderStates state;
    @Serialization(id = 12)
    private int stateEnteredCode;
    @Serialization(id = 13)
    private long createTime;
    @Serialization(id = 14)
    private long updateTime;

    public OrderBO() {
        // set string to empty string to avoid the null pointer exception.
        this.sectCode = "";
        this.cntrNo = "";
    }

    public int getStateEnteredCode() {
        return stateEnteredCode;
    }

    public void setStateEnteredCode(int stateEnteredCode) {
        this.stateEnteredCode = stateEnteredCode;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getCntrNo() {
        return cntrNo;
    }

    public void setCntrNo(String cntrNo) {
        this.cntrNo = cntrNo;
    }

    public int getBizDate() {
        return bizDate;
    }

    public void setBizDate(int bizDate) {
        this.bizDate = bizDate;
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

    public long getAcctId() {
        return acctId;
    }

    public void setAcctId(long acctId) {
        this.acctId = acctId;
    }

    public byte getBsSideId() {
        return bsSideId;
    }

    public void setBsSideId(byte bsSideId) {
        this.bsSideId = bsSideId;
    }

    public short getExchId() {
        return exchId;
    }

    public void setExchId(short exchId) {
        this.exchId = exchId;
    }

    public String getSectCode() {
        return sectCode;
    }

    public void setSectCode(String sectCode) {
        this.sectCode = sectCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public OrderStates getState() {
        return state;
    }

    public void setState(OrderStates state) {
        this.state = state;
    }

    public long getExtNo() {
        return extNo;
    }

    public void setExtNo(long extNo) {
        this.extNo = extNo;
    }
}