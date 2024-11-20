/**
 * Clubspire, (c) Inspire CZ 2004-2011
 *
 * EmailQueueBean.java
 * Created on: 6.9.2011
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.email;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 EmailQueue Bean.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="EmailQueue"
 *      local-jndi-name="ejb/email/LocalEmailQueue"
 *      display-name="EmailQueueEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="EmailQueue"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailQueue o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="date DESC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailQueue o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          order="created ASC"
 *          other="LIMIT {1} OFFSET {0}"
 *          strategy="on-find"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailQueue o"
 *      result-type-mapping="Local"
 *      signature="LocalEmailQueue findFirstMail()"
 *      @jboss.declared-sql
 *          signature="LocalEmailQueue findFirstMail()"
 *          order="priority DESC, created ASC"
 *          other="LIMIT 1"
 *          strategy="on-find"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailQueue o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByHistory(java.lang.String historyId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByHistory(java.lang.String historyId)"
 *          where="emailhistory = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailQueue o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDependentHistory(java.lang.String historyId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDependentHistory(java.lang.String historyId)"
 *          where="dependentemailhistory = {0}"
 * @ejb.persistence
 *      table-name="email_queue"
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
public abstract class EmailQueueBean extends BaseEntityBean
        implements EntityBean  {

    private static org.apache.log4j.Logger logger = Logger.getLogger(EmailQueueBean.class);
    protected Logger getLogger() { return logger; }

    // Entity fields -------------------------------------------------------------------------------

    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     * @jboss.persistence dbindex="true"
     */
    public abstract String getId();
    public abstract void setId(String id);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.persistence dbindex="true"
     */
    public abstract java.util.Date getCreated();
    public abstract void setCreated(java.util.Date date);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     * @jboss.persistence dbindex="true"
     */
    public abstract java.lang.String getEmailHistory();
    public abstract void setEmailHistory(java.lang.String emailHistoryId);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.String getRecipient();
    public abstract void setRecipient(java.lang.String recipient);
    
    /**
     * Priority of the email. Greater number means higher priority.
     * 
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getPriority();
    public abstract void setPriority(int priority);
    
    /**
     * Priority of the email. Greater number means higher priority.
     * 
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getRemoveEmailHistory();
    public abstract void setRemoveEmailHistory(boolean emailHistory);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     * @jboss.persistence dbindex="true"
     */
    public abstract String getDependentEmailHistory();
    public abstract void setDependentEmailHistory(String emailHistoryId);

    // Entity relations ---------------------------------------------------------------------------

    // Business methods ----------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public EmailQueueDetails getDetails() {
        EmailQueueDetails details = new EmailQueueDetails();
        details.setId(getId());
        details.setCreated(getCreated());
        details.setEmailHistory(getEmailHistory());
        details.setRecipient(getRecipient());
        details.setPriority(getPriority());
        details.setRemoveEmailHistory(getRemoveEmailHistory());
        details.setDependentEmailHistory(getDependentEmailHistory());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(EmailQueueDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(EmailQueueUtil.generateGUID(this));
        }
        setId(details.getId());
        setCreated(details.getCreated());
        setRecipient(details.getRecipient());
        setEmailHistory(details.getEmailHistory());
        setPriority(details.getPriority());
        setRemoveEmailHistory(details.getRemoveEmailHistory());
        setDependentEmailHistory(details.getDependentEmailHistory());
        return getId();
    }
    
    public void ejbPostCreate(EmailQueueDetails details) throws CreateException {
        
    }
}

