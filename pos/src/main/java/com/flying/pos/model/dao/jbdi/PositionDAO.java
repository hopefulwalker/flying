package com.flying.pos.model.dao.jbdi;

import com.flying.pos.model.Position;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Date;
import java.util.List;

public interface PositionDAO {
    @SqlUpdate("insert into t_position(acctid,prodid,type,settleflag,bizdate,settledate,nos,frozennos,cytype,createtime,updatetime)"
            + " values(:acctid,:prodid,:type,:settleflag:,:bizdate,:settledate,:nos,:frozennos,:cytype,:createtime,:updatetime)")
    int insert(@Bind("acctid") long acctid, @Bind("prodid") long prodid, @Bind("type") short type,
               @Bind("settleflag") byte settleflag, @Bind("bizdate") int bizDate, @Bind("settledate") int settleDate,
               @Bind("nos") double nos, @Bind("frozennos") double frozennos, @Bind("cytype") short cytype, @Bind("createtime") long createtime,
               @Bind("updatetime") long updatetime);

    @SqlUpdate("delete from t_position where acctid=:acctid and prodid=:prodid and type=:type and settleflag=:settleflag and bizdate=:bizdate and " +
            "settledate=:settledate")
    int delete(@Bind("acctid") long acctid, @Bind("prodid") long prodid, @Bind("type") short type,
               @Bind("settleflag") byte settleflag, @Bind("bizdate") int bizDate, @Bind("settledate") int settleDate);

    @SqlQuery("select acctid,prodid,type,settleflag,bizdate,settledate,nos,frozennos,cytype,createtime,updatetime from t_position where acctid = :acctid")
    @Mapper(PositionMapper.class)
    List<Position> get(@Bind("acctid") long acctid);

    @SqlQuery("select acctid,prodid,type,settleflag,bizdate,settledate,nos,frozennos,cytype,createtime,updatetime from t_position "
            + " where acctid=:acctid and prodid=:prodid and type=:type and settleflag=:settleflag and bizdate=:bizdate and settledate=:settledate")
    @Mapper(PositionMapper.class)
    Position get(@Bind("acctid") long acctid, @Bind("prodid") long prodid, @Bind("type") short type,
                 @Bind("settleflag") byte settleflag, @Bind("bizdate") int bizDate, @Bind("settledate") int settleDate);

    /**
     * close with no args is used to close the connection
     */
    void close();
}