package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.exception.InvalidParameterException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;

import javax.naming.NamingException;

/**
 * EJB 2.0 ExternalClient Enterprise Bean. Entita reprezentuje aplikacie tretich stran,
 * ktore komunikuju s CS prostrednictvom dostupnych API (REST, ...)
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="ExternalClient"
 *      local-jndi-name="ejb/user/LocalExternalClient"
 *      display-name="ExternalClientEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="ExternalClient"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ExternalClient o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM ExternalClient o"
 *      result-type-mapping="Local"
 *      signature="LocalExternalClient findByOAuth2ClientId(java.lang.String clientId)"
 *      @jboss.declared-sql
 *          signature="LocalExternalClient findByOAuth2ClientId(java.lang.String clientId)"
 *          from=",oauth2_client_setting"
 *          where="oauth2_client_setting.client_id = {0} AND oauth2_setting_id = oauth2_client_setting.id"
 *          other="limit 1"
 * @ejb.persistence
 *      table-name="external_client"
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
public abstract class ExternalClientBean extends BaseEntityBean implements EntityBean {
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getName();
    public abstract void setName(String name);    
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="usergroup-external_client"
     *    role-name="external_client-is-in-usergroup"
     *    target-ejb="Skupina"
     *    target-role-name="usergroup-contains-external_client"
     *    target-multiple="false"
     *    cascade-delete="false"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="customer_group"
     * @ejb.value-object
     *    match="ent"
     *    aggregate="java.lang.String"
     *    aggregate-name="UserGroupId"
     */
    public abstract cz.inspire.enterprise.module.user.LocalSkupina getUserGroup();
    public abstract void setUserGroup(cz.inspire.enterprise.module.user.LocalSkupina usergroup);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="external_client-oauth2_client_setting"
     *    role-name="external_client-has-oauth2_setting"
     *    target-ejb="OAuth2ClientSetting"
     *    target-role-name="oauth2_setting-is-defined-for-external_client"
     *    target-multiple="false"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="oauth2_setting_id"
     @ejb.value-object
     *    match="*"
     *    aggregate="cz.inspire.enterprise.module.user.OAuth2ClientSettingDetails"
     *    aggregate-name="OAuth2ClientSetting"
     * @jboss.relation-read-ahead
     *  strategy="on-find"
     *  eager-load-group="base"
     */
    public abstract cz.inspire.enterprise.module.user.LocalOAuth2ClientSetting getOAuth2ClientSetting();
    public abstract void setOAuth2ClientSetting(cz.inspire.enterprise.module.user.LocalOAuth2ClientSetting oauth2ClientSetting);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ExternalClientDetails getDetails() {
        ExternalClientDetails details = new ExternalClientDetails();
        details.setId(getId());
        details.setName(getName());
        LocalOAuth2ClientSetting oAuth2ClientSetting = getOAuth2ClientSetting();
        if (oAuth2ClientSetting != null) {
            details.setOAuth2ClientSetting(oAuth2ClientSetting.getDetails());
        }
        LocalSkupina userGroup = getUserGroup();
        if (userGroup != null) {
            details.setUserGroupId(userGroup.getId());
        }
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(ExternalClientDetails details) throws InvalidParameterException, NamingException, CreateException, FinderException {
        setName(details.getName());
        LocalSkupina skupina = null;
        if (details.getUserGroupId() != null) {
            skupina = SkupinaUtil.getLocalHome().findByPrimaryKey(details.getUserGroupId());
        }
        setUserGroup(skupina);
        LocalOAuth2ClientSetting oAuth2ClientSetting = getOAuth2ClientSetting();
        if (oAuth2ClientSetting != null) {
            oAuth2ClientSetting.setDetails(details.getOAuth2ClientSetting());
        } else {
            oAuth2ClientSetting = OAuth2ClientSettingUtil
                    .getLocalHome().create(details.getOAuth2ClientSetting());
            setOAuth2ClientSetting(oAuth2ClientSetting);
        }
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(ExternalClientDetails details) throws CreateException {
        if (details.getId() == null) details.setId(ExternalClientUtil.generateGUID(this));
        setId(details.getId());
        setName(details.getName());
        return details.getId();
    }
    
    public void ejbPostCreate(ExternalClientDetails details) throws CreateException {
        if (details.getUserGroupId() != null) {
            try {
                LocalSkupina userGroup =
                        SkupinaUtil.getLocalHome().findByPrimaryKey(details.getUserGroupId());
                setUserGroup(userGroup);
            } catch (Exception e) {
                throw new CreateException("Could not set usergroup: " + details.getUserGroupId());
            }
        }
        if (details.getOAuth2ClientSetting() != null) {
            try {
                LocalOAuth2ClientSetting oauth2Setting = OAuth2ClientSettingUtil
                        .getLocalHome().create(details.getOAuth2ClientSetting());
                setOAuth2ClientSetting(oauth2Setting);
            } catch (Exception e) {
                throw new CreateException("Could not create OAuth2ClientSetting: " + details.getOAuth2ClientSetting());
            }
        }
    }
    
}
