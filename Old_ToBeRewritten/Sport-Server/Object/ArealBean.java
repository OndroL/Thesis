package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.exception.ApplicationException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Areal Enterprise Bean. Entita reprezentuje sportovni areal.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Areal"
 *      local-jndi-name="ejb/sport/LocalAreal"
 *      display-name="ArealEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Areal"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Areal o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="id"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Areal o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk)"
 *          from=",areal_loc"
 *          where="(nadrazeny_areal = {0}) AND (areal.id = areal_loc.areal) AND (areal_loc.jazyk = {1})"
 *          order="areal_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Areal o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findRoot(java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findRoot(java.lang.String jazyk)"
 *          from=",areal_loc"
 *          where="(nadrazeny_areal is NULL) AND (areal.id = areal_loc.areal) AND (areal_loc.jazyk = {0})"
 *          order="areal_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Areal o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk, int offset, int count)"
 *          from=",areal_loc"
 *          where="(nadrazeny_areal = {0}) AND (areal.id = areal_loc.areal) AND (areal_loc.jazyk = {1})"
 *          order="areal_loc.nazev"
 * 			other="LIMIT {3} OFFSET {2}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Areal o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findRoot(java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findRoot(java.lang.String jazyk, int offset, int count)"
 *          from=",areal_loc"
 *          where="(nadrazeny_areal is NULL) AND (areal.id = areal_loc.areal) AND (areal_loc.jazyk = {0})"
 *          order="areal_loc.nazev"
 * 			other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="LocalAreal findIfChild(java.lang.String childId, java.lang.String parentId)"
 *      @jboss.declared-sql
 *          signature="LocalAreal findIfChild(java.lang.String id, java.lang.String parentId)"
 * 			where="(id = {0}) AND areal_isChild({1}, {0})"
 *          eager-load-group="base"
 * @ejb.persistence
 *      table-name="areal"
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
public abstract class ArealBean extends BaseEntityBean implements EntityBean {

    protected Logger logger = Logger.getLogger(ArealBean.class);
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract String getId();
    public abstract void setId(String id);


     // Entity relations ---------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="areal-areal_loc"
     *    role-name="k-arealu-patri-locale-data"
     *    target-ejb="ArealLoc"
     *    target-role-name="locale-data-patri-k-arealu"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="areal"
     * @ejb.value-object
     *    aggregate="java.util.Map"
     *    aggregate-name="LocaleData"
     * @jboss.relation-read-ahead
     *  strategy="on-find"
     *  eager-load-group="*"
     */     
    public abstract java.util.Collection getLocaleData();
    public abstract void setLocaleData(java.util.Map localeDate);
  
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="areal-areal"
     *    role-name="podareal-patri-k-arealu"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="nadrazeny_areal"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="NadrazenyArealId"
     */
    public abstract LocalAreal getNadrazenyAreal();
    public abstract void setNadrazenyAreal(LocalAreal areal);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="areal-areal"
     *    role-name="areal-obsahuje-podarealy"
     */
    public abstract java.util.Collection getPodrazeneArealy();
    public abstract void setPodrazeneArealy(java.util.Collection podrazeneArealy);

    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="areal-objekt"
     *    role-name="areal-obsahuje-objekty"
     */
     public abstract java.util.Collection getObjekty();
     public abstract void setObjekty(java.util.Collection objekty);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getPocetNavazujucichRez();
    public abstract void setPocetNavazujucichRez(Integer pocetNavazujucichRez);

    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ArealDetails getDetails() {
        ArealDetails details = new ArealDetails(getId(),getPocetNavazujucichRez());
        Iterator it = getLocaleData().iterator();
        Map locData = new HashMap();
        while (it.hasNext()) {
            LocalArealLoc arealLoc = (LocalArealLoc) it.next();
            ArealLocDetails arealLocDetails = arealLoc.getDetails();
            locData.put(arealLocDetails.getJazyk(), arealLocDetails);
        }
        details.setLocaleData(locData);
        
        LocalAreal nadrazenyAreal = getNadrazenyAreal();
        if (nadrazenyAreal != null) {
            details.setNadrazenyArealId(nadrazenyAreal.getId());
        }
        
        return details;
        
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(ArealDetails areal) throws ApplicationException {

        // Locale data
        try {
            // Locale data
            // Clean up old locale entities
            Iterator oldIt = new Vector(getLocaleData()).iterator();
            while (oldIt.hasNext()) {
                LocalArealLoc arealLoc = (LocalArealLoc) oldIt.next();
                arealLoc.remove();
            }
            getLocaleData().clear();

            Map locData = areal.getLocaleData();
            Iterator it = locData.values().iterator();

            // Insert new
            LocalArealLocHome locHome = ArealLocUtil.getLocalHome();
	        while(it.hasNext()) {
	            ArealLocDetails locDetails = (ArealLocDetails) it.next();
	            LocalArealLoc locEntity = locHome.create(locDetails);
	            getLocaleData().add(locEntity);
	        }
            
            // parent
            
            if (areal.getNadrazenyArealId() == null) {
                setNadrazenyAreal(null);
            } else {
                LocalAreal nadrazenyAreal = ArealUtil.getLocalHome().findByPrimaryKey(
                    areal.getNadrazenyArealId());
                setNadrazenyAreal(nadrazenyAreal);
            }
            setPocetNavazujucichRez(areal.getPocetNavazujucichRez());
        } catch (Exception e) {
            throw new ApplicationException("Areal entity could not be updated.", e);
        }
        
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void addObjekt(LocalObjekt objekt) {
        getObjekty().add(objekt);
    }    

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void addAreal(LocalAreal areal) {
        getPodrazeneArealy().add(areal);
    }    
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(ArealDetails areal) throws CreateException {
        String id = areal.getId() == null ? ArealUtil.generateGUID(this) : areal.getId();        
        setId(id);
        setPocetNavazujucichRez(areal.getPocetNavazujucichRez());
        return null;
    }
    
    public void ejbPostCreate(ArealDetails areal) throws CreateException {
        Map locData = areal.getLocaleData();
        Iterator it = locData.values().iterator();
        try {
	        LocalArealLocHome locHome = ArealLocUtil.getLocalHome();
	        while(it.hasNext()) {
	            ArealLocDetails locDetails = (ArealLocDetails) it.next();
	            LocalArealLoc locEntity = locHome.create(locDetails);
	            getLocaleData().add(locEntity);
	        }
        } catch (NamingException ne) {
            throw new CreateException("Sport entity could not be created.");
        }
    }
    
    //    ====================finders===============================    
    
    /**
     * @ejb.select
     * query="SELECT o.id FROM Areal o WHERE o.nadrazenyAreal.id = ?1"
     */
    public abstract Collection<String> ejbSelectArealIdsByParent(String arealId) throws FinderException;
                
    /**
     * @ejb.home-method
     * view-type="local"
     */
    public Collection<String> ejbHomeGetArealIdsByParent(String arealId) throws FinderException {
        return ejbSelectArealIdsByParent(arealId);
    }
//    ==========================================================
}
