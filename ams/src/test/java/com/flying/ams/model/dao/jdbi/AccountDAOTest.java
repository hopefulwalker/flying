package com.flying.ams.model.dao.jdbi;

import com.flying.ams.model.IAcctStatusId;
import com.flying.ams.model.IAcctType;
import com.flying.util.db.DataContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.skife.jdbi.v2.DBI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountDAOTest {
    AccountDAO dao = null;

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
        dao = dbi.open(AccountDAO.class);
    }

    @After
    public void tearDown() throws Exception {
        if (dao != null)
            dao.close();
    }

    @Test
    public void testUpdate() {
        dao.delete(Long.MAX_VALUE);
        assertNull(dao.get(Long.MAX_VALUE));
        dao.insert(Long.MAX_VALUE, IAcctType.MASTER, Long.MAX_VALUE, Long.MAX_VALUE, IAcctStatusId.NORMAL,
                System.currentTimeMillis(), System.currentTimeMillis());
        assertEquals(Long.MAX_VALUE, dao.get(Long.MAX_VALUE).getCustId());
        dao.delete(Long.MAX_VALUE);
        assertNull(null, dao.get(Long.MAX_VALUE));
    }
}