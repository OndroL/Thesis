package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

/**
 * EJB 2.0 OAuth2ClientSetting Enterprise Bean. Entita uchovava parametre
 * pre autentizaciu a autorizaciu aplikacii tretich stran.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="OAuth2ClientSetting"
 *      local-jndi-name="ejb/user/LocalOAuth2ClientSetting"
 *      display-name="OAuth2ClientSettingEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="OAuth2ClientSetting"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OAuth2ClientSetting o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.persistence
 *      table-name="oauth2_client_setting"
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
public abstract class OAuth2ClientSettingBean extends BaseEntityBean implements EntityBean {
    
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
     * @jboss.column-name
     *    name="client_id"
     */
    public abstract String getClientId();
    public abstract void setClientId(String clientId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="client_secret"
     */
    public abstract String getClientSecret();
    public abstract void setClientSecret(String clientSecret);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getScopes();
    public abstract void setScopes(String scopes);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="resource_ids"
     */
    public abstract String getResourceIds();
    public abstract void setResourceIds(String resourceIds);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="authorized_grant_types"
     */
    public abstract String getAuthorizedGrantTypes();
    public abstract void setAuthorizedGrantTypes(String authorizedGrantTypes);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="registered_redirect_uris"
     */
    public abstract String getRegisteredRedirectUris();
    public abstract void setRegisteredRedirectUris(String registeredRedirectUris);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="auto_approve_scopes"
     */
    public abstract String getAutoApproveScopes();
    public abstract void setAutoApproveScopes(String autoApproveScopes);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="access_token_validity_seconds"
     */
    public abstract Integer getAccessTokenValiditySeconds();
    public abstract void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name
     *    name="refresh_token_validity_seconds"
     */
    public abstract Integer getRefreshTokenValiditySeconds();
    public abstract void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public OAuth2ClientSettingDetails getDetails() {
        OAuth2ClientSettingDetails details = new OAuth2ClientSettingDetails();
        details.setAccessTokenValiditySeconds(getAccessTokenValiditySeconds());
        details.setAuthorizedGrantTypes(getAuthorizedGrantTypes());
        details.setAutoApproveScopes(getAutoApproveScopes());
        details.setClientId(getClientId());
        details.setClientSecret(getClientSecret());
        details.setId(getId());
        details.setRefreshTokenValiditySeconds(getRefreshTokenValiditySeconds());
        details.setRegisteredRedirectUris(getRegisteredRedirectUris());
        details.setResourceIds(getResourceIds());
        details.setScopes(getScopes());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(OAuth2ClientSettingDetails details) {
        setAccessTokenValiditySeconds(details.getAccessTokenValiditySeconds());
        setAuthorizedGrantTypes(details.getAutoApproveScopes());
        setAutoApproveScopes(details.getAutoApproveScopes());
        setClientId(details.getClientId());
        setClientSecret(details.getClientSecret());
        setRefreshTokenValiditySeconds(details.getRefreshTokenValiditySeconds());
        setRegisteredRedirectUris(details.getRegisteredRedirectUris());
        setResourceIds(details.getResourceIds());
        setScopes(details.getScopes());
    }

    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(OAuth2ClientSettingDetails details) throws CreateException {
        if (details.getId() == null) details.setId(OAuth2ClientSettingUtil.generateGUID(this));
        setId(details.getId());
        setDetails(details);
        return details.getId();
    }
    public void ejbPostCreate(OAuth2ClientSettingDetails uzivatel) throws CreateException {
    }

}
