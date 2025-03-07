/**
 * ObjektBean.java
 * Created on 20. leden 2004, 13:05
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.exception.InvalidParameterException;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Objekt Enterprise Bean. Entita reprezentuje libovolny objekt sportoviste.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Objekt"
 *      local-jndi-name="ejb/sport/LocalObjekt"
 *      display-name="ObjektEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Objekt"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="id"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByAreal(java.lang.String arealId, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByAreal(java.lang.String arealId, java.lang.String jazyk)"
 *          from=",objekt_loc"
 *          where="(areal = {0}) AND (objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1})"
 *          order="objekt_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBaseByAreal(java.lang.String arealId, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBaseByAreal(java.lang.String arealId, java.lang.String jazyk)"
 *          from=",objekt_loc"
 *          where="(areal = {0}) AND (objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1}) AND is_base_objekt(objekt.id)
 *                  AND primyvstup = FALSE"
 *          order="objekt_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByAreal(java.lang.String arealId, java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByAreal(java.lang.String arealId, java.lang.String jazyk, int offset, int count)"
 *          from=",objekt_loc"
 *          where="(areal = {0}) AND (objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1})"
 *          order="objekt_loc.nazev"
 *          other="LIMIT {3} OFFSET {2}"
 *  * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByTypRezervace(java.lang.Integer typRezervace, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByTypRezervace(java.lang.Integer typRezervace, java.lang.String jazyk)"
 *          from=",objekt_loc"
 *          where="(typrezervace = {0}) AND (objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1})"
 *          order="objekt_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBaseByAreal(java.lang.String arealId, java.lang.String jazyk, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBaseByAreal(java.lang.String arealId, java.lang.String jazyk, int offset, int count)"
 *          from=",objekt_loc"
 *          where="(areal = {0}) AND (objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1}) AND is_base_objekt(objekt.id)
 *                  AND primyvstup = FALSE"
 *          order="objekt_loc.nazev"
 *          other="LIMIT {3} OFFSET {2}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBySport(java.lang.String sportId, java.lang.String jazyk)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBySport(java.lang.String sportId, java.lang.String jazyk)"
 *          from=",objekt_loc,objekt_sport"
 *          where="(objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {1}) AND (objekt.id = objekt_sport.objekt) AND (objekt_sport.sport = {0})"
 *          order="objekt_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByPrimyVstup(java.lang.String jazyk, boolean primyvstup)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByPrimyVstup(java.lang.String jazyk, boolean primyvstup)"
 *          from=",objekt_loc"
 *          where="(objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {0}) AND (primyvstup = {1})"
 *          order="objekt_loc.nazev"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Objekt o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByPrimyVstup(java.lang.String jazyk, int offset, int count, boolean primyvstup)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByPrimyVstup(java.lang.String jazyk, int offset, int count, boolean primyvstup)"
 *          from=",objekt_loc"
 *          where="(objekt.id = objekt_loc.objekt) AND (objekt_loc.jazyk = {0}) AND (primyvstup = {3})"
 *          other="LIMIT {2} OFFSET {1}"
 *          order="objekt_loc.nazev"
 * @ejb.persistence
 *      table-name="objekt"
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
public abstract class ObjektBean extends BaseEntityBean implements EntityBean {
    
    private final Logger logger = Logger.getLogger(ObjektBean.class);

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
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getKapacita();
    public abstract void setKapacita(int kapacita);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract int getCasovaJednotka();
    public abstract void setCasovaJednotka(int casovaJednotka);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract int getTypRezervace();
    public abstract void setTypRezervace(int typRezervace);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getPrimyVstup();
    public abstract void setPrimyVstup(boolean primyVstup);
    
    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
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
    public abstract Integer getVolnoPredRezervaci();
    public abstract void setVolnoPredRezervaci(Integer volnoPredRezervaci);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getVolnoPoRezervaci();
    public abstract void setVolnoPoRezervaci(Integer volnoPredRezervaci);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getZarovnatZacatekRezervace();
    public abstract void setZarovnatZacatekRezervace(Integer zarovnatZacatekRezervace);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getDelkaRezervaceNasobkem();
    public abstract void setDelkaRezervaceNasobkem(Integer delkaRezervaceNasobkem);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Boolean getVicestavovy();
    public abstract void setVicestavovy(Boolean vicestavovy);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getStav();
    public abstract void setStav(Integer stav);
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer[] getReservationStart();
    public abstract void setReservationStart(Integer[] hourMinuteInteger);
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer[] getReservationFinish();
    public abstract void setReservationFinish(Integer[] hourMinuteInteger);

    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     */
    public abstract boolean getOdcitatProcedury();
    public abstract void setOdcitatProcedury(boolean odcitatProcedury);

    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     */
    public abstract boolean getRezervaceNaTokeny();
    public abstract void setRezervaceNaTokeny(boolean odcitatProcedury);

    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     */
    public abstract boolean getRucniUzavreniVstupu();
    public abstract void setRucniUzavreniVstupu(boolean rucniUzavreniVstupu);
    
    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     */
    public abstract boolean getPozastavitVstup();
    public abstract void setPozastavitVstup(boolean pozastavitVstup);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getShowProgress();
    public abstract void setShowProgress(boolean showProgress);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getCheckTokensCount();
    public abstract void setCheckTokensCount(boolean checkTokensCount);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getSelectInstructor();
    public abstract void setSelectInstructor(boolean selectInstructor);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getShowInstructorName();
    public abstract void setShowInstructorName(boolean showInstructorName);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract boolean getShowSportName();
    public abstract void setShowSportName(boolean showSportName);

    /**
     * @ejb.persistent-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     */
    public abstract boolean getUpraveniCasuVstupu();
    public abstract void setUpraveniCasuVstupu(boolean upraveniCasuVstupu);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getVytvoreniRezervacePredZacatkem();
    public abstract void setVytvoreniRezervacePredZacatkem(Integer vytvoreniRezervacePredZacatkem);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getEditaceRezervacePredZacatkem();
    public abstract void setEditaceRezervacePredZacatkem(Integer editaceRezervacePredZacatkem);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Integer getZruseniRezervacePredZacatkem();
    public abstract void setZruseniRezervacePredZacatkem(Integer zruseniRezervacePredZacatkem);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-objekt_sport"
     *    role-name="objekt-ma-prirazene-sporty"
     */
    public abstract java.util.Collection getObjektSports();
    public abstract void setObjektSports(java.util.Collection objektSports);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-objekt_loc"
     *    role-name="k-objektu-patri-locale-data"
     *    target-ejb="ObjektLoc"
     *    target-role-name="locale-data-patri-k-objektu"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="objekt"
     * @ejb.value-object
     *    aggregate="java.util.Map<String,ObjektLocDetails>"
     *    aggregate-name="LocaleData"
     * @jboss.relation-read-ahead
     *  strategy="on-find"
     *  eager-load-group="*"
     */
    public abstract java.util.Collection<LocalObjektLoc> getLocaleData();
    public abstract void setLocaleData(java.util.Collection<LocalObjektLoc> localeDate);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="areal-objekt"
     *    role-name="objekt-patri-do-arealu"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="areal"
     * @ejb.value-object
     *    aggregate="cz.inspire.enterprise.module.sport.ejb.ArealDetails"
     *    aggregate-name="Areal"
     */
    public abstract LocalAreal getAreal();
    public abstract void setAreal(LocalAreal areal);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-ovladacObjektu"
     *    role-name="objekt-ma-ovladac"
     *    target-ejb="OvladacObjektu"
     *    target-role-name="ovladac-patri-k-objektu"
     *    target-cascade-delete="true"
     * @jboss.target-relation
     *    related-pk-field="id"
     *    fk-column="objektId"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.util.Collection<OvladacObjektuDetails>"
     *    aggregate-name="OvladaceObjektu"
     * @jboss.relation-read-ahead
     *  strategy="on-find"
     *  eager-load-group="*"
     */
    public abstract java.util.Collection<LocalOvladacObjektu> getOvladaceObjektu();
    public abstract void setOvladaceObjektu(java.util.Collection<LocalOvladacObjektu> ovladaceObjektu);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-objekt"
     *    role-name="podobjekt-patri-k-objektu"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="nadobjekt"
     *    fk-constraint="true"
     * @jboss.relation-table
     *      table-name="nadobjekt_objekt"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.util.Set<String>"
     *    aggregate-name="Nadobjekty"
     */
    public abstract Collection<LocalObjekt> getNadobjekty();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setNadobjekty(Collection<LocalObjekt> nadobjekty);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-objekt"
     *    role-name="objekt-obsahuje-podobjekty"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="objekt"
     *    fk-constraint="true"
     * @jboss.relation-table
     *      table-name="nadobjekt_objekt"
     * @ejb.value-object match="rel"
     *    aggregate="java.util.Set<String>"
     *    aggregate-name="Podobjekty"
     */
    public abstract Collection getPodobjekty();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setPodobjekty(Collection podobjekty);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-podminka_rezervace"
     *    role-name="objekt-ma-definovane-podminky-rezervaci"
     * @ejb.value-object match="rel"
     *    aggregate="java.util.List<cz.inspire.enterprise.module.sport.ejb.PodminkaRezervaceDetails>"
     *    aggregate-name="PodminkyRezervaci"
     */
     public abstract java.util.Collection<LocalPodminkaRezervace> getPodminkyRezervaci();
     public abstract void setPodminkyRezervaci(java.util.Collection<LocalPodminkaRezervace> objekty);       

    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getGoogleCalendarId();
    public abstract void setGoogleCalendarId(String googleCalendarId);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract boolean getGoogleCalendarNotification();
    public abstract void setGoogleCalendarNotification(boolean googleCalendarNotification);   
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getGoogleCalendarNotificationBefore();
    public abstract void setGoogleCalendarNotificationBefore(int googleCalendarNotificationBefore);  
        
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object
     */
    public List<SportDetails> getSports() {
        List<SportDetails> ret = new ArrayList<SportDetails>();
        List<LocalObjektSport> localObjektSportList = new ArrayList<LocalObjektSport>(getObjektSports());
//        Seradim podle indexu
        Collections.sort(localObjektSportList, new ObjektSportComparator());
        
        Iterator<LocalObjektSport> iter = localObjektSportList.iterator();
        while (iter.hasNext()) {
            LocalObjektSport localObjektSport = iter.next();
            ret.add(localObjektSport.getSport().getDetails());
        }
        
        return Collections.synchronizedList(ret);
    }
   
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object
     */
    public void setSports(List<SportDetails> sports) {
        Iterator<LocalObjektSport> it = getObjektSports().iterator();
        while (it.hasNext()) {
            LocalObjektSport objektSport = it.next();
            try {
                objektSport.remove();
            } catch (Exception ex) {
                logger.error("Nepodarilo se odstranit ObjektSport!", ex);
            }
        }
        
        setCinnosti(sports);        
    }
    
    // Business methods ----------------------------------------------------------------------------
    
    private void setAttributes(ObjektDetails objekt) {
        setKapacita(objekt.getKapacita());
        setCasovaJednotka(objekt.getCasovaJednotka());
        setTypRezervace(objekt.getTypRezervace());
        setPrimyVstup(objekt.getPrimyVstup());
        setMinDelkaRezervace(objekt.getMinDelkaRezervace());
        setMaxDelkaRezervace(objekt.getMaxDelkaRezervace());
        setVolnoPredRezervaci(objekt.getVolnoPredRezervaci());
        setVolnoPoRezervaci(objekt.getVolnoPoRezervaci());
        setZarovnatZacatekRezervace(objekt.getZarovnatZacatekRezervace());
        setDelkaRezervaceNasobkem(objekt.getDelkaRezervaceNasobkem());
        setVicestavovy(objekt.getVicestavovy());
        setStav(objekt.getStav());
        setReservationStart(objekt.getReservationStart());
        setReservationFinish(objekt.getReservationFinish());
        setOdcitatProcedury(objekt.getOdcitatProcedury());
        setRezervaceNaTokeny(objekt.getRezervaceNaTokeny());
        setRucniUzavreniVstupu(objekt.getRucniUzavreniVstupu());
        setUpraveniCasuVstupu(objekt.getUpraveniCasuVstupu());
        setPozastavitVstup(objekt.getPozastavitVstup());
        setShowProgress(objekt.getShowProgress());
        setCheckTokensCount(objekt.getCheckTokensCount());
        setSelectInstructor(objekt.getSelectInstructor());
        setShowInstructorName(objekt.getShowInstructorName());
        setShowSportName(objekt.getShowSportName());
        setVytvoreniRezervacePredZacatkem(objekt.getVytvoreniRezervacePredZacatkem());
        setEditaceRezervacePredZacatkem(objekt.getEditaceRezervacePredZacatkem());
        setZruseniRezervacePredZacatkem(objekt.getZruseniRezervacePredZacatkem());
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ObjektDetails getDetailsWithoutSports() {
        ObjektDetails details = new ObjektDetails();
        details.setId(getId());
        details.setKapacita(getKapacita());
        details.setCasovaJednotka(getCasovaJednotka());
        details.setTypRezervace(getTypRezervace());
        details.setPrimyVstup(getPrimyVstup());
        details.setMinDelkaRezervace(getMinDelkaRezervace());
        details.setMaxDelkaRezervace(getMaxDelkaRezervace());
        details.setVolnoPredRezervaci(getVolnoPredRezervaci());
        details.setVolnoPoRezervaci(getVolnoPoRezervaci());
        details.setZarovnatZacatekRezervace(getZarovnatZacatekRezervace());
        details.setDelkaRezervaceNasobkem(getDelkaRezervaceNasobkem());
        details.setVicestavovy(getVicestavovy());
        details.setStav(getStav());
        details.setReservationStart(getReservationStart());
        details.setReservationFinish(getReservationFinish());
        details.setOdcitatProcedury(getOdcitatProcedury());
        details.setRezervaceNaTokeny(getRezervaceNaTokeny());
        details.setRucniUzavreniVstupu(getRucniUzavreniVstupu());
        details.setUpraveniCasuVstupu(getUpraveniCasuVstupu());
        details.setPozastavitVstup(getPozastavitVstup());
        details.setShowProgress(getShowProgress());
        details.setCheckTokensCount(getCheckTokensCount());
        details.setSelectInstructor(getSelectInstructor());
        details.setShowInstructorName(getShowInstructorName());
        details.setShowSportName(getShowSportName());

        details.setAreal(getAreal().getDetails());
        Iterator it = getLocaleData().iterator();
        Map locData = new HashMap();
        while (it.hasNext()) {
            LocalObjektLoc objektLoc = (LocalObjektLoc) it.next();
            ObjektLocDetails objektLocDetails = objektLoc.getDetails();
            locData.put(objektLocDetails.getJazyk(), objektLocDetails);
        }
        details.setLocaleData(locData);        
        
        Collection<LocalOvladacObjektu> ovladaceObjektu = getOvladaceObjektu();
        List<OvladacObjektuDetails> ovladaceDetails = new ArrayList<OvladacObjektuDetails>();
        if (ovladaceObjektu != null) {
            for (LocalOvladacObjektu ovl : ovladaceObjektu) {
                ovladaceDetails.add(ovl.getDetails());
            }
        }
        details.setOvladaceObjektu(ovladaceDetails);
        details.setNadobjekty(getObjektIds(getNadobjekty()));        
        details.setPodobjekty(getObjektIds(getPodobjekty()));
        details.setPodminkyRezervaci(getPodminkyRezervaciInternal());   
        details.setGoogleCalendarId(getGoogleCalendarId());
        details.setGoogleCalendarNotification(getGoogleCalendarNotification());
        details.setGoogleCalendarNotificationBefore(getGoogleCalendarNotificationBefore());
        details.setVytvoreniRezervacePredZacatkem(getVytvoreniRezervacePredZacatkem());
        details.setEditaceRezervacePredZacatkem(getEditaceRezervacePredZacatkem());
        details.setZruseniRezervacePredZacatkem(getZruseniRezervacePredZacatkem());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ObjektDetails getDetails() {
        ObjektDetails details = getDetailsWithoutSports();
        details.setSports(getSports());             
        return details;
    }
    
    private Set<String> getObjektIds(Collection<LocalObjekt> localObjekts) {
        Set<String> ret = Collections.synchronizedSet(new HashSet<String>());
        for (LocalObjekt localObjekt : localObjekts) {
            ret.add(localObjekt.getId());
        }        
        return ret;
    }
    
    /**
     * Upravi entitu.
     * @param objekt popis entity
     * @ejb.interface-method view-type="local"
     * @throws InvalidParameterException při chybném popisu entity
     * @throws SystemException při systémové chybě
     */
    public void setDetails(ObjektDetails objekt) throws SystemException, InvalidParameterException {
        setAttributes(objekt);
        
        Iterator oldIt, it;
        
        try {
            // Locale data
            // Clean up old locale entities
            oldIt = new ArrayList<LocalObjektLoc>(getLocaleData()).iterator();
            while (oldIt.hasNext()) {
                LocalObjektLoc objektLoc = (LocalObjektLoc) oldIt.next();
                objektLoc.remove();
            }
            // Insert new locale entities
            Collection locData = objekt.getLocaleData().values();
            it = locData.iterator();
            LocalObjektLocHome locHome = ObjektLocUtil.getLocalHome();
            while(it.hasNext()) {
                ObjektLocDetails locDetails = (ObjektLocDetails) it.next();
                LocalObjektLoc locEntity = locHome.create(locDetails);
                getLocaleData().add(locEntity);
            }
            
        } catch (Exception e) {
            throw new SystemException("Could not update Objekt.", e);
        }
        // areal
        
        String arealId = objekt.getAreal().getId();
        
        try {
            LocalAreal areal = ArealUtil.getLocalHome().findByPrimaryKey(arealId);
            setAreal(areal);
        } catch (FinderException fe) {
            throw new InvalidParameterException("Areal does not exists.");
        } catch (NamingException ne) {
            throw new SystemException("Could not update Objekt.", ne);
        }
        
        // sports
        try {                  
            LocalObjektSportHome objektSportHome = ObjektSportUtil.getLocalHome();
            Collection<LocalObjektSport> col = objektSportHome.findByObjekt(getId());
            for (LocalObjektSport localObjektSport : col) {
                localObjektSport.remove();
            }
        } catch (Exception ex) {
            logger.error("Nepodarilo se vymazat seznam cinnosti!", ex);
        }
        
        setCinnosti(objekt.getSports());
        
        //OVLADANI
        try {
            LocalOvladacObjektuHome ovladacObjektuHome = OvladacObjektuUtil.getLocalHome();
            Collection<LocalOvladacObjektu> ovladaceEntity = ovladacObjektuHome.findByObjekt(objekt.getId());
            for (LocalOvladacObjektu localOvl : ovladaceEntity) {
                localOvl.remove();
            }
        } catch (Exception e) {
            throw new SystemException("Could not remove ovladace from object: " + getId(), e);
        }
        setOvladace(objekt.getOvladaceObjektu());        
        setNadobjekts(objekt);
        setPodobjekts(objekt);
        setPodminkyRezervaciInternal(objekt.getPodminkyRezervaci());
        setGoogleCalendarId(objekt.getGoogleCalendarId());
        setGoogleCalendarNotification(objekt.getGoogleCalendarNotification());
        setGoogleCalendarNotificationBefore(objekt.getGoogleCalendarNotificationBefore());
    }
    
    private void setNadobjekts(ObjektDetails objekt) {        
        getNadobjekty().clear();
        if (objekt.getNadobjekty() != null) {
            try {               
                
                LocalObjektHome objektHome = ObjektUtil.getLocalHome();
                Iterator<String> iter = objekt.getNadobjekty().iterator();
                
                while(iter.hasNext()) {
                    String objektId = iter.next();
                    LocalObjekt localObjekt = objektHome.findByPrimaryKey(objektId);
                    getNadobjekty().add(localObjekt);
                }
            } catch (Exception ex) {
                logger.error("Nepodarilo se nastavit nadobjekty!", ex);
            }
        }
    }
    
    private void setPodobjekts(ObjektDetails objekt) {
            getPodobjekty().clear();
        if (objekt.getPodobjekty() != null) {
            try {               
            
                LocalObjektHome objektHome = ObjektUtil.getLocalHome();
                Iterator<String> iter = objekt.getPodobjekty().iterator();
                
                while(iter.hasNext()) {
                    String objektId = iter.next();
                    LocalObjekt localObjekt = objektHome.findByPrimaryKey(objektId);
                    getPodobjekty().add(localObjekt);
                }
            } catch (Exception ex) {
                logger.error("Nepodarilo se nastavit podobjekty!", ex);
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
    public String ejbCreate(ObjektDetails objekt) throws CreateException {
        if (objekt.getId() == null) {
            objekt.setId(ObjektUtil.generateGUID(this));
        }
        
        setId(objekt.getId());
        setKapacita(objekt.getKapacita());
        setCasovaJednotka(objekt.getCasovaJednotka());
        setTypRezervace(objekt.getTypRezervace());
        setPrimyVstup(objekt.getPrimyVstup());
        setMinDelkaRezervace(objekt.getMinDelkaRezervace());
        setMaxDelkaRezervace(objekt.getMaxDelkaRezervace());
        setVolnoPredRezervaci(objekt.getVolnoPredRezervaci());
        setVolnoPoRezervaci(objekt.getVolnoPoRezervaci());
        setZarovnatZacatekRezervace(objekt.getZarovnatZacatekRezervace());
        setDelkaRezervaceNasobkem(objekt.getDelkaRezervaceNasobkem());
        setVicestavovy(objekt.getVicestavovy());
        setStav(objekt.getStav());
        setReservationStart(objekt.getReservationStart());
        setReservationFinish(objekt.getReservationFinish());
        setOdcitatProcedury(objekt.getOdcitatProcedury());
        setRezervaceNaTokeny(objekt.getRezervaceNaTokeny());
        setRucniUzavreniVstupu(objekt.getRucniUzavreniVstupu());
        setUpraveniCasuVstupu(objekt.getUpraveniCasuVstupu());
        setShowProgress(objekt.getShowProgress());
        setGoogleCalendarId(objekt.getGoogleCalendarId());
        setGoogleCalendarNotification(objekt.getGoogleCalendarNotification());
        setGoogleCalendarNotificationBefore(objekt.getGoogleCalendarNotificationBefore());
        setVytvoreniRezervacePredZacatkem(objekt.getVytvoreniRezervacePredZacatkem());
        setEditaceRezervacePredZacatkem(objekt.getEditaceRezervacePredZacatkem());
        setZruseniRezervacePredZacatkem(objekt.getZruseniRezervacePredZacatkem());
        return objekt.getId();
    }
    
    public void ejbPostCreate(ObjektDetails objekt) throws CreateException {
        Collection locData = objekt.getLocaleData().values();
        Iterator it = locData.iterator();
        try {
            LocalObjektLocHome locHome = ObjektLocUtil.getLocalHome();
            while(it.hasNext()) {
                ObjektLocDetails locDetails = (ObjektLocDetails) it.next();
                LocalObjektLoc locEntity = locHome.create(locDetails);
                getLocaleData().add(locEntity);
            }
            
            setCinnosti(objekt.getSports());
            
            setOvladace(objekt.getOvladaceObjektu());           
        } catch (Exception e) {
            throw new CreateException("Sport entity could not be created: " + e.toString());
        }
        
        setNadobjekts(objekt);
        setPodobjekts(objekt);
        setPodminkyRezervaciInternal(objekt.getPodminkyRezervaci());
    }
    
    private void setOvladace(Collection<OvladacObjektuDetails> ovladace) {
        if (ovladace != null && !ovladace.isEmpty()) {
            try {
                LocalOvladacObjektuHome ovlLocalHome = OvladacObjektuUtil.getLocalHome();
                for (OvladacObjektuDetails ovl : ovladace) {
                    ovl.setObjektId(getId());
                    LocalOvladacObjektu ovlLoca = ovlLocalHome.create(ovl);
                    getOvladaceObjektu().add(ovlLoca);
                }
            } catch (Exception e) {
                logger.error("Cannot set ovladace for objekt " + getId(), e);
            }
        }
    }
    
    private void setCinnosti(List<SportDetails> sportList) {
        Iterator<SportDetails> it = sportList.iterator();
        
        try {
            LocalObjektSportHome objektSportHome = ObjektSportUtil.getLocalHome();
            int index = 0;
            while(it.hasNext()) {
                SportDetails sportDetails = it.next();
                ObjektSportDetails objektSportDetails = new ObjektSportDetails();
                objektSportDetails.setObjektId(getId());
                objektSportDetails.setIndex(index++);
                objektSportDetails.setSportId(sportDetails.getId());
                LocalObjektSport localObjektSport = objektSportHome.create(objektSportDetails);
                getObjektSports().add(localObjektSport);
            }
        } catch (Exception ex) {
            logger.error("Nepodarilo se nastavit ObjektSports", ex);
        }
    }
    
    private void setPodminkyRezervaciInternal(List<PodminkaRezervaceDetails> podminkyRezervace) {
        Collection<LocalPodminkaRezervace> podminkyRezervaciOld = new ArrayList(getPodminkyRezervaci());
        try {
            if (!podminkyRezervaciOld.isEmpty()) {
                for (LocalPodminkaRezervace localPodm : podminkyRezervaciOld) {
                    localPodm.remove();
                }
            }
            if (podminkyRezervace != null && !podminkyRezervace.isEmpty()) {
                    LocalPodminkaRezervaceHome podminkaRezervaceHome = PodminkaRezervaceUtil.getLocalHome();
                    long priority = 0;
                    for (PodminkaRezervaceDetails podm : podminkyRezervace) {
                        podm.setObjektId(getId());
                        podm.setPriorita(priority++);
                        LocalPodminkaRezervace podmNew = podminkaRezervaceHome.create(podm);
                        getPodminkyRezervaci().add(podmNew);
                    }
            }
        } catch (Exception e) {
            logger.error("Cannot set reservation conditions for object " + getId(), e);
        }
    }
    
    public List<PodminkaRezervaceDetails> getPodminkyRezervaciInternal() {
        Collection<LocalPodminkaRezervace> podminkyRezervaciLocal = getPodminkyRezervaci();
        List<PodminkaRezervaceDetails> podminkyRezervaci = new ArrayList<PodminkaRezervaceDetails>();
        if (podminkyRezervaciLocal == null || podminkyRezervaciLocal.isEmpty()) {
            return podminkyRezervaci;
        }
        for (LocalPodminkaRezervace podmLocal : podminkyRezervaciLocal) {
            PodminkaRezervaceDetails podm = podmLocal.getDetailsWithoutObjectId();
            podm.setObjektId(getId());
            podminkyRezervaci.add(podm);
        }
        
        Collections.sort(podminkyRezervaci, new PodminkyRezervaciComparator());
        
        return podminkyRezervaci;
    }
 
//    ====================finders===============================    
    
    /**
     * @ejb.select
     * query="SELECT o.id FROM Objekt o WHERE o.areal.id = ?1"
     */
    public abstract Collection<String> ejbSelectObjektIdsOfAreal(String arealId) throws FinderException;
                
    /**
     * @ejb.home-method
     * view-type="local"
     */
    public Collection<String> ejbHomeGetObjektIdsOfAreal(String arealId) throws FinderException {
        return ejbSelectObjektIdsOfAreal(arealId);
    }
    
//    ==========================================================

    private static class ObjektSportComparator implements Comparator<LocalObjektSport>, Serializable {

        private static final long serialVersionUID = 1l;
        
        public ObjektSportComparator() {
        }

        @Override
        public int compare(LocalObjektSport o1, LocalObjektSport o2) {
            return o1.getIndex() - o2.getIndex();
        }
    }

    private static class PodminkyRezervaciComparator implements Comparator<PodminkaRezervaceDetails>, Serializable {

        private static final long serialVersionUID = 1l;
        
        public PodminkyRezervaciComparator() {
        }

        @Override
        public int compare(PodminkaRezervaceDetails o1, PodminkaRezervaceDetails o2) {
            Long p1 = o1.getPriorita();
            Long p2 = o2.getPriorita();
            return p1.compareTo(p2);
        }
    }
    
}
