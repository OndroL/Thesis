/**
 * Clubspire, (c) Inspire CZ 2004-2012
 *
 * SMSHistoryBean.java
 * Created on: 8.11.2012
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sms;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

/**
 * EJB 2.0 SMSHistoryBean Bean. Entita reprezentuje odeslanou SMS (at uz hromadne nebo automaticky).
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="SMSHistory"
 *      local-jndi-name="ejb/sms/LocalSMSHistory"
 *      display-name="SMSHistoryEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="SMSHistory"
 *      match="ent"
 * @ejb.persistence
 *      table-name="sms_history"
 *      @jboss.persistence
 *          create-table="true"
 *          data-source="jdbc/BookingSystemDB"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SMSHistory o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDate(java.util.Date dateFrom, java.util.Date dateTo)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDate(java.util.Date dateFrom, java.util.Date dateTo)"
 *          order="date DESC"
 *          where="(date >= {0}) AND (date <= {1})"
 *          strategy="on-find"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SMSHistory o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, boolean automatic)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, boolean automatic)"
 *          order="date DESC"
 *          where="(date >= {0}) AND (date <= {1}) AND (automatic = {2})"
 *          strategy="on-find"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.container-configuration
 *      name="Standard CMP 2.x EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @version 1.0
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class SMSHistoryBean extends BaseEntityBean implements EntityBean {

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
    public abstract java.util.Date getDate();
    public abstract void setDate(java.util.Date date);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getMessage();
    public abstract void setMessage(java.lang.String message);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.util.Collection getGroups();
    public abstract void setGroups(java.util.Collection groups);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.util.Collection getRecipients();
    public abstract void setRecipients(java.util.Collection recipients);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.util.Collection getMoreRecipients();
    public abstract void setMoreRecipients(java.util.Collection moreRecipients);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.Boolean getAutomatic();
    public abstract void setAutomatic(java.lang.Boolean automatic);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SMSHistoryDetails getDetails() {
        SMSHistoryDetails details = new SMSHistoryDetails();
        details.setId(getId());
        details.setDate(getDate());
        details.setGroups(getGroups());
        details.setMoreRecipients(getMoreRecipients());
        details.setRecipients(getRecipients());
        details.setMessage(getMessage());
        details.setAutomatic(getAutomatic());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SMSHistoryDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(SMSHistoryUtil.generateGUID(this));
        }
        setId(details.getId());
        setDate(details.getDate());
        setGroups(details.getGroups());
        setMoreRecipients(details.getMoreRecipients());
        setRecipients(details.getRecipients());
        setMessage(details.getMessage());
        setAutomatic(details.getAutomatic());
        return getId();
    }
    
    public void ejbPostCreate(SMSHistoryDetails details) throws CreateException {
        
    }
}
