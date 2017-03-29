/*
 Created by Walker.Zhang on 2015/3/11.
 Revision History:
 Date          Who              Version      What
 2015/3/11     Walker.Zhang     0.1.0        Created.
 */

package com.flying.pos.model;

import java.util.Date;

public class Position {
    private long acctId;
    private String prodId;
    /**
     * 持仓类型（可用/余额）
     */
    private short type;
    private byte settleFlag;
    private int bizDate;
    private int settleDate;
    /**
     * 数值（金额/数量）
     */
    private double nos;
    /**
     * 冻结数值（金额/数量）
     */
    private double frozenNos;
    /**
     * 货币类型
     */
    private short cyType;
    private long createTime;
    private long updateTime;

    public Position(long acctId, String prodId, short type, byte settleFlag, int bizDate, int settleDate,
                    double nos, double frozenNos, short cyType, long createTime, long updateTime) {
        this.acctId = acctId;
        this.prodId = prodId;
        this.type = type;
        this.settleFlag = settleFlag;
        this.bizDate = bizDate;
        this.settleDate = settleDate;
        this.nos = nos;
        this.frozenNos = frozenNos;
        this.cyType = cyType;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getAcctId() {
        return acctId;
    }

    public String getProdId() {
        return prodId;
    }

    public short getType() {
        return type;
    }

    public byte getSettleFlag() {
        return settleFlag;
    }

    public int getBizDate() {
        return bizDate;
    }

    public int getSettleDate() {
        return settleDate;
    }

    public double getNos() {
        return nos;
    }

    public double getFrozenNos() {
        return frozenNos;
    }

    public short getCyType() {
        return cyType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}