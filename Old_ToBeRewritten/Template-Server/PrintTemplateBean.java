/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * PrintTemplateBean.java
 * Created on: 10.1.2013
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.template.ejb;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

/**
 * PrintTemplateBean
 * 
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="PrintTemplate"
 *      local-jndi-name="ejb/template/LocalPrintTemplate"
 *      display-name="PrintTemplateEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="PrintTemplate"
 *      match="*"
 * @ejb.persistence
 *      table-name="print_template"
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
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class PrintTemplateBean extends BaseEntityBean implements EntityBean {
    
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
    public abstract String getContent();
    public abstract void setContent(String content);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract int getType();
    public abstract void setType(int type);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getTemplateName();
    public abstract void setTemplateName(String templateName);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getFileName();
    public abstract void setFileName(String fileName);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PrintTemplateDetails getDetails() {
        PrintTemplateDetails details = new PrintTemplateDetails();
        details.setId(getId());
        details.setContent(getContent());
        details.setType(getType());
        details.setTemplateName(getTemplateName());
        details.setFileName(getFileName());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------    
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(PrintTemplateDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(PrintTemplateUtil.generateGUID(this));
        }
        setId(details.getId());
        setContent(details.getContent());
        setType(details.getType());
        setTemplateName(details.getTemplateName());
        setFileName(details.getFileName());
        return getId();
    }
    
    public void ejbPostCreate(PrintTemplateDetails details) throws CreateException {
        
    }
}
