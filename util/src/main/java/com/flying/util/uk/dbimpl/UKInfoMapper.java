/*
  File: UKInfoMapper.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    retrive this from flygroup.
                                The perfermance is poor compare to java.util.UUID.
 */
package com.flying.util.uk.dbimpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UKInfoMapper implements ResultSetMapper<UKInfo> {

	@Override
	public UKInfo map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		return new UKInfo(r.getString("name"), r.getLong("beginuk"), r.getLong("enduk"), r.getTimestamp("createtime"),
				r.getTimestamp("updatetime"));
	}
}