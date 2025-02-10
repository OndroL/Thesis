/**
 * Clubspire, (c) Inspire CZ 2004-2017
 *
 * ActivityFavouriteBean.java
 * Created on: May 17, 2017
 * Author: Iveta Jurcikova
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * ActivityFavouriteBean
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="ActivityFavourite"
 *      local-jndi-name="ejb/sport/LocalActivityFavourite"
 *      display-name="ActivityFavouriteEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="ActivityFavourite"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityFavourite o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByZakaznik(java.lang.String zakaznikId, int limit, int offset)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByZakaznik(java.lang.String zakaznikId, int limit, int offset)"
 *          where="(zakaznikid = {0})"  
 *          order="pocet DESC, datumposlednizmeny DESC"
 *          other="LIMIT {1} OFFSET {2}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityFavourite o"
 *      result-type-mapping="Local"
 *      signature="ActivityFavourite findByZakaznikAktivita(java.lang.String zakaznikId, java.lang.String aktivitaId)"
 *      @jboss.declared-sql
 *          signature="ActivityFavourite findByZakaznikAktivita(java.lang.String zakaznikId, java.lang.String aktivitaId)"
 *          where="(zakaznikid = {0}) AND (activityid = {1})"  
 * @ejb.persistence
 *      table-name="activity_favourite"
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
 * @author <a href="mailto:iveta.jurcikova@inspire.cz">Iveta Jurcikova</a>
 */
public abstract class ActivityFavouriteBean extends BaseEntityBean implements EntityBean {

    private static Logger logger = Logger.getLogger(ActivityFavouriteBean.class);

    // Entity fields -------------------------------------------------------------------------------
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method view-type="local"
     */
    public abstract String getId();
    public abstract void setId(String id);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getZakaznikId();
    public abstract void setZakaznikId(String zakaznikId);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getActivityId();
    public abstract void setActivityId(String activityId);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract int getPocet();
    public abstract void setPocet(int pocet);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract Date getDatumPosledniZmeny();
    public abstract void setDatumPosledniZmeny(Date datumPosledniZmeny);

    /**
     * @ejb.create-method
     */
    public String ejbCreate(ActivityFavouriteDetails activityFavourite) throws CreateException {
        String id = activityFavourite.getId();
        if (id == null) {
            id = ActivityFavouriteUtil.generateGUID(this);
        }
        setId(id);
        setZakaznikId(activityFavourite.getZakaznikId());
        setActivityId(activityFavourite.getActivityId());
        setPocet(activityFavourite.getPocet());
        setDatumPosledniZmeny(activityFavourite.getDatumPosledniZmeny());

        return id;
    }
    
    public void ejbPostCreate(ActivityFavouriteDetails activityFavourite)
    throws CreateException {
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public ActivityFavouriteDetails getDetails() {
        ActivityFavouriteDetails details = new ActivityFavouriteDetails();
        details.setId(getId());
        details.setZakaznikId(getZakaznikId());
        details.setActivityId(getActivityId());
        details.setPocet(getPocet());
        details.setDatumPosledniZmeny(getDatumPosledniZmeny());
        return details;
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public void setDetails(ActivityFavouriteDetails details) {
        setZakaznikId(details.getZakaznikId());
        setActivityId(details.getActivityId());
        setPocet(details.getPocet());
        setDatumPosledniZmeny(details.getDatumPosledniZmeny());
    }
}
