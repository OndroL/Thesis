
package cz.inspire.enterprise.module.license;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;


/**
 * EJB 2.0 Permanentka Enterprise Bean. Entita reprezentuje permanentku.
 *
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="License"
 *      local-jndi-name="ejb/vip/LocalLicense"
 *      display-name="LicenseEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="License"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM License o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="createdDate"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM License o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(java.util.Date termFrom, java.util.Date termTo, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(java.util.Date termFrom, java.util.Date termTo, int offset, int count)"
 *          from=",ucet"
 *          where="(ucet = ucet.cislo) AND (ucet.datumvystaveni >= {0}) AND (ucet.datumvystaveni <= {1}) AND (ucet.datumstorna IS NULL)"
 *          order="ucet.datumvystaveni DESC"
 * 			other="LIMIT {3} OFFSET {2}"
 * @ejb.persistence
 *      table-name="license"
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
 * @author Marek
 */
public abstract class LicenseBean extends BaseEntityBean implements EntityBean {
    
    private static Logger logger = Logger.getLogger(LicenseBean.class);
    protected Logger getLogger() { return logger; }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
     /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getCenterId();
    public abstract void setCenterId(int centerId);
    
     /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getCustomer();
    public abstract void setCustomer(String customer);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getValid();
    public abstract void setValid(Boolean valid);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getCenterOnline();
    public abstract void setCenterOnline(Boolean centerOnline);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getValidFromSet();
    public abstract void setValidFromSet(Boolean validFromSet);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Date getValidFrom();
    public abstract void setValidFrom(Date validFrom);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getValidToSet();
    public abstract void setValidToSet(Boolean validToSet);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Date getValidTo();
    public abstract void setValidTo(Date validTo);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getActivityLimit();
    public abstract void setActivityLimit(Integer activityLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getSportCenterLimit();
    public abstract void setSportCenterLimit(Integer sportCenterLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getSportCustomersLimit();
    public abstract void setSportCustomersLimit(Integer sportCustomersLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getUsersLimit();
    public abstract void setUsersLimit(Integer usersLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getCustomerGroupsLimit();
    public abstract void setCustomerGroupsLimit(Integer customerGroupsLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getPokladnaLimit();
    public abstract void setPokladnaLimit(Integer pokladnaLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getSkladLimit();
    public abstract void setSkladLimit(Integer skladLimit);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getOvladaniQuido();
    public abstract void setOvladaniQuido(Boolean ovladaniQuido);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Long getModules();
    public abstract void setModules(Long modules);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Integer getMaxClients();
    public abstract void setMaxClients(Integer maxClients);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getHash();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setHash(String hash);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Date getCreatedDate();
    public abstract void setCreatedDate(Date createdDate);
        
    /**
    * @ejb.persistent-field
    * @ejb.value-object match="*"
    * @ejb.interface-method
    *    view-type="local"
    */
    public abstract Date getGeneratedDate();
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setGeneratedDate(Date generatedDate);
    
    // Entity relations ---------------------------------------------------------------------------
    
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public LicenseDetails getDetails() {
        LicenseDetails details =  new LicenseDetails();
        details.setId(getId());
        details.setCenterId(getCenterId());
        details.setCustomer(getCustomer());
        details.setValid(getValid());
        details.setCenterOnline(getCenterOnline());
        details.setValidFrom(getValidFrom());
        details.setValidFromSet(getValidFromSet());
        details.setValidTo(getValidTo());
        details.setValidToSet(getValidToSet());
        details.setActivityLimit(getActivityLimit());
        details.setSportCenterLimit(getSportCenterLimit());
        details.setSportCustomersLimit(getSportCustomersLimit());
        details.setUsersLimit(getUsersLimit());
        details.setCustomerGroupsLimit(getCustomerGroupsLimit());
        details.setPokladnaLimit(getPokladnaLimit());
        details.setSkladLimit(getSkladLimit());
        details.setOvladaniQuido(getOvladaniQuido());
        details.setModules(getModules());
        details.setMaxClients(getMaxClients());
        details.setHash(getHash());
        details.setCreatedDate(getCreatedDate());
        details.setGeneratedDate(getGeneratedDate());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(LicenseDetails details) {
        setCenterId(details.getCenterId());
        setCustomer(details.getCustomer());
        setValid(details.getValid());
        setCenterOnline(details.getCenterOnline());
        setValidFrom(details.getValidFrom());
        setValidFromSet(details.getValidFromSet());
        setValidTo(details.getValidTo());
        setValidToSet(details.getValidToSet());
        setActivityLimit(details.getActivityLimit());
        setSportCenterLimit(details.getSportCenterLimit());
        setSportCustomersLimit(details.getSportCustomersLimit());
        setUsersLimit(details.getUsersLimit());
        setCustomerGroupsLimit(details.getCustomerGroupsLimit());
        setPokladnaLimit(details.getPokladnaLimit());
        setSkladLimit(details.getSkladLimit());
        setOvladaniQuido(details.getOvladaniQuido());
        setModules(details.getModules());
        setMaxClients(details.getMaxClients());
        setHash(details.getHash());
        setGeneratedDate(details.getGeneratedDate());
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(LicenseDetails details) throws CreateException {
        if (details.getId() == null) details.setId(LicenseUtil.generateGUID(this));
        setId(details.getId());
        setCreatedDate(new Date());
        setDetails(details);
        return details.getId();
    }
    public void ejbPostCreate(LicenseDetails details) throws CreateException {
    }
    
}
