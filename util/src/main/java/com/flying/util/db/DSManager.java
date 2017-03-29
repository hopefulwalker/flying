/*
  File: DSManager.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.4     walker  0.1.0    Create this file.
 */
package com.flying.util.db;

import com.flying.util.cfg.IConfiguration;
import com.flying.util.cfg.PropertiesFileConfiguration;
import com.flying.util.crypto.Cypher;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DSManager {
    public static final String USER_PWD_AUTOENCRYPT = "com.flying.util.db.userpwd.autoencrypt";
    private static final Logger logger = LoggerFactory.getLogger(DSManager.class);

    private static final String DEFAULT_CFG_FILE_NAME = "dsmanager";
    private static final String CFG_DRIVER_POSTFIX = ".driverclassname";
    private static final String CFG_URL_POSTFIX = ".url";
    private static final String CFG_USER_POSTFIX = ".username";
    private static final String CFG_PWD_POSTFIX = ".password";
    private static final String CFG_INITIALSIZE_POSTFIX = ".initialsize";
    private static final String CFG_MAXTOTAL_POSTFIX = ".maxtotal";

    private static final String CFG_LOGABANDONED_POSTFIX = "logabandoned";
    private static final String CFG_TESTONBORROW_POSTFIX = ".testonborrow";
    private static final String CFG_TESTONRETURN_POSTFIX = ".testonreturn";

    private static final String DEFAULT_INITIALSIZE = "1";
    private static final String DEFAULT_MAXTOTAL = "5";
    private static final String DEFAULT_TESTONBORROW = "true";
    private static final String DEFAULT_TESTONRETURN = "false";
    private static final String DEFAULT_AUTOENCRYPT = "false";
    private static final String DEFAULT_LOGABANDONED = "true";

    public static BasicDataSource createDataSource(String dbName) throws DSManagerException {
        IConfiguration cfg = new PropertiesFileConfiguration(DEFAULT_CFG_FILE_NAME);
        return DSManager.createDataSource(cfg, dbName);
    }

    public static BasicDataSource createDataSource(IConfiguration cfg, String dbName) throws DSManagerException {
        try {
            // Check the configuration items.
            if (!cfg.containsKey(dbName + CFG_DRIVER_POSTFIX) || !cfg.containsKey(dbName + CFG_URL_POSTFIX)
                    || !cfg.containsKey(dbName + CFG_USER_POSTFIX) || !cfg.containsKey(dbName + CFG_PWD_POSTFIX)
                    || !cfg.containsKey(dbName + CFG_INITIALSIZE_POSTFIX) || !cfg.containsKey(dbName + CFG_MAXTOTAL_POSTFIX)) {
                cfg = new PropertiesFileConfiguration(DEFAULT_CFG_FILE_NAME);
            }
            String driver = cfg.getValue(dbName + CFG_DRIVER_POSTFIX);
            String url = cfg.getValue(dbName + CFG_URL_POSTFIX);
            String user = cfg.getValue(dbName + CFG_USER_POSTFIX);
            String pwd = cfg.getValue(dbName + CFG_PWD_POSTFIX);
            String autoencrypt = cfg.getValue(USER_PWD_AUTOENCRYPT, DEFAULT_AUTOENCRYPT);
            if (autoencrypt.equalsIgnoreCase("true") && user != null && pwd != null) {
                // if encrypted, just decrypt.
                // if plain, encrypt and save configuration.
                boolean modified = false;
                if (user.charAt(0) == Cypher.CYPHER_STRING_BEGIN_CHAR
                        && user.charAt(user.length() - 1) == Cypher.CYPHER_STRING_END_CHAR) {
                    user = Cypher.decrypt(user);
                } else {
                    cfg.put(dbName + CFG_USER_POSTFIX, Cypher.encrypt(user));
                    modified = true;
                }
                if (pwd.charAt(0) == Cypher.CYPHER_STRING_BEGIN_CHAR
                        && pwd.charAt(pwd.length() - 1) == Cypher.CYPHER_STRING_END_CHAR) {
                    pwd = Cypher.decrypt(pwd);
                } else {
                    cfg.put(dbName + CFG_PWD_POSTFIX, Cypher.encrypt(pwd));
                    modified = true;
                }
                if (modified)
                    try {
                        cfg.save();
                    } catch (Exception e) {
                        logger.warn("Failed to save the encrypted user name and password!", e);
                    }
            }
            int initialSize = Integer.parseInt(cfg.getValue(dbName + CFG_INITIALSIZE_POSTFIX, DEFAULT_INITIALSIZE));
            int maxTotal = Integer.parseInt(cfg.getValue(dbName + CFG_MAXTOTAL_POSTFIX, DEFAULT_MAXTOTAL));
            boolean testOnBorrow = BooleanUtils.toBoolean(cfg.getValue(dbName + CFG_TESTONBORROW_POSTFIX,
                    DEFAULT_TESTONBORROW));
            boolean testOnReturn = BooleanUtils.toBoolean(cfg.getValue(dbName + CFG_TESTONRETURN_POSTFIX,
                    DEFAULT_TESTONRETURN));
            boolean logAbandoned = BooleanUtils.toBoolean(cfg.getValue(dbName + CFG_LOGABANDONED_POSTFIX,
                    DEFAULT_LOGABANDONED));
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName(driver);
            bds.setUrl(url);
            bds.setUsername(user);
            bds.setPassword(pwd);
            bds.setInitialSize(initialSize);
            bds.setMaxTotal(maxTotal);
            bds.setTestOnBorrow(testOnBorrow);
            bds.setTestOnReturn(testOnReturn);
            bds.setLogAbandoned(logAbandoned);
            return bds;
        } catch (Exception e) {
            // InitializeException, CypherException
            throw new DSManagerException("Fail to create data source!" + dbName, e);
        }
    }
}