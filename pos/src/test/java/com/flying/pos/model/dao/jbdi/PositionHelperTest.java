package com.flying.pos.model.dao.jbdi;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.flying.common.model.ICyType;
import com.flying.pos.model.ISettleFlag;
import com.flying.pos.model.IPosType;
import com.flying.pos.model.PositionHelper;
import org.junit.*;

import com.flying.pos.model.Position;
import com.flying.util.math.DoubleUtils;
import com.flying.util.math.IntegerUtils;

public class PositionHelperTest {

	static List<Position> positions = new ArrayList<>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		int curDate = IntegerUtils.getDate(System.currentTimeMillis());		
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.SETTLED, curDate-1, curDate-1, 1000,
				100, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.SETTLED, curDate, curDate, 2000,
				100, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.UNSETTLED, curDate-1, curDate,
				1000, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.UNSETTLED, curDate,  curDate+1,
				-500, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.UNSETTLED, curDate, curDate+2,
				-200, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.AVBAL, ISettleFlag.UNSETTLED, curDate, curDate+3, 200,
				0, ICyType.CNY, 0, 0));

		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.SETTLEBAL, ISettleFlag.SETTLED, curDate-1, curDate-1,
				5000, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.SETTLEBAL, ISettleFlag.UNSETTLED, curDate-1, curDate,
				-1000, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.SETTLEBAL, ISettleFlag.UNSETTLED, curDate-1, curDate,
				500, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.SETTLEBAL, ISettleFlag.SETTLED, curDate, curDate,
				4500, 0, ICyType.CNY, 0, 0));
		positions.add(new Position(Long.MAX_VALUE, "CASH-CNY", IPosType.SETTLEBAL, ISettleFlag.UNSETTLED, curDate, curDate+1,
				-4500, 0, ICyType.CNY, 0, 0));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		positions.clear();
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetAvTradeBalListOfPositionShortIntInt() {
		int curDate = IntegerUtils.getDate(System.currentTimeMillis());
		Assert.assertEquals(900, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate - 1, curDate - 1),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1900, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate-1, curDate),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1200, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate, curDate),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1200, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate, curDate+1),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1200, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate, curDate+2),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1400, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate, curDate+3),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(1400, PositionHelper.getAvTradeBal(positions, ICyType.CNY, curDate+10, curDate+10),
				DoubleUtils.DEFAULT_DELTA);
	}
	
	@Test
	public void testGetAvTransBalListOfPositionShortIntInt() {
		int curDate = IntegerUtils.getDate(System.currentTimeMillis());
		assertEquals(4000, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate-1, curDate-1),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(4500, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate-1, curDate),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(4500, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate-1, curDate+1),
				DoubleUtils.DEFAULT_DELTA);

		assertEquals(0, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate, curDate),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(0, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate, curDate+1),
				DoubleUtils.DEFAULT_DELTA);
		assertEquals(0, PositionHelper.getAvTransBal(positions, ICyType.CNY, curDate, curDate+2),
				DoubleUtils.DEFAULT_DELTA);
	}	
}
