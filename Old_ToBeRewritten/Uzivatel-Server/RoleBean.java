/**
 * RoleBean.java
 *
 * Created on 20. leden 2004, 13:05
 */
package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Role Enterprise Bean. Entita reprezentuje roli ve ktere vystupuje uzivatel. Role je
 * logicke seskupeni uzivatelu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Role"
 *      local-jndi-name="ejb/user/LocalRole"
 *      display-name="RoleEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Role"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Role o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="nazev"
 * @ejb.persistence
 *      table-name="role"
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
public abstract class RoleBean extends BaseEntityBean implements EntityBean {   
    
    private final Logger logger = Logger.getLogger(RoleBean.class);
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getId();
    public abstract void setId(String id);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method view-type="local"
     */
    public abstract String getNazev();
    
    /**
     * @ejb.persistent-field
     * @ejb.interface-method view-type="local"
     */
    public abstract void setNazev(String nazev);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method view-type="local"
     */
    public abstract String getPopis();
    
    /**
     * @ejb.persistent-field
     * @ejb.interface-method view-type="local"
     */
    public abstract void setPopis(String popis);    

    // Businessethods ------------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public cz.inspire.enterprise.module.user.RoleDetails getDetails() {
        return new RoleDetails(getId(), getNazev(), getPopis());
    }
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(RoleDetails role) throws CreateException {
        setId(role.getId());
        setNazev(role.getNazev());
        setPopis(role.getPopis());        
        return role.getId();
    }
    
    public void ejbPostCreate(RoleDetails role) throws CreateException {
    }
      
}