/**
 * Clubspire, (c) Inspire CZ 2004-2024
 *
 * TokenConfirmationBean.java
 * Created on: 21. 6. 2024
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.token;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * TokenConfirmation Enterprise Bean. Entita reprezentuje predmet, jenz slouzi k identifikaci zakaznika.
 * Jedna se napriklad o cipovou nebo magnetickou kartu, carovy kod nebo klic. 
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="TokenConfirmation"
 *      local-jndi-name="ejb/token/LocalTokenConfirmation"
 *      display-name="TokenConfirmationEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="TokenConfirmation"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM TokenConfirmation o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDate(java.util.Date from, java.util.Date to)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDate(java.util.Date from, java.util.Date to)"
 *          where="cas >= {0} AND cas <= {1}"
 *          order="cas DESC"
 * @ejb.persistence
 *      table-name="token_confirmation"
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
public abstract class TokenConfirmationBean extends BaseEntityBean implements EntityBean {

    private static final Logger LOGGER = Logger.getLogger(TokenConfirmationBean.class);
    
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
    
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
    public abstract Date getCas();
    public abstract void setCas(Date cas);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getZakaznikId();
    public abstract void setZakaznikId(String zakaznikId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getUzivatelId();
    public abstract void setUzivatelId(String uzivatelId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getTokenId();
    public abstract void setTokenId(String tokenId);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract boolean getConfirmation();
    public abstract void setConfirmation(boolean confirmation);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public TokenConfirmationDetails getDetails() {
        return new TokenConfirmationDetails(getId(), getCas(), getZakaznikId(), getUzivatelId(), getTokenId(), getConfirmation());
    }
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * @ejb.create-method
     */
    public String ejbCreate(TokenConfirmationDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(TokenConfirmationUtil.generateGUID(this));
        }
        
        setId(details.getId());
        setCas(details.getCas());
        setZakaznikId(details.getZakaznikId());
        setUzivatelId(details.getUzivatelId());
        setTokenId(details.getTokenId());
        setConfirmation(details.getConfirmation());
        return details.getId();
    }
    
    public void ejbPostCreate(TokenConfirmationDetails details) throws CreateException {
    }

}