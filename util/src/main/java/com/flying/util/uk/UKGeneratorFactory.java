package com.flying.util.uk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.flying.util.cfg.IConfiguration;
import com.flying.util.cfg.DynamicConfiguration;
import com.flying.util.uk.dbimpl.DBUKGenerator;

public class UKGeneratorFactory {
	public static final String GENERATOR_SIZE = "com.flying.util.uk.generator.size";
	public static final String GENERATOR_STEP = "com.flying.util.uk.generator.step";
	public static final String GENERATOR_DSNAME = "com.flying.util.uk.generator.dsname";
	public static final String DEFAULT_STEP = "1";
	public static final String DEFAULT_SIZE = "1000000";
	public static final String DEFAULT_DSNAME = "com.flying.util.uk.mysql";

	private static IUKGenerator generator = null;
	private static final Lock lock = new ReentrantLock();

	public static IUKGenerator getUKGenerator(IConfiguration cfg) throws UKGenException {
		if (generator != null)
			return generator;
		lock.lock();
		try {
			if (generator == null) {
				int step = Integer.valueOf(cfg.getValue(GENERATOR_STEP, DEFAULT_STEP));
				int size = Integer.valueOf(cfg.getValue(GENERATOR_SIZE, DEFAULT_SIZE));
				generator = new DBUKGenerator(step, size, cfg.getValue(GENERATOR_DSNAME, DEFAULT_DSNAME));
			}
		} finally {
			lock.unlock();
		}
		return generator;
	}

	public static IUKGenerator getUKGenerator() throws UKGenException {
		if (generator != null) return generator;
		IConfiguration cfg = new DynamicConfiguration();
		cfg.put(UKGeneratorFactory.GENERATOR_STEP, DEFAULT_STEP);
		cfg.put(UKGeneratorFactory.GENERATOR_SIZE, DEFAULT_SIZE);
		cfg.put(UKGeneratorFactory.GENERATOR_DSNAME, DEFAULT_DSNAME);
		return getUKGenerator(cfg);
	}
}
