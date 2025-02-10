/**
 * Sport-Server, (c) Inspire CZ 2004-2006
 *
 * SportBean.java
 * Vytvoreno: 20.1.2004
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospisil</a>
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.exception.ApplicationException;
import cz.inspire.enterprise.exception.InvalidParameterException;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import cz.inspire.enterprise.module.sport.InstructorConstants;
import cz.inspire.enterprise.module.sport.SazbaStorna;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Sport Enterprise Bean. Entita reprezentuje libovolny sport.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Sport"
 *      local-jndi-name="ejb/user/LocalSport"
 *      display-name="SportEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="Sport"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="id"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk)"
 *          from=",sport_loc"
 *          where="(nadrazeny_sport = {0}) AND (sport.id = sport_loc.sport) AND (sport_loc.jazyk = {1})"
 *          order="sport_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByParent(java.lang.String parentId, java.lang.String jazyk, int offset, int count)"
 *          from=",sport_loc"
 *          where="(nadrazeny_sport = {0}) AND (sport.id = sport_loc.sport) AND (sport_loc.jazyk = {1})"
 *          order="sport_loc.nazev"
 *          other="LIMIT {3} OFFSET {2}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByCategory(java.lang.String kategorieId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByCategory(java.lang.String kategorieId, int offset, int count)"
 *          from=",sport_loc"
 *          where="(sport_kategorie = {0}) AND (sport.id = sport_loc.sport)"
 *          order="sport_loc.nazev"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByZbozi(java.lang.String zboziId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByZbozi(java.lang.String zboziId, int offset, int count)"
 * 			where="(zboziid = {0})"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findRoot(java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findRoot(java.lang.String jazyk)"
 *          from=",sport_loc"
 *          where="(nadrazeny_sport is NULL) AND (sport.id = sport_loc.sport) AND (sport_loc.jazyk = {0})"
 *          order="sport_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findRoot(java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findRoot(java.lang.String jazyk, int offset, int count)"
 *          from=",sport_loc"
 *          where="(nadrazeny_sport is NULL) AND (sport.id = sport_loc.sport) AND (sport_loc.jazyk = {0})"
 *          order="sport_loc.nazev"
 * 	    other="LIMIT {2} OFFSET {1}"
* @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findCategoryRoot(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findCategoryRoot(int offset, int count)"
 *          from=",sport_loc"
 *          where="(sport_kategorie is NULL) AND (sport.id = sport_loc.sport)"
 *          order="sport_loc.nazev"
 * 	    other="LIMIT {1} OFFSET {0}"
 * @ejb.persistence
 *      table-name="sport"
 *      @jboss.persistence
 *          create-table="true"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.container-configuration
 *      name="Optimistic CMP EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @author <a href="http://www.inspire.cz">Inspire CZ, s.r.o.</a>
 */
public abstract class SportBean extends BaseEntityBean implements EntityBean {
    
    private static final Logger logger = Logger.getLogger(SportBean.class);
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    private Set<InstructorDetails> instructorSet;
    
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
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getTyp();
    public abstract void setTyp(int typ);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getZboziId();
    public abstract void setZboziId(String zboziId);

    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getSkladId();
    public abstract void setSkladId(String zboziId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Integer getSazbaJednotek();
    public abstract void setSazbaJednotek(java.lang.Integer sazbaJednotek);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getSazbaNaOsobu();
    public abstract void setSazbaNaOsobu(boolean sazbaNaOsobu);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Integer getSazbaNaCas();
    public abstract void setSazbaNaCas(java.lang.Integer sazbaNaCas);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getUctovatZalohu();
    public abstract void setUctovatZalohu(boolean uctovatZalohu);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getPodsportyCount();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setPodsportyCount(int count);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract List<SazbaStorna> getSazbyStorna();
    public abstract void setSazbyStorna(List<SazbaStorna> sazbyStorna);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getMinDelkaRezervace();
    public abstract void setMinDelkaRezervace(Integer minDelkaRezervace);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getMaxDelkaRezervace();
    public abstract void setMaxDelkaRezervace(Integer maxDelkaRezervace);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getObjednavkaZaplniObjekt();
    public abstract void setObjednavkaZaplniObjekt(boolean objednavkaZaplniObjekt);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getDelkaRezervaceNasobkem();
    public abstract void setDelkaRezervaceNasobkem(Integer delkaRezervaceNasobkem);
    
    /**
     * barva skupiny, pouziva se v gui
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Color getBarvaPopredi();
    public abstract void setBarvaPopredi(Color barvaPopredi);

    /**
     * barva skupiny, pouziva se v gui
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Color getBarvaPozadi();
    public abstract void setBarvaPozadi(Color barvaPozadi);

    /**
     * urcuje bude-li se na casove ose zobrazovat nazev cinnosti
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getZobrazitText();
    public abstract void setZobrazitText(boolean zobrazitText);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getViditelnyWeb();
    public abstract void setViditelnyWeb(boolean uctovatZalohu);

     /**
     * vrati potrebny  pocet minut medzi koncom 1.rezervacie a zaciatkom 2.rezervacie
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getNavRezervaceOffset();
    public abstract void setNavRezervaceOffset(Integer minutes);

    /**
     * Delka hlavni rezervace, pouzita v pripade, ak sa z rezervacie vytvori hlavna + navazujuce
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getDelkaHlavniRez();
    public abstract void setDelkaHlavniRez(Integer minutes);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getMinimalniPocetOsob();
    public abstract void setMinimalniPocetOsob(Integer minimalniPocetOsob);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getMinutyPredVyhodnocenimKapacity();
    public abstract void setMinutyPredVyhodnocenimKapacity(Integer minuty);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getMaximalniPocetOsobNaZakaznika();
    public abstract void setMaximalniPocetOsobNaZakaznika(Integer maximalniPocetOsobNaZakaznika);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-sport_loc"
     *    role-name="ke-sportu-patri-locale-data"
     *    target-ejb="SportLoc"
     *    target-role-name="locale-data-patri-ke-sportu"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="sport"
     * @ejb.value-object
     *    aggregate="java.util.Map<String,SportLocDetails>"
     *    aggregate-name="LocaleData"
     * @jboss.relation-read-ahead
     *  strategy="on-find"
     *  eager-load-group="*"
     */
    public abstract java.util.Collection getLocaleData();
    public abstract void setLocaleData(java.util.Collection localeDate);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="kategorie_sportu-sport"
     *    role-name="sport-patri-do-kategorie"
     *    target-ejb="SportKategorie"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="sport_kategorie"
     * @ejb.value-object
     *    aggregate="cz.inspire.enterprise.module.sport.ejb.SportKategorieDetails"
     *    aggregate-name="SportKategorie"
     */
    public abstract LocalSportKategorie getSportKategorie();
    public abstract void setSportKategorie(LocalSportKategorie kategorie);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-sport"
     *    role-name="podsport-je-druhem-sportu"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="nadrazeny_sport"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="NadrazenySportId"
     */
    public abstract LocalSport getNadrazenySport();
    public abstract void setNadrazenySport(LocalSport sport);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-sport"
     *    role-name="sport-se-deli-na-podsporty"
     */
    public abstract java.util.Collection getPodrazeneSporty();
    public abstract void setPodrazeneSporty(java.util.Collection podrazeneSporty);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-objekt_sport"
     *    role-name="sport-je-prirazen-objektum"
     */
    public abstract java.util.Collection getObjekty();
    public abstract void setObjekty(java.util.Collection objekty);

        /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-nav_sport"
     *    role-name="sport-ma-navazujici-sport"
     *    target-ejb="Sport"
     *    target-role-name="navazujici-sport-ma-sport"
     *    target-multiple="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="navazujici_sport"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="NavazujiciSportId"
     */
    public abstract LocalSport getNavazujiciSport();
    public abstract void setNavazujiciSport(LocalSport navazujiciSport);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="activity-sport"
     *      role-name="sport-of-activity"
     *      cascade-delete="false"
     * @jboss.relation
     *      related-pk-field="id"
     *      fk-column="activity_id"
     *      fk-constraint="true"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="ActivityId"
     */
    public abstract LocalActivity getActivity();
    public abstract void setActivity(LocalActivity activityLocal);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="sport-sport_instructor"
     *      role-name="sport-has-sport_instructors"
     *      cascade-delete=false
     */
    public abstract Collection<LocalSportInstructor> getSportInstructors();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setSportInstructors(Collection<LocalSportInstructor> sportInstructors);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.value-object
     *      match="*"
     */
    public Set<InstructorDetails> getInstructors() {
        return instructorSet;
    }
    
    public void setInstructors(Set<InstructorDetails> instructors) {
        instructorSet = instructors;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public String getActivityId() {
        LocalActivity activity = getActivity();
        return activity == null ? null : activity.getId();
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SportDetails getDetails() {
        SportDetails details = new SportDetails();
        details.setId(getId());
        details.setTyp(getTyp());
        details.setZboziId(getZboziId());
        details.setSkladId(getSkladId());
        details.setPodsportyCount(getPodsportyCount());
        details.setSazbaJednotek(getSazbaJednotek());
        details.setSazbaNaOsobu(getSazbaNaOsobu());
        details.setSazbaNaCas(getSazbaNaCas());
        details.setUctovatZalohu(getUctovatZalohu());
        details.setSazbyStorna(getSazbyStorna());
        details.setMinDelkaRezervace(getMinDelkaRezervace());
        details.setMaxDelkaRezervace(getMaxDelkaRezervace());
        details.setObjednavkaZaplniObjekt(getObjednavkaZaplniObjekt());
        details.setMaximalniPocetOsobNaZakaznika(getMaximalniPocetOsobNaZakaznika());
        details.setDelkaRezervaceNasobkem(getDelkaRezervaceNasobkem());
        
        details.setBarvaPopredi(getBarvaPopredi());
        details.setBarvaPozadi(getBarvaPozadi());
        details.setZobrazitText(getZobrazitText());
        details.setViditelnyWeb(getViditelnyWeb());

        details.setNavRezervaceOffset(getNavRezervaceOffset());
        details.setDelkaHlavniRez(getDelkaHlavniRez());
        
        details.setMinimalniPocetOsob(getMinimalniPocetOsob());
        details.setMinutyPredVyhodnocenimKapacity(getMinutyPredVyhodnocenimKapacity());
        
        if (getSportKategorie() != null) {
            details.setSportKategorie(getSportKategorie().getDetails());
        }
        
        Iterator it = getLocaleData().iterator();
        Map locData = new HashMap();
        while (it.hasNext()) {
            LocalSportLoc sportLoc = (LocalSportLoc) it.next();
            SportLocDetails sportLocDetails = sportLoc.getDetails();
            locData.put(sportLocDetails.getJazyk(), sportLocDetails);
        }
        details.setLocaleData(locData);

        LocalSport navazujiciSport = getNavazujiciSport();
        if (navazujiciSport != null){
            details.setNavazujiciSportId(navazujiciSport.getId());
        }
        
        LocalActivity activity = getActivity();
        if (activity != null) {
            details.setActivityId(activity.getId());
        }
        
//        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = loadInstructors();
        setInstructors(instructors);
        details.setInstructors(instructors);       
        
        return details;
        
    }
    
    private Set<InstructorDetails> loadInstructors() {
        //        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = new HashSet<InstructorDetails>();
        Collection<LocalSportInstructor> sportInstructors = getSportInstructors();
        for (LocalSportInstructor localSportInstructor : sportInstructors) {
            if (localSportInstructor.getDeleted()) {
                continue;
            }
            LocalInstructor instructor = localSportInstructor.getInstructor();
            if (instructor == null) {
                //add special instructor standing for none instructor
                InstructorDetails noneInstructor = new InstructorDetails();
                noneInstructor.setId(InstructorConstants.ZADNY_INSTRUKTOR_ID);
                instructors.add(noneInstructor);
            } else {
                instructors.add(mapInstructorData(instructor));
            }
        }
        return instructors;
    }
    
    private InstructorDetails mapInstructorData(LocalInstructor localInstructor) {
        InstructorDetails instructor = new InstructorDetails();
        instructor.setId(localInstructor.getId());
        instructor.setColor(localInstructor.getColor());
        instructor.setDeleted(localInstructor.getDeleted());
        instructor.setEmail(localInstructor.getEmail());
        instructor.setFirstName(localInstructor.getFirstName());
        instructor.setLastName(localInstructor.getLastName());
        instructor.setPhoneCode(localInstructor.getPhoneCode());
        instructor.setPhoneNumber(localInstructor.getPhoneNumber());
        instructor.setGoogleCalendarId(localInstructor.getGoogleCalendarId());
        instructor.setGoogleCalendarNotification(localInstructor.getGoogleCalendarNotification());
        instructor.setGoogleCalendarNotificationBefore(localInstructor.getGoogleCalendarNotificationBefore());
//        instructor.setPhoto(localInstructor.getPhoto());
        return instructor;
    }
    
    private void setBasicAttributes(SportDetails sport) {
        setTyp(sport.getTyp());
        setZboziId(sport.getZboziId());
        setSkladId(sport.getSkladId());
        setSazbaJednotek(sport.getSazbaJednotek());
        setSazbaNaOsobu(sport.getSazbaNaOsobu());
        setSazbaNaCas(sport.getSazbaNaCas());
        setUctovatZalohu(sport.getUctovatZalohu());
        setSazbyStorna(sport.getSazbyStorna());
        setMinDelkaRezervace(sport.getMinDelkaRezervace());
        setMaxDelkaRezervace(sport.getMaxDelkaRezervace());
        setObjednavkaZaplniObjekt(sport.getObjednavkaZaplniObjekt());
        setMaximalniPocetOsobNaZakaznika(sport.getMaximalniPocetOsobNaZakaznika());
        setDelkaRezervaceNasobkem(sport.getDelkaRezervaceNasobkem());        
        setBarvaPopredi(sport.getBarvaPopredi());
        setBarvaPozadi(sport.getBarvaPozadi());
        setZobrazitText(sport.getZobrazitText());
        setViditelnyWeb(sport.getViditelnyWeb());
        setNavRezervaceOffset(sport.getNavRezervaceOffset());
        setDelkaHlavniRez(sport.getDelkaHlavniRez());        
        setMinimalniPocetOsob(sport.getMinimalniPocetOsob());
        setMinutyPredVyhodnocenimKapacity(sport.getMinutyPredVyhodnocenimKapacity());
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(SportDetails sport) throws SystemException, InvalidParameterException {       
        setBasicAttributes(sport);
        
        if (sport.getSportKategorie() != null) {
            String kategorieId = sport.getSportKategorie().getId();
            try {
            LocalSportKategorie localKategorie = SportKategorieUtil.getLocalHome()
                    .findByPrimaryKey(kategorieId);
            setSportKategorie(localKategorie);
            } catch (FinderException fe) {
                throw new InvalidParameterException("SportKategorie does not exists.");
            } catch (NamingException ne) {
                throw new SystemException("Could not update SportKategorie.", ne);
            }
        }
        
        
        updateSportInstructor(sport);        
        
        setInstructors(sport.getInstructors());
        // Locale data
        try {
            // Locale data
            // Clean up old locale entities
            Iterator oldIt = new Vector(getLocaleData()).iterator();
            while (oldIt.hasNext()) {
                LocalSportLoc sportLoc = (LocalSportLoc) oldIt.next();
                sportLoc.remove();
            }
            getLocaleData().clear();
            
            Map locData = sport.getLocaleData();
            Iterator it = locData.values().iterator();
            
            // Insert new
            LocalSportLocHome locHome = SportLocUtil.getLocalHome();
            while(it.hasNext()) {
                SportLocDetails locDetails = (SportLocDetails) it.next();
                LocalSportLoc locEntity = locHome.create(locDetails);
                getLocaleData().add(locEntity);
            }
        } catch (Exception e) {
            getEntityContext().setRollbackOnly();
            SystemException se = new SystemException("Areal entity could not be updated.", e);
            logger.error(se);
            throw se;
        }
        
        // parent
        
        String parentId = sport.getNadrazenySportId();
        if (parentId != null) {
            try {
                LocalSport parentSport = SportUtil.getLocalHome().findByPrimaryKey(parentId);
                setNadrazenySport(parentSport);
            } catch (Exception e) {
                getEntityContext().setRollbackOnly();
                throw new InvalidParameterException("Could not set parent sport.", e);
            }
        } else {
            setNadrazenySport(null);
        }

        String navazujiciSport = sport.getNavazujiciSportId();
        if (navazujiciSport != null) {
            try {
                LocalSport navSport = SportUtil.getLocalHome().findByPrimaryKey(navazujiciSport);
                setNavazujiciSport(navSport);
            } catch (Exception e) {
                getEntityContext().setRollbackOnly();
                throw new InvalidParameterException("Could not set navazujici sport.", e);
            }
        } else {
            setNavazujiciSport(null);
        }
        
        String activityId = sport.getActivityId();
        if (activityId != null) {
            try {
                LocalActivity activity = ActivityUtil.getLocalHome().findByPrimaryKey(activityId);
                setActivity(activity);
            } catch (Exception ex) {
                getEntityContext().setRollbackOnly();
                throw new InvalidParameterException("Could not set activity.", ex);
            }
            
        }
    }
    
    private void updateSportInstructor(SportDetails sport) {
        Set<InstructorDetails> instructors = loadInstructors();
        Set<String> oldInstructorIds = new HashSet<String>();
        for (InstructorDetails instructorDetails : instructors) {
            oldInstructorIds.add(instructorDetails.getId());
        }

        Set<String> newInstructorIds = new HashSet<String>();
        for (InstructorDetails instructorDetails : (Set<InstructorDetails>) sport.getInstructors()) {
            newInstructorIds.add(instructorDetails.getId());
        }

        Set<String> deleted = new HashSet<String>(oldInstructorIds);
        deleted.removeAll(newInstructorIds);
        if (!deleted.isEmpty()) {
            String sportId = sport.getId();
            for (String sportInstructorId : deleted) {
                try {
                    LocalSportInstructor sportInstructor;
                    if (InstructorConstants.ZADNY_INSTRUKTOR_ID.equals(sportInstructorId)) {
                        sportInstructor = SportInstructorUtil.getLocalHome()
                                .findBySportWithoutInstructor(sportId);
                    } else {
                        sportInstructor = SportInstructorUtil.getLocalHome()
                                .findBySportAndInstructor(sportId, sportInstructorId);
                    }
                    sportInstructor.setDeleted(true);
                } catch (Exception ex) {
                    logger.error("Nepodarilo se oznacit SportInstructor jako smazany! Id=" + sportInstructorId, ex);
                }
            }
        }

        newInstructorIds.removeAll(oldInstructorIds);
        if (!newInstructorIds.isEmpty()) {
            for (String instructorId : newInstructorIds) {
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivityId());
                sid.setDeleted(false);
                sid.setInstructorId(InstructorConstants.ZADNY_INSTRUKTOR_ID.equals(instructorId) ? null : instructorId);

                LocalSport instance = (LocalSport) getEntityContext().getEJBLocalObject();
                try {
                    SportInstructorUtil.getLocalHome().create(sid, instance);
                } catch (Exception ex) {
                    logger.error("Nepodarilo se vytvorit sport instructor!", ex);
                }
            }
        }
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void addSport(LocalSport sport) throws ApplicationException {
        getPodrazeneSporty().add(sport);
        setPodsportyCount(getPodsportyCount() + 1);
    }
    
    /**
     * Check whether given sport has at least one visible instructor assigned.
     * Set special NONE instructor for given sport in case it has no instructor assigned.
     * 
     * @ejb.interface-method
     *      view-type="local"
     */
    public void checkSportWithoutInstructor() {
        try {
            Long instructorsCount = SportInstructorUtil.getLocalHome().countSportInstructors(getId());
            if (instructorsCount == null || instructorsCount == 0) {
                //add special instructor representing NONE instructor
                LocalSport sport = (LocalSport) getEntityContext().getEJBLocalObject();
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivity().getId());
                sid.setDeleted(false);
                sid.setInstructorId(null);
                SportInstructorUtil.getLocalHome().create(sid, sport);
            }
        } catch (CreateException ex) {
            logger.error("Nepodarilo sa vytvorit SportInstructor entitu bez instruktora pre sport " + getId(), ex);
        } catch (Exception ex) {
            logger.error("Nepodarilo sa zistit pocet instruktorov sportu " + getId(), ex);
        }
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SportDetails sport) throws CreateException {
        String id = sport.getId();
        if (id == null) {
            id = SportUtil.generateGUID(this);
        }
        setId(id);
        setPodsportyCount(0);
        setBasicAttributes(sport);
        return id;
    }
    
    public void ejbPostCreate(SportDetails sport) throws CreateException {
        Map locData = sport.getLocaleData();
        Iterator it = locData.values().iterator();
        try {
            LocalSportLocHome locHome = SportLocUtil.getLocalHome();
            while(it.hasNext()) {
                SportLocDetails locDetails = (SportLocDetails) it.next();
                LocalSportLoc locEntity = locHome.create(locDetails);
                getLocaleData().add(locEntity);
            }
            String navSportId = sport.getNavazujiciSportId();
            if (navSportId != null) {
                LocalSport navSport = SportUtil.getLocalHome().findByPrimaryKey(navSportId);
                setNavazujiciSport(navSport);
            }
            
            String activityId = sport.getActivityId();
            if (activityId != null) {
                LocalActivity activity = ActivityUtil.getLocalHome().findByPrimaryKey(activityId);
                setActivity(activity);

            }
            
            LocalSport instance = (LocalSport) getEntityContext().getEJBLocalObject();
            Set<InstructorDetails> instructors = sport.getInstructors();
            if (instructors != null && !instructors.isEmpty()) {
                for (InstructorDetails instructorDetails : instructors) {
                    SportInstructorDetails sid = new SportInstructorDetails();
                    sid.setActivityId(sport.getActivityId());
                    sid.setDeleted(false);
                    sid.setInstructorId(InstructorConstants.ZADNY_INSTRUKTOR_ID.equals(instructorDetails.getId()) ?
                            null : instructorDetails.getId());
                    SportInstructorUtil.getLocalHome().create(sid, instance);
                }
            } else {
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivityId());
                sid.setDeleted(false);
                SportInstructorUtil.getLocalHome().create(sid, instance);
            }
        } catch (Exception ne) {
            throw new CreateException("Sport entity could not be created. " + ne.getMessage());
        }
        
        if (sport.getSportKategorie() != null) {
            String kategorieId = sport.getSportKategorie().getId();
            try {
            LocalSportKategorie localKategorie = SportKategorieUtil.getLocalHome()
                    .findByPrimaryKey(kategorieId);
            setSportKategorie(localKategorie);
            } catch (Exception fe) {
                throw new CreateException("SportKategorie could not be created.");
            }
        }
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM Sport sport, IN (sport.localeData) AS loc WHERE sport.nadrazenySport IS NOT NULL
     *          AND sport.nadrazenySport.id = ?1 AND loc.jazyk = ?2"
     */
    public abstract Long ejbSelectCountAllByParentAndLanguage(String parentId, String language) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountAllByParentAndLanguage(String parentId, String language) throws FinderException {
        return ejbSelectCountAllByParentAndLanguage(parentId, language);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM Sport sport WHERE sport.sportKategorie IS NOT NULL
     *          AND sport.sportKategorie.id = ?1 "
     */
    public abstract Long ejbSelectCountAllByCategory(String categoryId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountAllByCategory(String categoryId) throws FinderException {
        return ejbSelectCountAllByCategory(categoryId);
    }
    
    /**
     * @ejb.select
     * query="SELECT sport.id FROM Sport sport, IN (sport.localeData) AS loc WHERE sport.nadrazenySport IS NOT NULL
     *          AND sport.nadrazenySport.id = ?1 AND loc.jazyk = ?2 ORDER BY loc.nazev"
     */
    public abstract Collection<String> ejbSelectGetAllIdsByParentAndLanguage(String parentId, String language) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Collection<String> ejbHomeGetAllIdsByParentAndLanguage(String parentId, String language) throws FinderException {
        return ejbSelectGetAllIdsByParentAndLanguage(parentId, language);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM Sport sport, IN (sport.localeData) AS loc WHERE sport.nadrazenySport IS NULL
     *          AND loc.jazyk = ?1"
     */
    public abstract Long ejbSelectCountRootByLanguage(String language) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountRootByLanguage(String language) throws FinderException {
        return ejbSelectCountRootByLanguage(language);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sport.id) FROM Sport sport WHERE sport.sportKategorie IS NULL"
     */
    public abstract Long ejbSelectCountCategoryRoot() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountCategoryRoot() throws FinderException {
        return ejbSelectCountCategoryRoot();
    }
    
    /**
     * @ejb.select
     * query="SELECT sport.id FROM Sport sport, IN (sport.localeData) AS loc WHERE sport.nadrazenySport IS NULL
     *          AND loc.jazyk = ?1 ORDER BY loc.nazev"
     */
    public abstract Collection<String> ejbSelectGetRootIdsByLanguage(String language) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Collection<String> ejbHomeGetRootIdsByLanguage(String language) throws FinderException {
        return ejbSelectGetRootIdsByLanguage(language);
    }
    
    /** Required to implement EntityBean. Not implemented. */
    @Override
    public void ejbRemove() throws RemoveException, EJBException {
        try {
            Collection<LocalSportInstructor> sportInstructors = SportInstructorUtil.getLocalHome().findBySport(getId());
            for (LocalSportInstructor localSportInstructor : sportInstructors) {
                localSportInstructor.setDeleted(true);
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }
    
}