/*
 * ObjednavkaObjektu.java
 *
 * Created on 20. leden 2004, 15:54
 */

package cz.inspire.enterprise.module.common;

import javax.ejb.*;

import org.apache.log4j.Logger;


/**
 * EJB 2.0 NastaveniEnterprise Bean. Entita reprezentuje systemona nastaveni.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Nastaveni"
 *      local-jndi-name="ejb/bs/LocalNastaveni"
 *      display-name="NastaveniEJB"
 *      view-type="local"
 *      primkey-field="key" 
 * @ejb.value-object
 *      name="Nastaveni"
 *      match="ent"
 * @ejb.persistence
 *      table-name="nastaveni"
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
public abstract class NastaveniBean extends BaseEntityBean
implements EntityBean  {
    
    private static org.apache.log4j.Logger logger = Logger.getLogger(NastaveniBean.class);
    protected Logger getLogger() { return logger; }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     */
    public abstract String getKey();    
    public abstract void setKey(String key);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method view-type="local"
     *
     */
    public abstract java.io.Serializable getValue();        
    /**
     * @ejb.interface-method view-type="local"
     */
    public abstract void setValue(java.io.Serializable value);
    
    
    // Entity relations ---------------------------------------------------------------------------
     
    // Business methods ----------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public cz.inspire.enterprise.module.common.NastaveniDetails getDetails() {
        NastaveniDetails details = new NastaveniDetails();
        details.setKey(getKey());
        details.setValue(getValue());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(String key, java.io.Serializable value) throws CreateException {
        setKey(key);
        setValue(value);
        return null;
    }
    public void ejbPostCreate(String key, java.io.Serializable value) throws CreateException {
    }
}
