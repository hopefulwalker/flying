package com.flying.framework.fsm;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IStateEventTest {
    private static final Logger logger = LoggerFactory.getLogger(IStateEventTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testString() throws Exception {
        System.out.println("cao");
        byte[] by = new byte[20];
        by[0] = 66;
        by[2] = 3;
        for (int i = 0; i < 10; i++)
            by[i] = 66;
        String str = new String(by, "UTF-8").trim();
        System.out.println("out");
        System.out.println(str);
    }

}
