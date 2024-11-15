/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* NastaveniJsonBean.java
* Created on: 9.9.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.common;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 NastaveniEnterprise Bean. Entita reprezentuje systemona nastaveni.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="NastaveniJson"
 *      local-jndi-name="ejb/bs/LocalNastaveniJson"
 *      display-name="NastaveniJsonEJB"
 *      view-type="local"
 *      primkey-field="key" 
 * @ejb.value-object
 *      name="NastaveniJson"
 *      match="ent"
 * @ejb.persistence
 *      table-name="nastaveni_json"
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
public abstract class NastaveniJsonBean extends BaseEntityBean implements EntityBean {

    private static org.apache.log4j.Logger logger = Logger.getLogger(NastaveniJsonBean.class);
    
    protected Logger getLogger() { 
        return logger; 
    }
    
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
    public abstract String getValue();        
    /**
     * @ejb.interface-method view-type="local"
     */
    public abstract void setValue(String value);
    
    // Entity relations ---------------------------------------------------------------------------
     
    // Business methods ----------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public cz.inspire.enterprise.module.common.NastaveniJsonDetails getDetails() {
        NastaveniJsonDetails details = new NastaveniJsonDetails();
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
    public String ejbCreate(String key, String value) throws CreateException {
        setKey(key);
        setValue(value);
        return null;
    }
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(NastaveniJsonDetails details) throws CreateException {
        setKey(details.getKey());
        setValue(details.getValue());
        return null;
    }
    
    public void ejbPostCreate(String key, String value) throws CreateException {
    }
    
    public void ejbPostCreate(NastaveniJsonDetails details) throws CreateException {
    }
}