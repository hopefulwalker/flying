/*
  File: DataContext.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.4     walker  0.1.0    Create this file.
 */
package com.flying.util.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DataContext {
    private static final DataContext me = new DataContext();
    private static final Object dsmLock = new Object();
    private Map<String, BasicDataSource> cache = null;

    private DataContext() {
        cache = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Get instance of data context. Singleton class implemention
     *
     * @return Instance of this class
     */
    public static DataContext getInstance() {
        return me;
    }

    /**
     * lookup the data source according to the name.
     *
     * @param name
     * @return Data source according to the dbName
     * @throws DSManagerException
     */
    public BasicDataSource lookup(String name) {
        BasicDataSource bds = null;
        if (cache.containsKey(name)) {
            bds = (BasicDataSource) cache.get(name);
        }
        return bds;
    }

    public BasicDataSource getBDS(String name) throws DSManagerException {
        BasicDataSource bds = lookup(name);
        if (bds != null)
            return bds;
        synchronized (dsmLock) {
            bds = lookup(name);
            if (bds == null) {
                bds = DSManager.createDataSource(name);
                bind(name, bds);
            }
        }
        return bds;
    }

    private void bind(String name, BasicDataSource bds) {
        cache.put(name, bds);
    }

    private void unbind(String name) {
        cache.remove(name);
    }

    public void release() throws SQLException {
        Collection<BasicDataSource> values = cache.values();
        for (BasicDataSource bds : values)
            bds.close();
        cache.clear();
    }

    public void release(String name) throws SQLException {
        if (cache.containsKey(name)) {
            cache.get(name).close();
            unbind(name);
        }
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
