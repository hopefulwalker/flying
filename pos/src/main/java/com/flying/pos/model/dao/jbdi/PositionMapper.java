package com.flying.pos.model.dao.jbdi;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flying.pos.model.Position;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PositionMapper implements ResultSetMapper<Position> {

	@Override
	public Position map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		return new Position(r.getLong("acctid"), r.getString("prodid"), r.getByte("type"), r.getByte("settleflag"),
				r.getInt("bizdate"), r.getInt("settledate"), r.getDouble("nos"), r.getDouble("frozennos"),
				r.getShort("cytype"), r.getLong("createtime"), r.getLong("updatetime"));
	}
}