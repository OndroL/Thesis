/**
 * Sport-Server, (c) Inspire CZ 2004-2006
 *
 * OtviraciDobaObjektuBean.java
 * Vytvoreno: 21.1.2004
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospíšil</a>
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import javax.ejb.*;
import cz.inspire.enterprise.module.sport.OtviraciDoba;
import java.util.Date;
import java.util.List;

/**
 * EJB 2.0 OtviraciDobaObjektu Enterprise Bean. Entita reprezentuje otviraci dobu objektu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="OtviraciDobaObjektu"
 *      local-jndi-name="ejb/sport/LocalOtviraciDobaObjektu"
 *      display-name="OtviraciDobaObjektuEJB"
 *      view-type="local"
 * @ejb.value-object
 *      name="OtviraciDobaObjektu"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OtviraciDobaObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="objekt_id"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OtviraciDobaObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObjekt(java.lang.String objektId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObjekt(java.lang.String objektId)"
 *          where="objektid = {0}"
 *          order="platnostod DESC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OtviraciDobaObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObjekt(java.lang.String objektId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObjekt(java.lang.String objektId, int offset, int count)"
 *          where="objektid = {0}"
 *          order="platnostod DESC"
 * 			other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OtviraciDobaObjektu o"
 *      result-type-mapping="Local"
 *      signature="LocalOtviraciDobaObjektu findCurrent(java.lang.String objektId, java.util.Date day)"
 *      @jboss.declared-sql
 *          signature="LocalOtviraciDobaObjektu findCurrent(java.lang.String objektId, java.util.Date day)"
 *          where="(objektid = {0}) AND (platnostod <= {1})"
 *          order="platnostod DESC"
 *          other="LIMIT 1 OFFSET 0"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OtviraciDobaObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAfter(java.lang.String objektId, java.util.Date day)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAfter(java.lang.String objektId, java.util.Date day)"
 *          where="(objektid = {0}) AND (platnostod >= {1})"
 *          order="platnostod DESC"
 * 
 * @ejb.persistence
 *      table-name="otviraci_doba"
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
public abstract class OtviraciDobaObjektuBean implements EntityBean {
    
    protected EntityContext entityContext = null;
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract String getObjektId();
    public abstract void setObjektId(String objektId);
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract java.util.Date getPlatnostOd();
    public abstract void setPlatnostOd(java.util.Date platnostOd);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract OtviraciDoba getOtviraciDoba();
    public abstract void setOtviraciDoba(OtviraciDoba otviraciDoba);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public OtviraciDobaObjektuDetails getDetails() {
        OtviraciDobaObjektuDetails details = new OtviraciDobaObjektuDetails(
                getObjektId(),
                getPlatnostOd(),
                getOtviraciDoba());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(OtviraciDobaObjektuDetails details) {
        setOtviraciDoba(details.getOtviraciDoba());
    }
    
    /**
     * @ejb.select
     * query="SELECT o.platnostOd FROM OtviraciDobaObjektu o WHERE o.objektId = ?1 AND o.platnostOd <= ?2 ORDER BY o.platnostOd DESC"
     */
    public abstract List<Date> ejbSelectGetCurrentIdsByObjectAndDay(String objektId, Date day) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public OtviraciDobaObjektuPK ejbHomeGetCurrentIdsByObjectAndDay(String objektId, Date day) throws FinderException {
        List<Date> dates = ejbSelectGetCurrentIdsByObjectAndDay(objektId, day);
        if (dates == null || dates.isEmpty()) {
            return null;
        }
        return new OtviraciDobaObjektuPK(objektId, dates.get(0));
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public OtviraciDobaObjektuPK ejbCreate(OtviraciDobaObjektuDetails details) throws CreateException {
        setObjektId(details.getObjektId());
        setPlatnostOd(details.getPlatnostOd());
        setOtviraciDoba(details.getOtviraciDoba());
        return null;
    }
    public void ejbPostCreate(OtviraciDobaObjektuDetails details) throws CreateException {
        
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
