/*
 File: CustomerDAO.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2015.1.9     walker  0.1.0    Create this file.
 */
package com.flying.cms.model.dao.jdbi;

import com.flying.cms.model.Customer;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Date;

public interface CustomerDAO {
    @SqlUpdate("insert into t_customer (cid,certtype,certno,name,createtime,updatetime)"
            + " values (:cid,:certtype,:certno,:name,:createtime,updatetime)")
    int insert(@Bind("cid") long cid, @Bind("certtype") short certtype, @Bind("certno") String certno,
               @Bind("name") String name, @Bind("createtime") long createtime, @Bind("updatetime") long updatetime);

    @SqlUpdate("delete from t_customer where cid=:cid")
    int delete(@Bind("cid") long cid);

    @SqlQuery("select cid,certtype,certno,name,createtime,updatetime from t_customer where cid=:cid")
    @Mapper(CustomerMapper.class)
    Customer get(@Bind("cid") long cid);

    /**
     * close with no args is used to close the connection
     */
    void close();
}
