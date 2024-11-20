/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * GeneratedAttachmentBean.java
 * Created on: 10.1.2013
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.email;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import cz.inspire.enterprise.module.template.ejb.LocalPrintTemplate;
import cz.inspire.enterprise.module.template.ejb.PrintTemplateUtil;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * GeneratedAttachmentBean
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="GeneratedAttachment"
 *      local-jndi-name="ejb/email/LocalGeneratedAttachment"
 *      display-name="GeneratedAttachmentEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="GeneratedAttachment"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM GeneratedAttachment o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByEmailAndHistory(java.lang.String historyId, java.lang.String email)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByEmailAndHistory(java.lang.String historyId, java.lang.String email)"
 *          where="email_history = {0} AND email = {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM GeneratedAttachment o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByHistory(java.lang.String historyId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByHistory(java.lang.String historyId)"
 *          where="email_history = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM GeneratedAttachment o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByEmailAndHistoryAndTemplate(java.lang.String historyId, java.lang.String email, java.lang.String templateId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByEmailAndHistoryAndTemplate(java.lang.String historyId, java.lang.String email, java.lang.String templateId)"
 *          where="email_history = {0} AND email = {1} AND print_template = {2}"
 * @ejb.persistence
 *      table-name="generated_attachment"
 *      @jboss.persistence
 *          create-table="true"
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
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class GeneratedAttachmentBean extends BaseEntityBean implements EntityBean {
    private static Logger logger = Logger.getLogger(GeneratedAttachmentBean.class);
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
    public abstract String getEmail();
    public abstract void setEmail(String email);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.util.Map getAttributes();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setAttributes(java.util.Map attributes);
    
    // Entity relations ----------------------------------------------------------------------------    
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="generated_attachment-print_template"
     *    role-name="priloha-byla-vygenerovana-z-sablony"
     *    target-ejb="PrintTemplate"
     *    target-role-name="tiskova-sablona-je-urceno-pro-generovani-prilohy"
     *    cascade-delete="false"
     *    target-multiple="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="print_template"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="PrintTemplateId"
     */
     public abstract cz.inspire.enterprise.module.template.ejb.LocalPrintTemplate getPrintTemplate();
     public abstract void setPrintTemplate(cz.inspire.enterprise.module.template.ejb.LocalPrintTemplate printTemplate);
     
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="generated_attachment-email_history"
     *    role-name="priloha-patri-k-emailu"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="email_history"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="EmailHistoryId"
     */
     public abstract cz.inspire.enterprise.module.email.LocalEmailHistory getEmailHistory();
     public abstract void setEmailHistory(cz.inspire.enterprise.module.email.LocalEmailHistory emailHistory);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public GeneratedAttachmentDetails getDetails() {
        GeneratedAttachmentDetails details = new GeneratedAttachmentDetails();
        details.setId(getId());
        details.setAttributes(getAttributes());
        details.setEmail(getEmail());
        
        LocalPrintTemplate localPrintTemplate = getPrintTemplate();
        details.setPrintTemplateId(localPrintTemplate == null ? null : localPrintTemplate.getId());
        
        LocalEmailHistory localEmailHistory = getEmailHistory();
        details.setEmailHistoryId(localEmailHistory == null ? null : localEmailHistory.getId());
        
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------    
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(GeneratedAttachmentDetails details, LocalEmailHistory localEmailHistory) throws CreateException {
        if (details.getId() == null) {
            details.setId(GeneratedAttachmentUtil.generateGUID(this));
        }
        setId(details.getId());
        setAttributes(details.getAttributes());
        setEmail(details.getEmail());
        return getId();
    }
    
    public void ejbPostCreate(GeneratedAttachmentDetails details, LocalEmailHistory localEmailHistory) throws CreateException {
        try {
            setEmailHistory(localEmailHistory);

            String templateId = details.getPrintTemplateId();
            if (templateId != null) {
                LocalPrintTemplate localTemplate = PrintTemplateUtil.getLocalHome().findByPrimaryKey(templateId);
                setPrintTemplate(localTemplate);
            }
        } catch (Exception e) {
            entityContext.setRollbackOnly();
            logger.error("GeneratedAttachment couldn't be created", e);
            throw new CreateException("GeneratedAttachment couldn't be created: " + e.toString());
        }
    }
}
