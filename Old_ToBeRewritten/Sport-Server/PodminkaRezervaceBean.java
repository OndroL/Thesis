/**
* Clubspire, (c) Inspire CZ 2004-2013
*
* PodminkaRezervaceBean.java
* Created on: 25.7.2014
* Author: Tomáš Kramec
*
*/
package cz.inspire.enterprise.module.sport.ejb;
 
import cz.inspire.enterprise.exception.ApplicationException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Podminka rezervace Enterprise Bean.
 * Entita reprezentuje podminku, ktera musi byt splnena pro vytvoreni rezervace na dany objekt.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="PodminkaRezervace"
 *      local-jndi-name="ejb/sport/LocalPodminkaRezervace"
 *      display-name="PodminkaRezervaceEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="PodminkaRezervace"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PodminkaRezervace o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="priorita"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PodminkaRezervace o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          order="priorita"
 *          other="LIMIT {1} OFFSET {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PodminkaRezervace o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObjekt(java.lang.String objektId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObjekt(java.lang.String objektId, int offset, int count)"
 *          where="objektid = {0}"
 *          order="priorita"
 *          other="LIMIT {2} OFFSET {1}"
 * 
 * @ejb.persistence
 *      table-name="podminka_rezervace"
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
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public abstract class PodminkaRezervaceBean  extends BaseEntityBean implements EntityBean {
   
    private static final Logger logger = Logger.getLogger(PodminkaRezervaceBean.class);
    
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
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     */
    public abstract String getName();
    public abstract void setName(String name);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract long getPriorita();
    public abstract void setPriorita(long priorita);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getObjektRezervaceId();
    public abstract void setObjektRezervaceId(String objectId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Boolean getObjektRezervaceObsazen();
    public abstract void setObjektRezervaceObsazen(Boolean priorita);
    
    // Entity relations ---------------------------------------------------------------------------
        
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-podminka_rezervace"
     *    role-name="podminka-rezervace-pro-objekt"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="objektid"
     * @ejb.value-object
     *    aggregate="java.lang.String"
     *    aggregate-name="ObjektId"
     */
    public abstract LocalObjekt getObjekt();
    public abstract void setObjekt(LocalObjekt objekt);
    
     // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PodminkaRezervaceDetails getDetails() {
        PodminkaRezervaceDetails podminka = getDetailsWithoutObjectId();
        podminka.setObjektId(getObjekt().getId());
        return podminka;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PodminkaRezervaceDetails getDetailsWithoutObjectId() {
        PodminkaRezervaceDetails podminka = new PodminkaRezervaceDetails();
        podminka.setId(getId());
        podminka.setName(getName());
        podminka.setPriorita(getPriorita());
        podminka.setObjektRezervaceId(getObjektRezervaceId());
        podminka.setObjektRezervaceObsazen(getObjektRezervaceObsazen());
        return podminka;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(PodminkaRezervaceDetails details) throws ApplicationException {
        setPriorita(details.getPriorita());
        setName(details.getName());
        setObjektRezervaceId(details.getObjektRezervaceId());
        setObjektRezervaceObsazen(details.getObjektRezervaceObsazen());
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(p.id) FROM PodminkaRezervace p WHERE p.objekt.id = ?1"
     */
    public abstract Long ejbSelectCountPodminkyRezervaci(String objektId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountPodminkyRezervace(String objektId) throws FinderException {
        return ejbSelectCountPodminkyRezervaci(objektId);
    }
    
    /**
     * @ejb.select
     * query="SELECT MAX(p.priorita) FROM PodminkaRezervace p"
     */
    public abstract Long ejbSelectGetMaxPriority() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeGetMaxPriority() throws FinderException {
        return ejbSelectGetMaxPriority();
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(PodminkaRezervaceDetails podminka) throws CreateException {
        if (podminka.getId() == null) podminka.setId(PodminkaRezervaceUtil.generateGUID(this));
        setId(podminka.getId());
        setName(podminka.getName());
        setPriorita(podminka.getPriorita());
        setObjektRezervaceId(podminka.getObjektRezervaceId());
        setObjektRezervaceObsazen(podminka.getObjektRezervaceObsazen());
        return null;
    }
    
    public void ejbPostCreate(PodminkaRezervaceDetails podminka) throws CreateException {
        if (podminka.getObjektId() == null) {
            throw new CreateException("ObjektId cannot be null to create PodminkaRezervace.");
        }
        try {
            //entity relation
            LocalObjektHome objectHome = ObjektUtil.getLocalHome();
            LocalObjekt objekt = objectHome.findByPrimaryKey(podminka.getObjektId());
            setObjekt(objekt);
        } catch (Exception ex) {
            throw new CreateException("Couldn't set Objekt for PodminkaRezervace: " + ex);
        }
    }
    
    /**
     * @ejb.select
     * query="SELECT DISTINCT p.objekt.id FROM PodminkaRezervace p WHERE p.objektRezervaceId = ?1"
     */
    public abstract Collection<String> ejbSelectGetObjectIdsByReservationConditionObject(String objectId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Collection<String> ejbHomeGetObjectIdsByReservationConditionObject(String objectId) throws FinderException {
        return ejbSelectGetObjectIdsByReservationConditionObject(objectId);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(p.id) FROM PodminkaRezervace p WHERE p.objekt.id = ?1"
     */
    public abstract Long ejbSelectCountAllByObject(String objectId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountAllByObject(String objectId) throws FinderException {
        return ejbSelectCountAllByObject(objectId);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(p.id) FROM PodminkaRezervace p"
     */
    public abstract Long ejbSelectCountAll() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountAll() throws FinderException {
        return ejbSelectCountAll();
    }
}