package com.flying.cms.model.dao.jdbi;

import com.flying.cms.model.ICertType;
import com.flying.util.db.DataContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.skife.jdbi.v2.DBI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CustomerDAOTest {
    CustomerDAO dao = null;
    String certNO = "TEST25209912120639";

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
        dao = dbi.open(CustomerDAO.class);
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
        dao.insert(Long.MAX_VALUE, ICertType.INDIVIDUAL_ID_CARD, certNO, "Walker.Zhang",
                System.currentTimeMillis(), System.currentTimeMillis());
        assertEquals(dao.get(Long.MAX_VALUE).getCid(), Long.MAX_VALUE);
        dao.delete(Long.MAX_VALUE);
        assertNull(dao.get(Long.MAX_VALUE));
    }
}
