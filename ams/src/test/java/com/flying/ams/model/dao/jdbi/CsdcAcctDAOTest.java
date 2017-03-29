package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.IAcctStatusId;
import com.flying.ams.model.IExchAcctType;
import com.flying.common.model.IExchId;
import com.flying.util.db.DataContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.skife.jdbi.v2.DBI;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CsdcAcctDAOTest {
    CsdcAcctDAO dao = null;
    String csdcNO = "TEST005111";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        BasicDataSource bds = DataContext.getInstance().getBDS("mysql");
        DBI dbi = new DBI(bds);
        dao = dbi.open(CsdcAcctDAO.class);
    }

    @After
    public void tearDown() throws Exception {
        if (dao != null)
            dao.close();
    }

    @Test
    public void testUpdate() {
        dao.delete(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO);
        assertNull(dao.get(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO));
        dao.insert(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO, Long.MAX_VALUE, IAcctStatusId.NORMAL,
                System.currentTimeMillis(), System.currentTimeMillis());
        assertEquals(Long.MAX_VALUE, dao.get(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO).getCustId());
        dao.delete(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO);
        assertNull(dao.get(IExchId.SHANGHAI, IExchAcctType.SHARE, csdcNO));
    }
}
