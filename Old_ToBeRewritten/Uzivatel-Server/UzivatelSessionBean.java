/**
 * Uzivatel-Server, (c) Inspire CZ 2004-2006
 *
 * UzivatelSessionBean.java
 * Vytvoreno: 20.1.2004
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospisil</a>
 *
 */
package cz.inspire.enterprise.module.user;

import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

/**
 * EJB 2.0 UzivatelSession Enterprise Bean. Entita reprezentuje uzivatelske sezeni.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="UzivatelSession"
 *      local-jndi-name="ejb/user/LocalUzivatelSession"
 *      display-name="UzivatelSessionEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="UzivatelSession"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatelSession findLast(java.lang.String login)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatelSession findLast(java.lang.String login)"
 *          where="logintime IN ( SELECT MAX(logintime) FROM uzivatel_session WHERE uzivatel={0})"
 *          other="limit 1"
 *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatelSession findLastRestLogin(java.lang.String login)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatelSession findLastRestLogin(java.lang.String login)"
 *          where="logintime IN ( SELECT MAX(logintime) FROM uzivatel_session WHERE uzivatel={0} AND logintype = '3')"
 *          other="limit 1"
 *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllInDates(java.util.Date from, java.util.Date to)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllInDates(java.util.Date from, java.util.Date to)"
 *          from=", uzivatel"
 *          where="uzivatel_session.uzivatel = uzivatel.login AND uzivatel.skupina != 'web' AND ((logintime <= {1}) AND ({0} <= logouttime OR logouttime IS NULL))"
  *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllInDatesWithLogin(java.util.Date from, java.util.Date to, java.lang.String loginName)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllInDatesWithLogin(java.util.Date from, java.util.Date to, java.lang.String loginName)"
 *          from=", uzivatel"
 *          where="uzivatel_session.uzivatel = uzivatel.login AND uzivatel.skupina != 'web' AND ((logintime <= {1}) AND ({0} <= logouttime OR (logouttime IS NULL))) AND uzivatel.login = {2}"
 *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o WHERE o.logoutTime IS NULL"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findLogged()"
 *  @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM UzivatelSession o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findLogged(int type)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findLogged(int type)"
 *          from=", uzivatel as uz"
 *          where="logoutTime IS NULL AND loginType = {0} AND uzivatel = uz.login AND uz.login <> 'admin'"
 * @ejb.persistence
 *      table-name="uzivatel_session"
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
public abstract class UzivatelSessionBean implements EntityBean {
    
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
     * CMF loginTime methods.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Date getLoginTime();
    public abstract void setLoginTime(java.util.Date loginTime);
    
    /**
     * CMF logoutTime methods.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Date getLogoutTime();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setLogoutTime(java.util.Date logout);
    
    /**
     * CMF loginType methods.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getLoginType();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setLoginType(int loginType);
    
    /**
     * CMF logoutMethod methods.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getLogoutMethod();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setLogoutMethod(int logoutMethod);

     /**
     * CMF IP address methods.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getIPAddress();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setIPAddress(String ipAddress);
    
     /**
     * Client id used by user.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getClientId();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setClientId(String clientId);
    
    /**
     * Last active time.
     *
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Date getLastActiveTime();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setLastActiveTime(Date clientId);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * CMR admin methods.
     *
     * @ejb.interface-method
     *   view-type="local"
     * @ejb.relation
     *   name="uzivatel-uzivatel_session"
     *   role-name="uzivatel_session-reprezentuje-prihlaseni-uzivatele"
     * @jboss.relation
     *   related-pk-field="login"
     *   fk-column="uzivatel"
     * @ejb.value-object
     *   aggregate="java.lang.String"
     *   aggregate-name="Uzivatel"
     */
    public abstract LocalUzivatel getUzivatel();
    public abstract void setUzivatel(LocalUzivatel uzivatel);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public UzivatelSessionDetails getDetails() {
        UzivatelSessionDetails details = new UzivatelSessionDetails();
        details.setId(getId());
        details.setLoginTime(getLoginTime());
        details.setLogoutTime(getLogoutTime());
        details.setLoginType(getLoginType());
        details.setLogoutMethod(getLogoutMethod());
        details.setUzivatel(getUzivatel().getLogin());
        details.setIPAddress(getIPAddress());
        details.setClientId(getClientId());
        details.setLastActiveTime(getLastActiveTime());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(UzivatelSessionDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(UzivatelSessionUtil.generateGUID(this));
        }
        setId(details.getId());
        
        if (details.getLoginTime() == null) {
            details.setLoginTime(new Date());
        }
        
        setLoginTime(details.getLoginTime());
        setLogoutTime(details.getLogoutTime());
        setLoginType(details.getLoginType());
        setLogoutMethod(details.getLogoutMethod());
        setIPAddress(details.getIPAddress());    
        setClientId(details.getClientId());
        setLastActiveTime(details.getLastActiveTime());
        return details.getId();
    }
    
    public void ejbPostCreate(UzivatelSessionDetails details) throws CreateException {
        if (details.getUzivatel() != null) {
            try {
                LocalUzivatel uzivatel =
                    UzivatelUtil.getLocalHome().findByPrimaryKey(details.getUzivatel());
                setUzivatel(uzivatel);
            } catch (Exception e) {
                entityContext.setRollbackOnly();
                throw new CreateException("Could not set Uzivatel.");
            }
        } else {
            entityContext.setRollbackOnly();
            throw new CreateException("Empty uzivatel not allowed.");
        }
    }
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(int loginType, LocalUzivatel uzivatel) throws CreateException {
        setId(UzivatelSessionUtil.generateGUID(this));
        setLoginTime(new Date());
        setLoginType(loginType);
        setLogoutMethod(UzivatelSessionLoggingState.LOGOUT_NONE.getNumber());
        return null;
    }
    
    public void ejbPostCreate(int loginType, LocalUzivatel uzivatel) throws CreateException {
        if (uzivatel == null) {
            entityContext.setRollbackOnly();
            throw new CreateException("Empty uzivatel not allowed.");
        }
        setUzivatel(uzivatel);
    }    
    
    /**
     * Gets the EntityContext. To be used by classes extending this.
     * @return the EntityContext of the EJB
     */
    protected EntityContext getEntityContext() {
        return entityContext;
    }
    
    /** Required to implement EntityBean. Sets the EntityContext. */
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }
    /** Required to implement EntityBean. Sets the EntityContext to null. */
    public void unsetEntityContext() throws EJBException {
        entityContext = null;
    }
    
    /** Required to implement EntityBean. Not implemented. */
    public void ejbActivate() throws EJBException {
    }
    
    /** Required to implement EntityBean. Not implemented. */
    public void ejbPassivate() throws EJBException {
    }
    
    /** Required to implement EntityBean. Not implemented. */
    public void ejbLoad() throws EJBException {
    }
    
    /** Required to implement EntityBean. Not implemented. */
    public void ejbStore() throws EJBException {
    }
    
    /** Required to implement EntityBean. Not implemented. */
    public void ejbRemove() throws RemoveException, EJBException {
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(o.id) FROM UzivatelSession o WHERE o.loginTime > ?1 "
     */
    public abstract Long ejbSelectCountNewerSessions(Date date) throws FinderException;
                
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountNewerSessions(Date date) throws FinderException {
        return ejbSelectCountNewerSessions(date);
    }
}
