package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;

/**
 * EJB 2.0 UzivatelLoginAttempt Enterprise Bean. Entita eviduje pokusy uzivatele o prihlaseni.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="UzivatelLoginAttempt"
 *      local-jndi-name="ejb/user/LocalUzivatelLoginAttempt"
 *      display-name="UzivatelLoginAttemptEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="UzivatelLoginAttempt"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelLoginAttempt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelLoginAttempt o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatelLoginAttempt findLastByLoginAndIp(java.lang.String login, java.lang.String ip)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatelLoginAttempt findLastByLoginAndIp(java.lang.String login, java.lang.String ip)"
 *          where="login = {0} AND ip = {1} "
 *          order="created DESC"
 *          other="limit 1"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelLoginAttempt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByLoginAndIpFromRecentTime(java.lang.String login, java.lang.String ip, java.util.Date referenceTime)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByLoginAndIpFromRecentTime(java.lang.String login, java.lang.String ip, java.util.Date referenceTime)"
 *          where="login = {0} AND ip = {1} AND created > {2} "
 *          order="created DESC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelLoginAttempt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAttemptsOnlyOlderThan(java.util.Date referenceTime)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAttemptsOnlyOlderThan(java.util.Date referenceTime)"
 *          where="created < {0} AND (blockedTillTime IS NULL) "
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelLoginAttempt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBlockingOlderThan(java.util.Date referenceTime)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBlockingOlderThan(java.util.Date referenceTime)"
 *          where="blockedTillTime < {0} "
 * @ejb.persistence
 *      table-name="uzivatel_login_attempt"
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
 * @author <a href="http://www.inspire.cz">Inspire CZ, s.r.o.</a>
 */
public abstract class UzivatelLoginAttemptBean extends BaseEntityBean implements EntityBean {
    
    protected EntityContext entityContext = null;
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getLogin();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setLogin(String login);
    
    /**
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Date getCreated();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setCreated(java.util.Date created);
    
    /**
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Date getBlockedTillTime();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setBlockedTillTime(java.util.Date blockedTillTime);
    
    
     /**
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getIp();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setIp(String ip);
    
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public UzivatelLoginAttemptDetails getDetails() {
        UzivatelLoginAttemptDetails details = new UzivatelLoginAttemptDetails();
        details.setId(getId());
        details.setCreated(getCreated());
        details.setBlockedTillTime(getBlockedTillTime());
        details.setLogin(getLogin());
        details.setIp(getIp());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(UzivatelLoginAttemptDetails details) throws CreateException {
        if (details.getId() == null)
            details.setId(UzivatelLoginAttemptUtil.generateGUID(this));
        setId(details.getId());
        setCreated(details.getCreated());
        setBlockedTillTime(details.getBlockedTillTime());
        setLogin(details.getLogin());
        setIp(details.getIp());
        return null;
    }
    public void ejbPostCreate(UzivatelLoginAttemptDetails details) throws CreateException {
        
    }
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate() throws CreateException {
        setId(UzivatelLoginAttemptUtil.generateGUID(this));
        return getId();
    }
    public void ejbPostCreate() throws CreateException {
        
    }
    
    /**
     * Gets the EntityContext. To be used by classes extending this.
     * @return the EntityContext of the EJB
     */
    protected EntityContext getEntityContext() {
        return entityContext;
    }
    
    
    /**
     * @ejb.select
     * query="SELECT COUNT(o) FROM UzivatelLoginAttempt o where o.login = ?1 AND o.ip = ?2 AND o.created > ?3"
     */
    public abstract Long ejbSelectCountLoginAttemptsInTime(String login, String ip, Date backInTime) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountLoginAttemptsInTime(String login, String ip, int timeMinsBack) throws FinderException {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, timeMinsBack*(-1));
        
        return ejbSelectCountLoginAttemptsInTime(login, ip, cal.getTime());
    }
}
