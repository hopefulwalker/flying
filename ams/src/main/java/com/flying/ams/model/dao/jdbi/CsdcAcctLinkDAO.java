package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.CsdcAcctLinkBO;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Date;
import java.util.List;

public interface CsdcAcctLinkDAO {
    @SqlUpdate("insert into t_csdcacctlink(exchid,exchaccttype,exchacctno,acctid,createtime,updatetime)"
            + " values (:exchid,:exchaccttype,:exchacctno,:acctid,:createtime,:updatetime)")
    int insert(@Bind("exchid") long exchid, @Bind("exchaccttype") short exchaccttype, @Bind("exchacctno") String exchacctno,
               @Bind("acctid") long acctid, @Bind("createtime") long createtime, @Bind("updatetime") long updatetime);

    @SqlUpdate("delete from t_csdcacctlink where exchid=:exchid and exchaccttype=:exchaccttype and exchacctno=:exchacctno and acctid=:acctid")
    int delete(@Bind("exchid") long exchid, @Bind("exchaccttype") short exchaccttype,
               @Bind("exchacctno") String exchacctno, @Bind("acctid") long acctid);

    @SqlQuery("select exchid,exchaccttype,exchacctno,acctid,createtime,updatetime from t_csdcacctlink where acctid=:acctid")
    @Mapper(CsdcAcctLinkMapper.class)
    List<CsdcAcctLinkBO> get(@Bind("acctid") long acctid);

    /**
     * close with no args is used to close the connection
     */
    void close();
}
