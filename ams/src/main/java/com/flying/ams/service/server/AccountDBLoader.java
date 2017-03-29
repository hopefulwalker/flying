/**
 * Created by Walker.Zhang on 2015/3/29.
 * Revision History:
 * Date          Who              Version      What
 * 2015/3/29     Walker.Zhang     0.1.0        Created.
 * 2015/4/6      Walker.Zhang     0.1.1        split the service to client and server service.
 */
package com.flying.ams.service.server;

import com.flying.ams.model.AccountBO;
import com.flying.ams.model.dao.jdbi.AccountDAO;
import com.flying.util.db.DSManagerException;
import com.flying.util.db.DataContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.skife.jdbi.v2.DBI;

public class AccountDBLoader {
    public AccountBO load(long id) throws DSManagerException {
        // load from db.
        AccountDAO dao = null;
        BasicDataSource bds = null;
        AccountBO acctBO = null;
        // get the account now.
        try {
            bds = DataContext.getInstance().getBDS("mysql");
            DBI dbi = new DBI(bds);
            dao = dbi.open(AccountDAO.class);
            acctBO = dao.get(id);
        } catch (Exception e) {
            throw new DSManagerException(e);
        } finally {
            if (dao != null) dao.close();
        }
        return acctBO;
    }
}
