package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.AccountBO;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements ResultSetMapper<AccountBO> {

    @Override
    public AccountBO map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new AccountBO(r.getLong("aid"), r.getShort("type"), r.getLong("custid"), r.getLong("parentid"),
                r.getByte("statusid"), r.getLong("createtime"), r.getLong("updatetime"));
    }
}