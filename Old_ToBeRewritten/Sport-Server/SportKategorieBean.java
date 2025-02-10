package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.exception.ApplicationException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
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
 * Entita reprezentuje kategorii cinnosti.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="SportKategorie"
 *      local-jndi-name="ejb/sport/LocalSportKategorie"
 *      display-name="SportKategorieEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="SportKategorie"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportKategorie o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          from=",sport_kategorie_loc"
 *          where="(sport_kategorie.id = sport_kategorie_loc.sportkategorie)"
 *          order="sport_kategorie_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportKategorie o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findRoot()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findRoot()"
 *          from=",sport_kategorie_loc "
 *          where="nadrazena_kategorie is null AND (sport_kategorie.id = sport_kategorie_loc.sportkategorie)"
 *          order="sport_kategorie_loc.nazev" 
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportKategorie o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllByNadrazenaKategorie(java.lang.String nadrazenaKategorieId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllByNadrazenaKategorie(java.lang.String nadrazenaKategorieId)"
 *          from=",sport_kategorie_loc "
 *          where="nadrazena_kategorie = {0} AND (sport_kategorie.id = sport_kategorie_loc.sportkategorie)"
 *          order="sport_kategorie_loc.nazev" 
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportKategorie o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          other="LIMIT {1} OFFSET {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportKategorie o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllByMultisportFacilityId(java.lang.String multisportFacilityId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllByMultisportFacilityId(java.lang.String multisportFacilityId)"
 *          from=",sport_kategorie_loc "
 *          where="multisportfacilityid = {0} AND (sport_kategorie.id = sport_kategorie_loc.sportkategorie)"
 *          order="sport_kategorie_loc.nazev"
 * @ejb.persistence
 *      table-name="sport_kategorie"
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
public abstract class SportKategorieBean extends BaseEntityBean implements EntityBean {

    private final Logger logger = Logger.getLogger(SportKategorieBean.class);
    
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
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract String getMultisportFacilityId();
    public abstract void setMultisportFacilityId(String facilityId);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract String getMultisportServiceUUID();
    public abstract void setMultisportServiceUUID(String serviceUUID);
    
     // Entity relations ---------------------------------------------------------------------------
       
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport_kategorie-sport_kategorie"
     *    role-name="podkategorie-sportu-patri-ke-kategorie-sportu"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="nadrazena_kategorie"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="NadrazenaKategorieId"
     */
    public abstract LocalSportKategorie getNadrazenaKategorie();
    public abstract void setNadrazenaKategorie(LocalSportKategorie kategorie);    
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport_kategorie-sport_kategorie"
     *    role-name="kategorie-sportu-obsahuje-podkategorie-sportu"
     */
    public abstract java.util.Collection getPodrazeneKategorie();
    public abstract void setPodrazeneKategorie(java.util.Collection podrazeneKategorie);    
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport_kategorie-sport_kategorie_loc"
     *    role-name="ke-kategorii-cinnosti-patri-locale-data"
     *    target-ejb="SportKategorieLoc"
     *    target-role-name="locale-data-patri-ke-kategorii-cinnosti"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="sportKategorie"
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
     *    name="kategorie_sportu-sport"
     *    role-name="kategorie_sportu-obsahuje-sport"
     */
     public abstract java.util.Collection getCinnosti();
     public abstract void setCinnosti(java.util.Collection cinnosti);

    // Business methods ----------------------------------------------------------------------------
    
    private void setAttributes(SportKategorieDetails kategorie) {
        setMultisportFacilityId(kategorie.getMultisportFacilityId());
        setMultisportServiceUUID(kategorie.getMultisportServiceUUID());
    }
     
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SportKategorieDetails getDetails() {
        SportKategorieDetails details = new SportKategorieDetails(getId(), getMultisportFacilityId(),
                getMultisportServiceUUID());
        Iterator it = getLocaleData().iterator();
        Map locData = new HashMap();
        while (it.hasNext()) {
            LocalSportKategorieLoc arealLoc = (LocalSportKategorieLoc) it.next();
            SportKategorieLocDetails arealLocDetails = arealLoc.getDetails();
            locData.put(arealLocDetails.getJazyk(), arealLocDetails);
        }
        details.setLocaleData(locData);
        
        LocalSportKategorie nadrazenaKategorie = getNadrazenaKategorie();
        if (nadrazenaKategorie != null) {
            details.setNadrazenaKategorieId(nadrazenaKategorie.getId());
        }
        
        return details;
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(SportKategorieDetails kategorie) throws ApplicationException {
        setAttributes(kategorie);
        
        try {
            Iterator oldIt = new Vector(getLocaleData()).iterator();
            while (oldIt.hasNext()) {
                LocalSportKategorieLoc kategorieLoc = (LocalSportKategorieLoc) oldIt.next();
                kategorieLoc.remove();
            }
            getLocaleData().clear();

            Map locData = kategorie.getLocaleData();
            Iterator it = locData.values().iterator();

            // Insert new
            LocalSportKategorieLocHome locHome = SportKategorieLocUtil.getLocalHome();
	        while(it.hasNext()) {
	            SportKategorieLocDetails locDetails = (SportKategorieLocDetails) it.next();
	            LocalSportKategorieLoc locEntity = locHome.create(locDetails);
	            getLocaleData().add(locEntity);
	        }
                
            setNadrazenaKategorie(findKategorie(kategorie.getNadrazenaKategorieId()));            
        } catch (Exception e) {
            throw new ApplicationException("SportKategorie entity could not be updated.", e);
        }
        
    }
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SportKategorieDetails sportKategorie) throws CreateException {
        String id = sportKategorie.getId();
        if (id == null) {
            id = SportKategorieUtil.generateGUID(this);
        }
        
        setId(id);
        setAttributes(sportKategorie);
        return id;
    }
    
    public void ejbPostCreate(SportKategorieDetails sportKategorie) throws CreateException {
        Map locData = sportKategorie.getLocaleData();
        Iterator it = locData.values().iterator();
        setNadrazenaKategorie(findKategorie(sportKategorie.getNadrazenaKategorieId()));
        try {
            LocalSportKategorieLocHome locHome = SportKategorieLocUtil.getLocalHome();
            while (it.hasNext()) {
                SportKategorieLocDetails locDetails = (SportKategorieLocDetails) it.next();
                LocalSportKategorieLoc locEntity = locHome.create(locDetails);
                getLocaleData().add(locEntity);
            }
        } catch (NamingException ne) {
            throw new CreateException("Sport entity could not be created.");
        }
    }
    
    private LocalSportKategorie findKategorie(String kategorieId) {
        if (kategorieId == null) {
            return null;
        }

        LocalSportKategorie nadrazenaKategorie = null;
        try {
            nadrazenaKategorie = SportKategorieUtil.getLocalHome()
                    .findByPrimaryKey(kategorieId);
        } catch (Exception ex) {
            logger.error("Cannot find nadrazena kategorie.", ex);
        }
        
        return nadrazenaKategorie;
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM SportKategorie sport"
     */
    public abstract Long ejbSelectCount() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCount() throws FinderException {
        return ejbSelectCount();
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM SportKategorie sport WHERE sport.nadrazenaKategorie is null "
     */
    public abstract Long ejbSelectCountRoot() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountRoot() throws FinderException {
        return ejbSelectCountRoot();
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM SportKategorie sport WHERE sport.nadrazenaKategorie.id = ?1"
     */
    public abstract Long ejbSelectCountByNadrazenaKategorie(String kategorieId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountByNadrazenaKategorie(String kategorieId) throws FinderException {
        return ejbSelectCountByNadrazenaKategorie(kategorieId);
    }
    
}
