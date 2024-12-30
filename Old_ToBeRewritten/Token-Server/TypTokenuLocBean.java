/*
 * SportBean.java
 *
 * Created on 20. leden 2004, 13:05
 */

package cz.inspire.enterprise.module.token;

import java.util.Collection;

import javax.ejb.*;


/**
 * EJB 2.0 TypTokenuLoc Enterprise Bean. Entita reprezentuje jazykove zavisla data patrici
 * entite TypTokenu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="TypTokenuLoc"
 *      local-jndi-name="ejb/token/TypTokenuLoc"
 *      display-name="TypTokenyLocEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="TypTokenuLoc"
 *      match="*"
 * @ejb.persistence
 *      table-name="typ_tokenu_loc"
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
public abstract class TypTokenuLocBean implements EntityBean {

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
    
    // Entity relations ---------------------------------------------------------------------------
     
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public TypTokenuLocDetails getDetails() {
        TypTokenuLocDetails details = new TypTokenuLocDetails(getId(), getJazyk(), getNazev(), getPopis());
        return details;
    }
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(TypTokenuLocDetails typTokenuLoc) throws CreateException {
        String id = typTokenuLoc.getId();
        if (id==null) id = TypTokenuLocUtil.generateGUID(this);
        setId(id);
        setJazyk(typTokenuLoc.getJazyk());
        setNazev(typTokenuLoc.getNazev());
        setPopis(typTokenuLoc.getPopis());        
        return null;
    }
    public void ejbPostCreate(TypTokenuLocDetails typTokenuLoc) throws CreateException {
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
