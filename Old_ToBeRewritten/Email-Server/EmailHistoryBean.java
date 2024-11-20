/*
 * Clubspire, (c) Inspire CZ 2004-2006
 *
 * EmailHistoryBean.java
 * Created: 22. srpen 2007, 9:31
 * Author: Kamil
 *
 */

package cz.inspire.enterprise.module.email;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;


/**
 * EJB 2.0 EmailHistory Bean. Entita reprezentuje jeden odeslany mail.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="EmailHistory"
 *      local-jndi-name="ejb/email/LocalEmailHistory"
 *      display-name="EmailHistoryEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="EmailHistory"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailHistory o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="date DESC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailHistory o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          order="date DESC"
 *          other="LIMIT {1} OFFSET {0}"
 *          strategy="on-find"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM EmailHistory o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDate(java.util.Date dateFrom, java.util.Date dateTo, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDate(java.util.Date dateFrom, java.util.Date dateTo, int offset, int count)"
 *          order="date DESC"
 *          where="(date >= {0}) AND (date <= {1}) AND sent=true"
 *          other="LIMIT {3} OFFSET {2}"
 *          strategy="on-find"
 * @ejb.persistence
 *      table-name="email_history"
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
public abstract class EmailHistoryBean extends BaseEntityBean
        implements EntityBean  {
    
    private static org.apache.log4j.Logger logger = Logger.getLogger(EmailHistoryBean.class);
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
    public abstract java.util.Date getDate();
    public abstract void setDate(java.util.Date date);
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getText();
    public abstract void setText(java.lang.String text);
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getSubject();
    public abstract void setSubject(java.lang.String subject);
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
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.Boolean getHtml();
    public abstract void setHtml(java.lang.Boolean isHtml);
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Map getAttachments();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setAttachments(java.util.Map attachments);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Boolean getSent();

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setSent(java.lang.Boolean isSent);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="generated_attachment-email_history"
     *    role-name="email-ma-generovane-prilohy"
     * @ejb.value-object match="*"
     *    aggregate="java.util.List<GeneratedAttachmentDetails>"
     *    aggregate-name="GeneratedAttachments"
     */
    public abstract java.util.Collection getGeneratedAttachments();
    public abstract void setGeneratedAttachments(java.util.Collection generatedAttachments);

    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public EmailHistoryDetails getDetails() {
        EmailHistoryDetails details = new EmailHistoryDetails();
        details.setId(getId());
        details.setDate(getDate());
        details.setGroups(getGroups());
        details.setMoreRecipients(getMoreRecipients());
        details.setRecipients(getRecipients());
        details.setText(getText());
        details.setSubject(getSubject());
        details.setAutomatic(getAutomatic());
        details.setHtml(getHtml());
        details.setAttachments(null);
        details.setSent(getSent());
        
        List<GeneratedAttachmentDetails> generatedAttachments = new ArrayList<GeneratedAttachmentDetails>();
        Iterator<LocalGeneratedAttachment> it = getGeneratedAttachments().iterator();
        while(it.hasNext()) {
            LocalGeneratedAttachment polozka = it.next();
            generatedAttachments.add(polozka.getDetails());
        }
        details.setGeneratedAttachments(generatedAttachments);
        return details;
    }
    // Entity methods ------------------------------------------------------------------------------
    
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(EmailHistoryDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(EmailHistoryUtil.generateGUID(this));
        }
        setId(details.getId());
        setDate(details.getDate());
        setGroups(details.getGroups());
        setMoreRecipients(details.getMoreRecipients());
        setRecipients(details.getRecipients());
        setText(details.getText());
        setSubject(details.getSubject());
        setAutomatic(details.getAutomatic());
        setHtml(details.getHtml());
        setAttachments(details.getAttachments());
        setSent(details.getSent());
        return getId();
    }
    
    public void ejbPostCreate(EmailHistoryDetails details) throws CreateException {
        if (details.getGeneratedAttachments() != null) {
            try {
                Iterator<GeneratedAttachmentDetails> it = details.getGeneratedAttachments().iterator();
                LocalEmailHistory instance = (LocalEmailHistory) getEntityContext().getEJBLocalObject();
                while (it.hasNext()) {
                    GeneratedAttachmentDetails gad = it.next();
                    GeneratedAttachmentUtil.getLocalHome().create(gad, instance);
                }
            } catch (Exception e) {
                entityContext.setRollbackOnly();
                logger.error("EmailHistory couldn't be created", e);
                throw new CreateException("EmailHistory couldn't be created: " + e.toString());
            }
        }
    }
}
