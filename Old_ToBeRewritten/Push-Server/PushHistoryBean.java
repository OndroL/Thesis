/**
 * Clubspire, (c) Inspire CZ 2004-2017
 *
 * PushHistoryBean.java
 * Created on: Sep 6, 2017
 * Author: Iveta Jurcikova
 *
 */
package cz.inspire.enterprise.module.push;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 PushHistoryBean. Entita reprezentuje jedenu odoslanu push notifikaciu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="PushHistory"
 *      local-jndi-name="ejb/push/LocalPushHistory"
 *      display-name="PushHistoryEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="PushHistory"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushHistory ph"
 *      result-type-mapping="Local"
 *      signature="LocalPushHistory findByMulticastIdUzivatel(java.lang.String uzivatelId, java.lang.String multicastId)"
 *      @jboss.declared-sql
 *          signature="LocalPushHistory findByMulticastIdUzivatel(java.lang.String uzivatelId, java.lang.String multicastId)"
 *          where="(uzivatelId = {0}) AND (pushmulticast = {1})" 
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushHistory ph"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findSuccessfulSentByUzivatel(java.lang.String uzivatelId, java.lang.Boolean removed, java.util.Date lastDate, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findSuccessfulSentByUzivatel(java.lang.String uzivatelId, java.lang.Boolean removed, java.util.Date lastDate, int offset, int count)"
 *          from=", push_multicast pm"
 *          where="pushmulticast = pm.id AND uzivatelId = {0} AND removed = {1} AND pm.sent = TRUE AND pm.date > {2}" 
 *          order="read ASC, pm.date DESC"
 *          other="LIMIT {4} OFFSET {3}"
 * @ejb.persistence
 *      table-name="push_history"
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
 * @version 1.0
 * @author <a href="mailto:iveta.jurcikova@inspire.cz">Iveta Jurcikova</a>
 */
public abstract class PushHistoryBean extends BaseEntityBean implements EntityBean {
    
    private final Logger logger = Logger.getLogger(PushHistoryBean.class);
    
    @Override
    protected Logger getLogger() { return logger; }   
    
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
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getUzivatelId();
    public abstract void setUzivatelId(String uzivatelId);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Boolean getRead();

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setRead(java.lang.Boolean isRead);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Boolean getRemoved();

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setRemoved(java.lang.Boolean isRemoved);
    
    /**
     * @ejb.persistent-field     
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *      name="pushmulticast"
     */
    public abstract String getMulticastId();
    public abstract void setMulticastId(String MulticastId);

    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PushHistoryDetails getDetails() {
        PushHistoryDetails details = new PushHistoryDetails();
        details.setId(getId());
        details.setUzivatelId(getUzivatelId());
        details.setMulticastId(getMulticastId());
        details.setRemoved(getRemoved());
        details.setRead(getRead());

        return details;
    }
    
     /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(PushHistoryDetails details) throws NamingException, FinderException {
        setUzivatelId(details.getUzivatelId());
        setMulticastId(details.getMulticastId());
        setRead(details.getRead());
        setRemoved(details.getRemoved());
    }
    
    // Entity methods ------------------------------------------------------------------------------    
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     * @ejb.transaction
     *    type="RequiresNew"
     */
    public String ejbCreate(PushHistoryDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(PushHistoryUtil.generateGUID(this));
        }
        setId(details.getId());
        setUzivatelId(details.getUzivatelId());
        setRead(details.getRead());
        setRemoved(details.getRemoved());

        return getId();
    }

    public void ejbPostCreate(PushHistoryDetails details) throws CreateException {
    }
    
}