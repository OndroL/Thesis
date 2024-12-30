/**
 * SkupinaBean.java
 *
 * Created on 20. leden 2004, 13:05
 */
package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.exception.ApplicationException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Skupina Enterprise Bean. Entita reprezentuje skupinu uzivatelu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Skupina"
 *      local-jndi-name="ejb/user/LocalSkupina"
 *      display-name="SkupinaEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Skupina"
 *      match="ent"
 * @ejb.value-object
 *      name="SkupinaFull"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Skupina o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Skupina o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          order="nazev"
 * 			other="LIMIT {1} OFFSET {0}"
 * @ejb.persistence
 *      table-name="skupina"
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
public abstract class SkupinaBean extends BaseEntityBean implements EntityBean {
    
    private final Logger logger = Logger.getLogger(SkupinaBean.class);
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
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
    public abstract String getNazev();
    public abstract void setNazev(String nazev);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="full"
     */
    public abstract java.util.Map getNastaveni();
    public abstract void setNastaveni(java.util.Map nastaveni);    
    
     // Entity relations ---------------------------------------------------------------------------

     /**
      * @ejb.interface-method
      *    view-type="local"
      * @ejb.relation
      *    name="skupina-role"
      *    role-name="skupina-ma-prirazeny-role"
      *    target-ejb="Role"
      *    target-role-name="role-je-prirazena-uskupinam"
      *    target-multiple="true"
      * @jboss.relation
      *    related-pk-field="id"
      *    fk-column="role"
      * @jboss.target-relation
      *	   related-pk-field="id"
      *	   fk-column="skupina"    
      * @jboss.relation-table
      *    table-name="skupina_role"
      *    create-table="true"
      * @ejb.value-object
      *    match="ent"
      *    aggregate="java.util.Collection"
      *    aggregate-name="Role"
      */
    public abstract Collection getRole();
    public abstract void setRole(Collection role);
     
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="skupina-uzivatel"
     *    role-name="skupina-obsahuje-uzivatele"
     */
     public abstract java.util.Collection getUzivatele();
     public abstract void setUzivatele(java.util.Collection uzivatele);
        
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SkupinaDetails getDetails() {
        SkupinaDetails details = new SkupinaDetails(getId(), getNazev());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SkupinaFullDetails getFullDetails() {
        SkupinaFullDetails details = new SkupinaFullDetails(getId(), getNazev(), getNastaveni());
        List<String> roles = new ArrayList<String>();
        
        Iterator it = getRole().iterator();
        while(it.hasNext()) {
            LocalRole role = (LocalRole) it.next();
            roles.add(role.getId());
        }
        details.setRole(roles);
        return details;        
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setFullDetails(SkupinaFullDetails details) throws ApplicationException {
        setNazev(details.getNazev());
        setNastaveni(details.getNastaveni());
        getRole().clear();
        if (details.getRole() != null) {
            try {
                LocalRoleHome roleHome = RoleUtil.getLocalHome();
                Iterator it = details.getRole().iterator();
                while(it.hasNext()) {
                    String roleId = (String) it.next();
                    LocalRole role = roleHome.findByPrimaryKey(roleId);
                    getRole().add(role);
                }
            } catch (Exception e) {
                throw new ApplicationException("Could not set roles.", e);
            }
        }
    } 
        
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SkupinaFullDetails skupina) throws CreateException {
        if (skupina.getId() == null) {
            skupina.setId(SkupinaUtil.generateGUID(this));
        }
        
        setId(skupina.getId());
        setNazev(skupina.getNazev());
        setNastaveni(skupina.getNastaveni());
        return skupina.getId();
    }
    
    public void ejbPostCreate(SkupinaFullDetails skupina) throws CreateException {
        if (skupina.getRole() != null) {
            try {
                Iterator it = skupina.getRole().iterator();
                LocalRoleHome roleHome = RoleUtil.getLocalHome();
                while (it.hasNext()) {
                    String roleId = (String) it.next();
                    LocalRole role = roleHome.findByPrimaryKey(roleId);
                    getRole().add(role);
                }
            } catch (Exception e) {
                throw new CreateException("Could not set roles.");
            }
        }
    }
    
}