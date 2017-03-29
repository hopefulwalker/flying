package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.CsdcAcctBO;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CsdcAcctMapper implements ResultSetMapper<CsdcAcctBO> {

    @Override
    public CsdcAcctBO map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new CsdcAcctBO(r.getShort("exchid"), r.getShort("type"), r.getString("no"), r.getLong("custid"), r.getByte("statusid"),
                r.getLong("createtime"), r.getLong("updatetime"));
    }
}