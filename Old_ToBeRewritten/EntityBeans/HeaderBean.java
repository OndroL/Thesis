/**
 * Clubspire, (c) Inspire CZ 2004-2012
 *
 * HeaderBean.java
 * Created on: 29.3.2012
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.common;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

/**
 * EJB 2.0 HeaderEnterprise Bean. Entita reprezentuje nastaveni hlavicky pri tisknuti poznamek.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Header"
 *      local-jndi-name="ejb/bs/LocalHeader"
 *      display-name="HeaderEJB"
 *      view-type="local"
 *      primkey-field="id" 
 * @ejb.value-object
 *      name="Header"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Header o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findValidAtributes()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findValidAtributes()"
 *          where="location >= 0"
 *          order="location"
 * @ejb.persistence
 *      table-name="noteheader"
 *      @jboss.persistence
 *          create-table="false"
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
public abstract class HeaderBean extends BaseEntityBean implements EntityBean {
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
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getField();
    public abstract void setField(int field);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getLocation();
    public abstract void setLocation(int location);
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(String id, int field, int location) throws CreateException {
        setId(id);
        setField(field);
        setLocation(location);
        return id;
    }
    
    public void ejbPostCreate(String id, int field, int location) throws CreateException {
    }
}
