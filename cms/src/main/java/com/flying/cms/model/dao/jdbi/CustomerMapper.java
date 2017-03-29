/*
 File: CustomerMapper.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2015.1.9     walker  0.1.0    Create this file.
 */
package com.flying.cms.model.dao.jdbi;

import com.flying.cms.model.Customer;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements ResultSetMapper<Customer> {

    @Override
    public Customer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Customer(r.getLong("cid"), r.getShort("certtype"), r.getString("certno"), r.getString("name"),
                r.getLong("createtime"), r.getLong("updatetime"));
    }
}