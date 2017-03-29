package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.CsdcAcctLinkBO;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CsdcAcctLinkMapper implements ResultSetMapper<CsdcAcctLinkBO> {

    @Override
    public CsdcAcctLinkBO map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new CsdcAcctLinkBO(r.getShort("exchid"), r.getShort("exchaccttype"), r.getString("exchacctno"), r.getLong("acctid"),
                r.getLong("createtime"), r.getLong("updatetime"));
    }
}
