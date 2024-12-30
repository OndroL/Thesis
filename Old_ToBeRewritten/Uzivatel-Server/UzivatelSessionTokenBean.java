/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * UzivatelSessionTokenBean.java
 * Created on: 12.6.2013
 * Author: Tomáš Kramec
 *
 */
package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;

/**
 * EJB 2.0 UzivatelSessionTokenBean. Entita reprezentuje session uzivatela prihlaseneho z web klienta.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="UzivatelSessionToken"
 *      local-jndi-name="ejb/user/LocalUzivatelSessionToken"
 *      display-name="UzivatelSessionTokenEJB"
 *      view-type="local"
 *      primkey-field="series"
 * @ejb.value-object
 *      name="UzivatelSessionToken"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSessionToken o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatelSessionToken findByUsername(java.lang.String username)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatelSessionToken findByUsername(java.lang.String username)"
 *          where="username={0}"
 *          other="limit 1"
 * @ejb.persistence
 *      table-name="uzivatel_session_token"
 *      @jboss.persistence
 *          create-table="true"
 *          data-source="jdbc/BookingSystemDB"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.container-configuration
 *      name="Standard CMP 2.x EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * 
 * Uchovava data nutne pre remember-me autentizaciu prostrednictvom web klienta.
 * @version 1.0
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public abstract class UzivatelSessionTokenBean  extends BaseEntityBean implements EntityBean {
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     */
    public abstract String getSeries();
    public abstract void setSeries(String series);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getUsername();
    public abstract void setUsername(String username);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getToken();
    public abstract void setToken(String token);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract Date getLastUsed();
    public abstract void setLastUsed(Date lastUsed);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public UzivatelSessionTokenDetails getDetails() {
        return new UzivatelSessionTokenDetails(getSeries(), getUsername(), getToken(), getLastUsed());
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(UzivatelSessionTokenDetails details) {
        setLastUsed(details.getLastUsed());
        setToken(details.getToken());
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(UzivatelSessionTokenDetails token) throws CreateException {
        setLastUsed(token.getLastUsed());
        setSeries(token.getSeries());
        setToken(token.getToken());
        setUsername(token.getUsername());
        return getSeries();
    }
    public void ejbPostCreate(UzivatelSessionTokenDetails token) throws CreateException {}
    
    /**
     * @ejb.select
     * query="SELECT COUNT(t.series) FROM UzivatelSessionToken AS t WHERE t.username = ?1"
     */
    public abstract Long ejbSelectCountByUsername(String username) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountByUsername(String username) throws FinderException {
        return ejbSelectCountByUsername(username);
    }

}
