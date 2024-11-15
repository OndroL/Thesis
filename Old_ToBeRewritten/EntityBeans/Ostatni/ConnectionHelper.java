/**
 * Clubspire, (c) Inspire CZ 2004-2016
 *
 * ConnectionHelper.java
 * Created on: 13.5.2016
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.common;

import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBContext;
import javax.ejb.FinderException;

import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

/**
 * ConnectionHelper
 *
 * @version 1.0
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class ConnectionHelper {

    private static final String DATASOURCE_ENV_NAME = "java:BookingSystemDB";
    
    protected abstract Logger getLogger();
    
    protected abstract EJBContext getContext();
    
    public QueryRunner getQueryRunner() {
        DataSource ds = (DataSource) getContext().lookup(DATASOURCE_ENV_NAME);
        QueryRunner qr = new QueryRunner(ds);
        return qr;
    }
    
    public Connection getConnection() {
        try  {
            DataSource ds = (javax.sql.DataSource) getContext().lookup(DATASOURCE_ENV_NAME);
            return ds.getConnection();
        } catch(Exception e) {
            getLogger().error("Unable to retrieve connection!", e);
            return null;
        }
    }
    
    public <T extends Object> void closeResources(T... resources) {
        Set<T> prioritizedSet = new LinkedHashSet<T>();
        for (T obj : resources) {
            if (! (obj instanceof Connection) && ! (obj instanceof PreparedStatement)) {
                prioritizedSet.add(obj);
            }
        }
        
        for (T obj : resources) {
            if (! (obj instanceof Connection)) {
                prioritizedSet.add(obj);
            }
        }
        
        prioritizedSet.addAll(Arrays.asList(resources));
        
        for (T t : prioritizedSet) {
            closeResource(t);
        }
    }
    
    public void closeResources(List<? extends Object> resources) {
        Set<Object> prioritizedSet = new LinkedHashSet<Object>();
        for (Object obj : resources) {
            if (! (obj instanceof Connection) && ! (obj instanceof PreparedStatement)) {
                prioritizedSet.add(obj);
            }
        }
        for (Object obj : resources) {
            if (! (obj instanceof Connection)) {
                prioritizedSet.add(obj);
            }
        }
        
        prioritizedSet.addAll(Arrays.asList(resources));
        
        for (Object t : prioritizedSet) {
            closeResource(t);
        }
    }
    
    private void closeResource(Object object) {
        if (object != null) { 
            try {
                if (object instanceof Closeable) {
                    Closeable cl = (Closeable) object;
                    cl.close();
                } else if (object instanceof Statement) {
                    Statement stat = (Statement) object;
                    stat.close();
                } else if (object instanceof ResultSet) {
                    ResultSet rSet = (ResultSet) object;
                    rSet.close();
                } else if (object instanceof Connection) {
                    Connection conn = (Connection) object;
                    conn.close();
                } else if (object instanceof Closeable) {
                    Closeable csb = (Closeable) object;
                    csb.close();
                } else {
                    getLogger().error("Nepodporovany typ objektu! " + object.getClass());
                }
            } catch (Exception ex) {
                getLogger().error("Nepodarilo se uzavrit otevreny zdroj!", ex);
            }
        }
    }
    
    private Collection performQuery(final String query, boolean update, Object... params) throws FinderException {
        Collection kolekce = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            int index = 1;
            for (Object o : params) {               
                if (o instanceof String) {
                    ps.setString(index, (String) o);
                } else if (o instanceof Date) {
                    ps.setTimestamp(index, new Timestamp(((Date) o).getTime()));
                } else if (o instanceof BigDecimal) {
                    ps.setBigDecimal(index, (BigDecimal) o);
                } else if (o instanceof Integer) {
                    ps.setInt(index, (Integer) o);
                }
                index++;
            }
            
            if (update) {
                ps.executeUpdate();
                return null;
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                kolekce.add(rs.getString(1));
            }
        } catch (Exception e) {
            throw new FinderException(e.toString());
        } finally {
            closeResources(rs, ps, con);
        }
        return kolekce;
    }
    
    public Collection executeQuery(final String query, Object... params) throws FinderException {
        return performQuery(query, false, params);
    }
    
    public void updateQuery(final String query, Object... params) throws FinderException {
        performQuery(query, true, params);
    }
}
