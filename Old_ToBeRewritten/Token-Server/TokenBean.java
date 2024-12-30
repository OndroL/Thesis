package cz.inspire.enterprise.module.token;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Token Enterprise Bean. Entita reprezentuje predmet, jenz slouzi k identifikaci zakaznika.
 * Jedna se napriklad o cipovou nebo magnetickou kartu, carovy kod nebo klic. 
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Token"
 *      local-jndi-name="ejb/token/LocalToken"
 *      display-name="TokenEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Token"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Role o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Role o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          order="popis, id"
 *          other="LIMIT {1} OFFSET {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Role o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByTyp(java.lang.String typId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByTyp(java.lang.String typId, int offset, int count)"
 *          where="typ = {0}"
 *          order="popis, id"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Role o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByZakaznik(java.lang.String zakaznikId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByZakaznik(java.lang.String zakaznikId, int offset, int count)"
 *          from=",zakaznik_token"
 *          where="id = zakaznik_token.token AND zakaznik_token.casdo IS NULL AND zakaznik_token.zakaznik = {0}"
 *          order="popis, id"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Token o"
 *      result-type-mapping="Local"
 *      signature="LocalToken findByIdSuffix(java.lang.String suffix)"
 *      @jboss.declared-sql
 *          signature="LocalToken findByIdSuffix(java.lang.String suffix)"
 *          where="id LIKE {0}"
 * @ejb.persistence
 *      table-name="token"
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
public abstract class TokenBean extends BaseEntityBean implements EntityBean {
    
    private static final Logger logger = Logger.getLogger(TokenBean.class);
    
    protected Logger getLogger() {
        return logger;
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
    public abstract String getPopis();
    public abstract void setPopis(String id);
    
    // Entity relations ---------------------------------------------------------------------------
      
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="token-typ_tokenu"
     *    role-name="token-je-typu-typ_tokenu"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="id"
     *    fk-column="typ"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="TypTokenuId"
     */
     public abstract cz.inspire.enterprise.module.token.LocalTypTokenu getTypTokenu();
     public abstract void setTypTokenu(cz.inspire.enterprise.module.token.LocalTypTokenu typTokenu);
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public TokenDetails getDetails() {
        TokenDetails details =  new TokenDetails(getId(), getPopis());
        LocalTypTokenu typ = getTypTokenu();
        details.setTypTokenuId(typ == null ? null : typ.getId());
        return details;
    }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(TokenDetails details) {
        setPopis(details.getPopis());
        try {
            LocalTypTokenu typ = TypTokenuUtil.getLocalHome().findByPrimaryKey(
                    details.getTypTokenuId());
            setTypTokenu(typ);
        } catch (Exception ex) {
            logger.error("Nepodarilo sa updatovat typ Tokenu!", ex);
        }
    }
        
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(TokenDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(TokenUtil.generateGUID(this));
        }
        
        setId(details.getId());
        setPopis(details.getPopis());
        return details.getId();
    }
    
    public void ejbPostCreate(TokenDetails details) throws CreateException {
        try {
            LocalTypTokenu typ = TypTokenuUtil.getLocalHome().findByPrimaryKey(
                details.getTypTokenuId());
            setTypTokenu(typ);
        } catch (Exception e) {
            throw new CreateException(e.toString());
        }
    }
    
}
