package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.IExchAcctType;
import com.flying.common.model.IExchId;
import com.flying.util.db.DataContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.skife.jdbi.v2.DBI;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CsdcAcctLinkDAOTest {
    CsdcAcctLinkDAO dao = null;
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
        dao = dbi.open(CsdcAcctLinkDAO.class);
    }

    @After
    public void tearDown() throws Exception {
        if (dao != null)
            dao.close();
    }

    @Test
    public void testUpdate() {
        dao.delete(IExchId.SHANGHAI, IExchAcctType.FUND, csdcNO, Long.MAX_VALUE);
        assertEquals(0, dao.get(Long.MAX_VALUE).size());
        dao.insert(IExchId.SHANGHAI, IExchAcctType.FUND, csdcNO, Long.MAX_VALUE,
                System.currentTimeMillis(), System.currentTimeMillis());
        assertEquals(1, dao.get(Long.MAX_VALUE).size());
        assertEquals(Long.MAX_VALUE, dao.get(Long.MAX_VALUE).get(0).getAcctId());
        dao.delete(IExchId.SHANGHAI, IExchAcctType.FUND, csdcNO, Long.MAX_VALUE);
        assertEquals(0, dao.get(Long.MAX_VALUE).size());
    }

}
