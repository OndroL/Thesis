/**
 * BaseEntityBean.java
 *
 * Created on 19. brezen 2003, 15:46
 */
package cz.inspire.enterprise.module.common;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 *
 * @author  dominik
 */
public abstract class BaseEntityBean extends ConnectionHelper {
    
    /** Reference to container-managed EntityContext. */
    protected EntityContext entityContext = null;
    
    @Override
    protected EJBContext getContext() {
        return entityContext;
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
    
    protected void removeCollectionEJBObjects(Collection<EJBLocalObject> toRemove) {
        Iterator<EJBLocalObject> iter = toRemove.iterator();
        while (iter.hasNext()) {
            try {
                EJBLocalObject localObject = iter.next();
                localObject.remove();
            } catch (Exception ex) {
                getLogger().error("Nepodarilo se odstranit lokalni objekt!", ex);
            }
        }
    }
    
}