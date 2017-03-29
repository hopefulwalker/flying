/*
  File: DBUKGenerator.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    retrive this from flygroup.
                                The perfermance is poor compare to java.util.UUID.
 */
package com.flying.util.uk.dbimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.skife.jdbi.v2.DBI;

import com.flying.util.db.DataContext;
import com.flying.util.uk.UKGenException;
import com.flying.util.uk.IUKGenerator;

public class DBUKGenerator implements IUKGenerator {
	private int step;
	private int retriveSize;
	private String dsName;

	private Map<String, UKInfo> cache;
	private Map<String, Lock> locks;

	public DBUKGenerator(int step, int retriveSize, String dsName) {
		this.step = step;
		this.retriveSize = retriveSize;
		this.dsName = dsName;
		cache = new HashMap<>();
		locks = new HashMap<>();
		prepareTable();
	}

	@Override
	public long generate(String name) throws UKGenException {
		long key = 0;
		Lock lock = null;
		if (cache.containsKey(name)) {
			UKInfo info = cache.get(name);
			lock = locks.get(name);
			lock.lock();
			try {
				key = info.getBeginUK();
				if (key <= info.getEndUK())
					info.setBeginUK(key + step);
				else
					key = generateAfterRefresh(name);
			} finally {
				lock.unlock();
			}
		} else {
			synchronized (this) {
				lock = new ReentrantLock();
				locks.put(name, lock);
				key = generateAfterRefresh(name);
			}
		}
		return key;
	}

	@Override
	public void erase(String name) throws UKGenException {
		Lock lock = null;
		if (cache.containsKey(name)) {
			lock = locks.get(name);
			lock.lock();
			try {
				cache.remove(name);
				locks.remove(name);
				eraseDBRecord(name);
			} finally {
				lock.unlock();
			}
		} else {
			synchronized (this) {
				eraseDBRecord(name);
			}
		}
	}

	private void eraseDBRecord(String name) {
		DBUKInfoDAO dao = null;
		try {
			DBI dbi = new DBI(DataContext.getInstance().getBDS(dsName));
			dao = dbi.open(DBUKInfoDAO.class);
			dao.delete(name);
		} catch (Throwable t) {
			throw new UKGenException("Error in refresh uk information", t);
		} finally {
			if (dao != null)
				dao.close();
		}
	}

	private long generateAfterRefresh(String name) {
		long key = 0;
		DBUKInfoDAO dao = null;
		UKInfo info = null;
		try {
			DBI dbi = new DBI(DataContext.getInstance().getBDS(dsName));
			dao = dbi.open(DBUKInfoDAO.class);
			info = dao.refresh(name, step, retriveSize);
			key = info.getBeginUK();
			info.setBeginUK(key + step);
			cache.put(name, info);
		} catch (Throwable t) {
			throw new UKGenException("Error in refresh uk information", t);
		} finally {
			if (dao != null)
				dao.close();
		}
		return key;
	}

	private void prepareTable() {
		DBUKInfoDAO dao = null;
		try {
			DBI dbi = new DBI(DataContext.getInstance().getBDS(dsName));
			dao = dbi.open(DBUKInfoDAO.class);
			dao.createTable();
		} catch (Throwable t) {
			throw new UKGenException("Error in prepare the table", t);
		} finally {
			if (dao != null)
				dao.close();
		}
	}
}