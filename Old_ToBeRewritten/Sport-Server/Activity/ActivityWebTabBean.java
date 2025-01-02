/**
 * Clubspire, (c) Inspire CZ 2004-2018
 *
 * ActivityToWebTabMapBean.java
 * Created on: 16.4.2018
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 ActivityWebTabBean Enterprise Bean. Entita poskytuje mapovani id aktivity na index zalozky webclienta.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="ActivityWebTab"
 *      local-jndi-name="ejb/sport/LocalActivityWebTab"
 *      display-name="ActivityWebTabEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="ActivityWebTab"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityWebTab o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityWebTab o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBySport(java.lang.String sportId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBySport(java.lang.String sportId)"
 *          where="sportid = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityWebTab o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByActivity(java.lang.String activityId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByActivity(java.lang.String activityId)"
 *          where="activityid = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ActivityWebTab o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObject(java.lang.String objectId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObject(java.lang.String objectId)"
 *          where="objectid = {0}"
 * @ejb.persistence
 *      table-name="activity_webtab"
 * @jboss.persistence
 *      create-table="true"
 *      data-source="jdbc/BookingSystemDB"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.container-configuration
 *      name="Optimistic CMP EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class ActivityWebTabBean extends BaseEntityBean implements EntityBean {

    private final Logger logger = Logger.getLogger(ActivityWebTabBean.class);
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object match="*"
     *
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getSportId();
    public abstract void setSportId(String sportId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getActivityId();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setActivityId(String activityId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getObjectId();
    public abstract void setObjectId(String objectId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getTabIndex();
    public abstract void setTabIndex(int tabIndex);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ActivityWebTabDetails getDetails() {
        ActivityWebTabDetails activityWebTab = new ActivityWebTabDetails();
        activityWebTab.setId(getId());
        activityWebTab.setSportId(getSportId());
        activityWebTab.setActivityId(getActivityId());
        activityWebTab.setObjectId(getObjectId());
        activityWebTab.setTabIndex(getTabIndex());        
        return activityWebTab;
    }
    
    // Entity method ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(ActivityWebTabDetails activityWebTab) throws CreateException {
        if (activityWebTab.getId() == null) {
            activityWebTab.setId(ActivityWebTabUtil.generateGUID(this));
        }
        
        setId(activityWebTab.getId());
        setSportId(activityWebTab.getSportId());
        setActivityId(activityWebTab.getActivityId());
        setObjectId(activityWebTab.getObjectId());
        setTabIndex(activityWebTab.getTabIndex());
        return activityWebTab.getId();
    }
    
    public void ejbPostCreate(ActivityWebTabDetails activityWebTab) throws CreateException {
    }

}