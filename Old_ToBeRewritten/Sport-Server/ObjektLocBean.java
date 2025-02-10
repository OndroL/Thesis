/**
 * ObjektLocBean.java
 * Created on 20. leden 2004, 13:05
 */
package cz.inspire.enterprise.module.sport.ejb;

import javax.ejb.*;

/**
 * EJB 2.0 ObjektLoc Enterprise Bean. Entita reprezentuje jazykove zavisla data patrici
 * entite Objekt.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="ObjektLoc"
 *      local-jndi-name="ejb/sport/LocalObjektLoc"
 *      display-name="ObjektLocEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="ObjektLoc"
 *      match="*"
 * @ejb.persistence
 *      table-name="objekt_loc"
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
public abstract class ObjektLocBean implements EntityBean {

    protected EntityContext entityContext = null;
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getJazyk();
    public abstract void setJazyk(String jazyk);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getNazev();
    public abstract void setNazev(String nazev);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getPopis();
    public abstract void setPopis(String popis);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getZkracenyNazev();
    public abstract void setZkracenyNazev(String zkracenyNazev);
    
     // Entity relations ---------------------------------------------------------------------------
     
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ObjektLocDetails getDetails() {
        ObjektLocDetails details = new ObjektLocDetails(getId(), getJazyk(), getNazev(), getPopis(), getZkracenyNazev());
        return details;
    }
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(ObjektLocDetails objektLoc) throws CreateException {
        String id = objektLoc.getId();
        if (id==null) id = ObjektLocUtil.generateGUID(this);
        setId(id);        
        setJazyk(objektLoc.getJazyk());
        setNazev(objektLoc.getNazev());
        setPopis(objektLoc.getPopis());
        setZkracenyNazev(objektLoc.getZkracenyNazev());
        return null;
    }
    public void ejbPostCreate(ObjektLocDetails objektLoc) throws CreateException {
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
