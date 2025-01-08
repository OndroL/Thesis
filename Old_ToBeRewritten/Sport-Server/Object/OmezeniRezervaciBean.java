/**
 * Clubspire, (c) Inspire CZ 2004-2009
 *
 * OmezeniRezervaciBean.java
 * Created on: 17.6.2009
 * Author: Dusan Katona
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.module.sport.OtviraciDoba;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Sport Enterprise Bean. Entita reprezentuje omezeni rezervaci pre web.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="OmezeniRezervaci"
 *      local-jndi-name="ejb/sport/LocalOmezeniRezervaci"
 *      display-name="OmezeniRezervaciEJB"
 *      view-type="local"
 *      primkey-field="objektId"
 * @ejb.value-object
 *      name="OmezeniRezervaci"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OmezeniRezervaci o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="id"
 * @ejb.persistence
 *      table-name="omezeni_rezervaci"
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
 * @version 1.0
 * @author <a href="mailto:dusan.katona@inspire.cz">Dusan Katona</a>
 */
public abstract class OmezeniRezervaciBean implements EntityBean{

    private static Logger logger = Logger.getLogger(OmezeniRezervaciBean.class);

    protected EntityContext entityContext = null;

    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getObjektId();
    public abstract void setObjektId(String objektId);

    /**
     * Omezeni je reprezentovane ako OtviraciDoba (rozsirena tydenni)
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract OtviraciDoba getOmezeni();
    public abstract void setOmezeni(OtviraciDoba omezeni);

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public OmezeniRezervaciDetails getDetails() {
        OmezeniRezervaciDetails omezeniDetails = new OmezeniRezervaciDetails();
        omezeniDetails.setObjektId(getObjektId());
        omezeniDetails.setOmezeni(getOmezeni());
        return omezeniDetails;
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(OmezeniRezervaciDetails details) {
        setOmezeni(details.getOmezeni());
    }

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(OmezeniRezervaciDetails details) throws CreateException {
        if (details.getObjektId() == null) details.setObjektId(OmezeniRezervaciUtil.generateGUID(this));
        setObjektId(details.getObjektId());
        setOmezeni(details.getOmezeni());
        return details.getObjektId();
    }
    public void ejbPostCreate(OmezeniRezervaciDetails details) throws CreateException {
    }


    /**
     * Gets the EntityContext. To be used by classes extending this.
     * @return the EntityContext of the EJB
     */
    protected EntityContext getEntityContext() {
        return entityContext;
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbLoad() throws EJBException, RemoteException {
    }

    public void ejbStore() throws EJBException, RemoteException {
    }
}
