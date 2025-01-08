/**
 * Clubspire, (c) Inspire CZ 2004-2012
 *
 * ObjektSportBean.java
 * Created on: 22.11.2012
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import javax.ejb.*;

/**
 * EJB 2.0 Sport Enterprise Bean.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="ObjektSport"
 *      local-jndi-name="ejb/sport/LocalObjektSport"
 *      display-name="ObjektSportEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="ObjektSport"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ObjektSport o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObjekt(java.lang.String objektId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObjekt(java.lang.String objektId)"
 *          where="(objekt = {0})"
 * @ejb.persistence
 *      table-name="objekt_sport"
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
 *
 * @version 1.0
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class ObjektSportBean implements EntityBean {
    
    protected EntityContext entityContext = null;
    
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
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract int getIndex();
    public abstract void setIndex(int id);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sport-objekt_sport"
     *    role-name="objekt_sport-odkazuje-na-sport"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="sport"
     *    dbindex="true"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="SportId"
     */
    public abstract LocalSport getSport();
    public abstract void setSport(LocalSport sport);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="objekt-objekt_sport"
     *    role-name="objekt_sport-odkazuje-na-objekt"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="objekt"
     *    dbindex="true"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="ObjektId"
     */
    public abstract LocalObjekt getObjekt();
    public abstract void setObjekt(LocalObjekt objekt);
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(ObjektSportDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(ObjektSportUtil.generateGUID(this));
        }
        setId(details.getId());
        setIndex(details.getIndex());
        return details.getId();
    }
    
    public void ejbPostCreate(ObjektSportDetails details) throws CreateException {
        if (details.getSportId() != null) {
            try {
                LocalSport sport = SportUtil.getLocalHome().findByPrimaryKey(details.getSportId());
                setSport(sport);
            } catch (Exception ex) {
                throw new CreateException("ObjektSport couldn't be created: " + ex);
            }
            
            try {
                LocalObjekt objekt = ObjektUtil.getLocalHome().findByPrimaryKey(details.getObjektId());
                setObjekt(objekt);
            } catch (Exception ex) {
                throw new CreateException("ObjektSport couldn't be created: " + ex);
            }
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
