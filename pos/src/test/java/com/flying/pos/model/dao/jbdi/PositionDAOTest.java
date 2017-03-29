package com.flying.pos.model.dao.jbdi;

import static org.junit.Assert.*;

import java.util.Date;

import com.flying.common.model.ICyType;
import com.flying.pos.model.ISettleFlag;
import com.flying.pos.model.IPosType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import com.flying.util.db.DataContext;
import com.flying.util.math.DoubleUtils;

public class PositionDAOTest {
	PositionDAO dao = null;
	private final static long productCode = 600001;
	private int bizdate = 20150112;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {
		BasicDataSource bds = DataContext.getInstance().getBDS("mysql");
		DBI dbi = new DBI(bds);
		dao = dbi.open(PositionDAO.class);
	}

	@After
	public void tearDown() throws Exception {
		if (dao != null)
			dao.close();
	}

	@Test
	public void testUpdate() {
		dao.delete(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate);
		assertNull(dao.get(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate));
		dao.insert(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate, 1000, 0,
				ICyType.CNY, System.currentTimeMillis(), System.currentTimeMillis());
		assertEquals(1000, dao.get(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate)
				.getNos(), DoubleUtils.DEFAULT_DELTA);
		dao.delete(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate);
		assertNull(dao.get(Long.MAX_VALUE, productCode, IPosType.SETTLEBAL, ISettleFlag.SETTLED, bizdate, bizdate));
	}
}
