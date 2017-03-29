package com.flying.util.uk.dbimpl;

import com.flying.util.cfg.IConfiguration;
import com.flying.util.cfg.DynamicConfiguration;
import com.flying.util.cfg.ReadOnlyConfiguration;
import com.flying.util.uk.IUKGenerator;
import com.flying.util.uk.UKGeneratorFactory;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Walker.Zhang
 */
public class DBUKGeneratorTest {
    private static IConfiguration cfg = null;
    private static String STEP = "3";
    private static String SIZE = "1000000";
    IUKGenerator generator;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        cfg = new DynamicConfiguration();
        cfg.put(UKGeneratorFactory.GENERATOR_STEP, STEP);
        cfg.put(UKGeneratorFactory.GENERATOR_SIZE, SIZE);
        cfg.put(UKGeneratorFactory.GENERATOR_DSNAME, "mysql");
        cfg = new ReadOnlyConfiguration(cfg);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        generator = UKGeneratorFactory.getUKGenerator(cfg);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.flying.util.uk.dbimpl.DBUKGenerator#generate(java.lang.String)}.
     */
    @Test
    public void testGenerateErase() {
        String name = UUID.randomUUID().toString();
        long key = generator.generate(name);
        assertEquals(Long.parseLong(STEP), key);
        key = generator.generate(name);
        assertEquals(Long.parseLong(STEP) * 2, key);
        generator.erase(name);
        key = generator.generate(name);
        assertEquals(Long.parseLong(STEP), key);
        generator.erase(name);
    }

    /**
     * Test method for {@link com.flying.util.uk.dbimpl.DBUKGenerator#generate(java.lang.String)}.
     */
    @Test(timeout = 500)
    public void testPerfermance() {
        String name = UUID.randomUUID().toString();
        long key = 0;
        int times;
        for (times = 0; times < 100000; times++)
            key = generator.generate(name);
        assertEquals(Long.parseLong(STEP) * times, key);
        generator.erase(name);
    }
}
