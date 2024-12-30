/**
 * Clubspire, (c) Inspire CZ 2004-2017
 *
 * PushKeyBean.java
 * Created on: Aug 11, 2017
 * Author: Iveta Jurcikova
 *
 */
package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * PushKeyBean. Entita reprezentuje kluc potrebny k vykonaniu push notifikacie na strane mobilnej aplikacie.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="PushKey"
 *      local-jndi-name="ejb/user/LocalPushKey"
 *      display-name="PushKeyEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="PushKey"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushKey o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushKey o"
 *      result-type-mapping="Local"
 *      signature="LocalPushKey findByKey(java.lang.String key)"
 *      @jboss.declared-sql
 *          signature="LocalPushKey findByKey(java.lang.String key)"
 *          where="(key={0} OR key=CONCAT('android_', {0}) OR key=CONCAT('ios_', {0})) AND uzivatel_id IS NOT NULL"
 * @ejb.persistence
 *      table-name="push_key"
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
 *
 * @version 1.0
 * @author <a href="mailto:iveta.jurcikova@inspire.cz">Iveta Jurcikova</a>
 */
public abstract class PushKeyBean extends BaseEntityBean implements EntityBean {

    private static final Logger logger = Logger.getLogger(PushKeyBean.class);
    
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
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getKey();
    public abstract void setKey(String key);
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="uzivatel-push_key"
     *    role-name="push_key-is-assigned-to-uzivatel"
     *    cascade-delete="true"
     * @jboss.relation
     *    related-pk-field="login"
     *    fk-column="uzivatel_id"
     * @ejb.value-object
     *    match="base"
     *    aggregate="cz.inspire.enterprise.module.user.UzivatelDetails"
     *    aggregate-name="Uzivatel"
     */
     public abstract cz.inspire.enterprise.module.user.LocalUzivatel getUzivatel();
     public abstract void setUzivatel(cz.inspire.enterprise.module.user.LocalUzivatel uzivatel);
     
     /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.value-object
     *    match="*"
     */
    public java.lang.String getUzivatelId() {
        LocalUzivatel uzivatel = getUzivatel();
        if (uzivatel == null) {
            return null;
        } else {
            return getUzivatel().getLogin();
        }
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setUzivatelId(String id) throws NamingException, FinderException {
        if (id == null) {
            setUzivatel(null);
        } else {
            LocalUzivatel uzivatel = UzivatelUtil.getLocalHome().findByPrimaryKey(id);
            setUzivatel(uzivatel);
        }
    }
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PushKeyDetails getDetails() {
        PushKeyDetails details = new PushKeyDetails();
        details.setId(getId());
        details.setKey(getKey());
        //todo hadze vynimku
   //     details.setUzivatelId(getUzivatelId());
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(PushKeyDetails details) {
        setKey(details.getKey());
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(PushKeyDetails pushKey) throws CreateException {
        if (pushKey.getId() == null) {
            pushKey.setId(PushKeyUtil.generateGUID(this));
        }
        
        setId(pushKey.getId());
        setKey(pushKey.getKey());
        return pushKey.getId();
    }

    public void ejbPostCreate(PushKeyDetails pushKey) throws CreateException {
    }
}
