/*
 * DepositBean.java
 *
 * Created on 20. leden 2004, 13:05
 */

package cz.inspire.enterprise.module.token;

import cz.inspire.enterprise.exception.ApplicationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.ejb.*;



/**
 * EJB 2.0 TypTokenu Enterprise Bean. Entita reprezentuje typ Tokenu.
 * 
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="TypTokenu"
 *      local-jndi-name="ejb/token/LocalTypTokenu"
 *      display-name="TypTokenuEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="TypTokenu"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM TypTokenu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM TypTokenu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(java.lang.String jazyk, int offset, int count)"
 *          from=",typ_tokenu_loc"
 *          where="(typ_tokenu.id = typ_tokenu_loc.typ_tokenu) AND (typ_tokenu_loc.jazyk = {0})"
 *          order="typ_tokenu_loc.nazev"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.persistence
 *      table-name="typ_tokenu"
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
 * @author <a href="http://www.inspire.cz">Inspire CZ, s.r.o.</a>
 */
public abstract class TypTokenuBean implements EntityBean {
    
    protected EntityContext entityContext = null;    
    
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
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getAutoPrirazovatZakaznikum();
    public abstract void setAutoPrirazovatZakaznikum(boolean flag);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getZobrazovatNazevTokenu();
    public abstract void setZobrazovatNazevTokenu(boolean flag);
    
    // Entity relations ---------------------------------------------------------------------------
        
      
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="token-typ_tokenu"
     *    role-name="typ_tokenu-je-typem-tokenu"
     */
    public abstract java.util.Collection getTokeny();
    public abstract void setTokeny(java.util.Collection tokeny);
     
   /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="typ_tokenu-typ_tokenu_loc"
     *    role-name="k-typu_tokenu-patri-locale-data"
     *    target-ejb="TypTokenuLoc"
     *    target-role-name="locale-data-patri-k-typu_tokenu"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="typ_tokenu"
     * @ejb.value-object
     *    aggregate="java.util.Map"
     *    aggregate-name="LocaleData"
     */
    public abstract java.util.Collection getLocaleData();
    public abstract void setLocaleData(java.util.Collection localeDate);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public TypTokenuDetails getDetails() {
        TypTokenuDetails details =  new TypTokenuDetails();
        details.setId(getId());
        details.setAutoPrirazovatZakaznikum(getAutoPrirazovatZakaznikum());
        details.setZobrazovatNazevTokenu(getZobrazovatNazevTokenu());
        
       // Locale data
        Iterator it = getLocaleData().iterator();
        Map locData = new HashMap();
        while (it.hasNext()) {
            LocalTypTokenuLoc typTokenuLoc = (LocalTypTokenuLoc) it.next();
            TypTokenuLocDetails typTokenuLocDetails = typTokenuLoc.getDetails();
            locData.put(typTokenuLocDetails.getJazyk(), typTokenuLocDetails);
        }
        details.setLocaleData(locData);
        
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(TypTokenuDetails details) throws ApplicationException {
        setAutoPrirazovatZakaznikum(details.getAutoPrirazovatZakaznikum());
        setZobrazovatNazevTokenu(details.getZobrazovatNazevTokenu());
        // Locale data
        try {
            // Clean up old locale entities
            Iterator oldIt = new Vector(getLocaleData()).iterator();
            while (oldIt.hasNext()) {
                LocalTypTokenuLoc typTokenuLoc = (LocalTypTokenuLoc) oldIt.next();
                typTokenuLoc.remove();
            }
            getLocaleData().clear();

            Map locData = details.getLocaleData();
            Iterator it = locData.values().iterator();

            // Insert new
            LocalTypTokenuLocHome locHome = TypTokenuLocUtil.getLocalHome();
	        while(it.hasNext()) {
	            TypTokenuLocDetails locDetails = (TypTokenuLocDetails) it.next();
	            LocalTypTokenuLoc locEntity = locHome.create(locDetails);
	            getLocaleData().add(locEntity);
	        }
        } catch (Exception e) {
            throw new ApplicationException("TypTokenu entity could not be updated.", e);
        }
            
    }        

    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(TypTokenuDetails details) throws CreateException {
        if (details.getId() == null) details.setId(TypTokenuUtil.generateGUID(this));
        setId(details.getId());
        setAutoPrirazovatZakaznikum(details.getAutoPrirazovatZakaznikum());
        setZobrazovatNazevTokenu(details.getZobrazovatNazevTokenu());

        return null;
    }
    public void ejbPostCreate(TypTokenuDetails details) throws CreateException {
        // Locale data
        try {
            Map locData = details.getLocaleData();
            Iterator it = locData.values().iterator();

            // Insert new
            LocalTypTokenuLocHome locHome = TypTokenuLocUtil.getLocalHome();
	        while(it.hasNext()) {
	            TypTokenuLocDetails locDetails = (TypTokenuLocDetails) it.next();
	            LocalTypTokenuLoc locEntity = locHome.create(locDetails);
	            getLocaleData().add(locEntity);
	        }
        } catch (Exception e) {
            throw new CreateException("TypTokenu could not be created: " + e.toString());
        }
    }
    
    /**
     * Gets the EntityContext. To be used by classes extending this.
     * @return the EntityContext of the EJB
     */
    protected EntityContext getEntityContext() {
        return entityContext;
    }

    /** Required to implement EntityBean. Sets the EntityContext. */
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }
    /** Required to implement EntityBean. Sets the EntityContext to null. */
    public void unsetEntityContext() throws EJBException {
        entityContext = null;
    }

    /** Required to implement EntityBean. Not implemented. */
    public void ejbActivate() throws EJBException {
    }

    /** Required to implement EntityBean. Not implemented. */
    public void ejbPassivate() throws EJBException {
    }

    /** Required to implement EntityBean. Not implemented. */
    public void ejbLoad() throws EJBException {
    }

    /** Required to implement EntityBean. Not implemented. */
    public void ejbStore() throws EJBException {
    }

    /** Required to implement EntityBean. Not implemented. */
    public void ejbRemove() throws RemoveException, EJBException {
    }    
}
