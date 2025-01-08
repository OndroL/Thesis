
package cz.inspire.enterprise.module.sport.ejb;

import javax.ejb.*;

/**
 * EJB 2.0 SportKategorieLoc Enterprise Bean. Entita reprezentuje jazykove zavisla data patrici
 * entite SportKategorie.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="SportKategorieLoc"
 *      local-jndi-name="ejb/sport/LocalSportKategorieLoc"
 *      display-name="SportKategorieLocEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="SportKategorieLoc"
 *      match="*"
 * @ejb.persistence
 *      table-name="sport_kategorie_loc"
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
public abstract class SportKategorieLocBean implements EntityBean {

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
    
    // Entity relations ----------------------------------------------------------------------------

    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public SportKategorieLocDetails getDetails() {
        SportKategorieLocDetails details = new SportKategorieLocDetails(getId(), getJazyk(), getNazev(), getPopis());
        return details;
    }
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SportKategorieLocDetails arealLoc) throws CreateException {
        String id = arealLoc.getId();
        if (id==null) {
            id = SportKategorieLocUtil.generateGUID(this);
        }
        setId(id);
        setJazyk(arealLoc.getJazyk());
        setNazev(arealLoc.getNazev());
        setPopis(arealLoc.getPopis());        
        return null;
    }
    
    public void ejbPostCreate(SportKategorieLocDetails arealLoc) throws CreateException {
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
