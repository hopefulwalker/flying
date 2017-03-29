package com.flying.ams.model;

import java.util.Date;

public class CsdcAcctLinkBO {
    private short exchId;
    private short exchAcctType;
    private String exchAcctNo;
    private long acctId;
    private long createTime;
    private long updateTime;

    public CsdcAcctLinkBO(short exchId, short exchAcctType, String exchAcctNo, long acctId, long createTime, long updateTime) {
        this.exchId = exchId;
        this.exchAcctType = exchAcctType;
        this.exchAcctNo = exchAcctNo;
        this.acctId = acctId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public short getExchId() {
        return exchId;
    }

    public short getExchAcctType() {
        return exchAcctType;
    }

    public String getExchAcctNo() {
        return exchAcctNo;
    }

    public long getAcctId() {
        return acctId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
