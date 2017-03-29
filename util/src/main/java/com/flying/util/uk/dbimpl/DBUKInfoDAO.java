/*
  File: DBUKinfoDAO.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    retrive this from flygroup.
                                The perfermance is poor compare to java.util.UUID.
 */
package com.flying.util.uk.dbimpl;

import java.util.Date;
import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public abstract class DBUKInfoDAO {

	@SqlUpdate("create table if not exists ukinfo (name VARCHAR(64) NOT NULL, beginuk BIGINT NULL DEFAULT 1, enduk BIGINT NULL DEFAULT 0, createtime DATETIME NULL DEFAULT CURRENT_TIMESTAMP, updatetime DATETIME NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (name))")
	public abstract void createTable();

	@SqlQuery("select name, beginuk, enduk, createtime, updatetime from ukinfo where name=:name")
	@Mapper(UKInfoMapper.class)
	public abstract UKInfo get(@Bind("name") String name);

	@SqlUpdate("insert ukinfo(name, beginuk, enduk, createtime, updatetime) values(:name, :beginuk, :enduk, :createtime, :updatetime)")
	public abstract int insert(@Bind("name") String name, @Bind("beginuk") long beginuk, @Bind("enduk") long enduk,
			@Bind("createtime") Date createtime, @Bind("updatetime") Date updatetime);

	@SqlUpdate("update ukinfo set beginuk=enduk+:step, enduk=enduk+:size, updatetime=CURRENT_TIMESTAMP where name=:name")
	public abstract int update(@Bind("name") String name, @Bind("step") int step, @Bind("size") int size);

	@SqlUpdate("delete from ukinfo where name=:name")
	public abstract int delete(@Bind("name") String name);	

	@Transaction(TransactionIsolationLevel.SERIALIZABLE)
	public UKInfo refresh(String name, int step, int size) {
		int rows = update(name, step, size);
		if (rows == 0)
			insert(name, step, size, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
		return get(name);
	}

	/**
	 * close with no args is used to close the connection
	 */
	public abstract void close();
}