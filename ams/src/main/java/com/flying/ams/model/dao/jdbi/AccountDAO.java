package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.AccountBO;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Date;

public interface AccountDAO {
    @SqlUpdate("insert into t_account (aid, type, custid, parentid, statusid, createtime, updatetime)" +
            " values (:aid, :type,:custid,:parentid,:statusid,:createtime,:updatetime)")
    int insert(@Bind("aid") long aid, @Bind("type") short type, @Bind("custid") long custid,
               @Bind("parentid") long parentid, @Bind("statusid") byte statusid, @Bind("createtime") long createtime,
               @Bind("updatetime") long updatetime);

    @SqlUpdate("delete from t_account where aid=:aid")
    int delete(@Bind("aid") long aid);

    @SqlQuery("select aid, type, custid, parentid, statusid, createtime, updatetime from t_account where aid = :aid")
    @Mapper(AccountMapper.class)
    AccountBO get(@Bind("aid") long aid);

    /**
     * close with no args is used to close the connection
     */
    void close();
}