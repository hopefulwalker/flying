/*
  File: UKInfo.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    retrive this from flygroup.
                                The perfermance is poor compare to java.util.UUID.
 */
package com.flying.util.uk.dbimpl;

import java.util.Date;

public class UKInfo {
	private String name;
	private long beginUK;
	private long endUK;
	@SuppressWarnings("unused")
	private Date createTime;
	@SuppressWarnings("unused")
	private Date updateTime;

	public UKInfo(String name, long beginUK, long endUK, Date createTime, Date updateTime) {
		this.name = name;
		this.beginUK = beginUK;
		this.endUK = endUK;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public long getBeginUK() {
		return beginUK;
	}

	public long getEndUK() {
		return endUK;
	}

	public void setBeginUK(long beginUK) {
		this.beginUK = beginUK;
	}
}
