package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.CsdcAcctBO;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Date;

public interface CsdcAcctDAO {
    @SqlUpdate("insert into t_csdcacct(exchid,type,no,custid,statusid,createtime,updatetime)"
            + " values (:exchid,:type,:no,:custid,:statusid,:createtime,:updatetime)")
    int insert(@Bind("exchid") long exchid, @Bind("type") short type, @Bind("no") String no, @Bind("custid") long custid,
               @Bind("statusid") byte statusid, @Bind("createtime") long createtime, @Bind("updatetime") long updatetime);

    @SqlUpdate("delete from t_csdcacct where exchid=:exchid and type=:type and no=:no")
    int delete(@Bind("exchid") long exchid, @Bind("type") short type, @Bind("no") String no);

    @SqlQuery("select exchid,type,no,custid,statusid,createtime,updatetime from t_csdcacct where exchid=:exchid and type=:type and no=:no")
    @Mapper(CsdcAcctMapper.class)
    CsdcAcctBO get(@Bind("exchid") long exchid, @Bind("type") short type, @Bind("no") String no);

    /**
     * close with no args is used to close the connection
     */
    void close();
}
