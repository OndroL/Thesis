/**
 * Uzivatel-Server, (c) Inspire CZ 2004-2006
 *
 * UzivatelBean.java, verze 1.1
 * Vytvoreno: 20. 1. 2004
 * Autor: dominik
 */
package cz.inspire.enterprise.module.user;

import cz.inspire.enterprise.exception.InvalidParameterException;
import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Uzivatel Enterprise Bean. Entita reprezentuje uzivatele komunikujiciho s aplikaci.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Uzivatel"
 *      local-jndi-name="ejb/user/LocalUzivatel"
 *      display-name="UzivatelEJB"
 *      view-type="local"
 *      primkey-field="login"
 * @ejb.value-object
 *      name="Uzivatel"
 *      match="ent"
 * @ejb.value-object
 *      name="UzivatelFull"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBySkupina(java.lang.String skupinaId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBySkupina(java.lang.String skupinaId)"
 *          where="skupina = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByAktivni(java.lang.Boolean aktivni)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByAktivni(java.lang.Boolean aktivni)"
 *          where="aktivni = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatel findByAuthKey(java.lang.String authKey)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatel findByAuthKey(java.lang.String authKey)"
 *          where="auth_key = {0}"
 *          other="limit 1"
 *  * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="LocalUzivatel findByAktivacniToken(java.lang.String aktivacniToken)"
 *      @jboss.declared-sql
 *          signature="LocalUzivatel findByAktivacniToken(java.lang.String aktivacniToken)"
 *          where="aktivacnitoken = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllNotWeb()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllNotWeb()"
 *          where="NOT skupina = 'web'"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Uzivatel o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBySkupina(java.lang.String skupinaId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBySkupina(java.lang.String skupinaId, int offset, int count)"
 *          where="skupina = {0}"
 * 			other="LIMIT {2} OFFSET {1}"
 * @ejb.persistence
 *      table-name="uzivatel"
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
public abstract class UzivatelBean extends BaseEntityBean implements EntityBean {

    private static Logger logger = Logger.getLogger(UzivatelBean.class);

    // Entity fields -------------------------------------------------------------------------------
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method view-type="local"
     */
    public abstract String getLogin();

    public abstract void setLogin(String login);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getHeslo();

    public abstract void setHeslo(String heslo);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getJmeno();

    public abstract void setJmeno(String jmeno);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract boolean getAktivni();

    public abstract void setAktivni(boolean aktivni);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract String getAktivacniToken();

    public abstract void setAktivacniToken(String aktivacniToken);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.column-name name="auth_key"
     */
    public abstract String getAuthKey();

    public abstract void setAuthKey(String authKey);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="full"
     */
    public abstract java.util.Map getNastaveni();

    public abstract void setNastaveni(java.util.Map nastaveni);

    // Entity relations ---------------------------------------------------------------------------
    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="uzivatel-role" role-name="uzivatel-vystupuje-v-rolich" target-ejb="Role"
     * target-role-name="role-je-prirazena-uzivatelum" target-multiple="true"
     * @jboss.relation related-pk-field="id" fk-column="id"
     * @jboss.target-relation related-pk-field="login" fk-column="login"
     * @jboss.relation-table table-name="uzivatel_role" create-table="true"
     * @ejb.value-object match="ent" aggregate="java.util.Collection" aggregate-name="Role"
     */
    public abstract Collection getRole();

    public abstract void setRole(Collection role);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="skupina-uzivatel" role-name="uzivatel-je-clenem-skupiny"
     * @jboss.relation related-pk-field="id" fk-column="skupina"
     * @ejb.value-object match="ent" aggregate="java.lang.String" aggregate-name="SkupinaId"
     */
    public abstract cz.inspire.enterprise.module.user.LocalSkupina getSkupina();

    public abstract void setSkupina(cz.inspire.enterprise.module.user.LocalSkupina skupina);

    /**
     * CMR logins methods.
     *
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="uzivatel-uzivatel_session" role-name="uzivatelska-sezeni"
     */
    public abstract java.util.Collection getSessions();

    public abstract void setSessions(java.util.Collection sessions);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="uzivatel-push_key" role-name="uzivatel-has-push_key"
     * @ejb.value-object match="*" aggregate="java.util.List<PushKeyDetails>" aggregate-name="PushKeys"
     * @jboss.relation-read-ahead strategy="on-find" eager-load-group="*"
     */
    public abstract java.util.Collection<LocalPushKey> getPushKeys();
    public abstract void setPushKeys(java.util.Collection<LocalPushKey> pushKeys);

    // Business methods ----------------------------------------------------------------------------
    /**
     * @ejb.interface-method view-type="local"
     */
    public UzivatelDetails getDetails() {
        UzivatelDetails details = new UzivatelDetails();
        details.setLogin(getLogin());
        details.setHeslo(getHeslo());
        details.setJmeno(getJmeno());
        details.setAktivni(getAktivni());
        details.setAktivacniToken(getAktivacniToken());
        details.setAuthKey(getAuthKey());
        details.setPushKeys(getPushKeysDetails());

        return details;
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public void setDetails(UzivatelFullDetails details) throws InvalidParameterException {
        setHeslo(details.getHeslo());
        setJmeno(details.getJmeno());
        setAktivni(details.getAktivni());
        setAktivacniToken(details.getAktivacniToken());
        setNastaveni(details.getNastaveni());

        if (details.getSkupinaId() != null) {
            try {
                LocalSkupina skupina
                        = SkupinaUtil.getLocalHome().findByPrimaryKey(details.getSkupinaId());
                setSkupina(skupina);
            } catch (Exception e) {
                throw new InvalidParameterException("Could not set skupina.", e);
            }
        } else {
            setSkupina(null);
        }
        try {
            setPushKeysInternal(details.getPushKeys());
        } catch (Exception ex) {
            logger.error("Nepodarilo se nastavit push kluce", ex);
        }
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public UzivatelFullDetails getFullDetails() {
        UzivatelFullDetails details = new UzivatelFullDetails();
        details.setLogin(getLogin());
        details.setAuthKey(getAuthKey());
        details.setHeslo(getHeslo());
        details.setJmeno(getJmeno());
        details.setAktivni(getAktivni());
        details.setAktivacniToken(getAktivacniToken());
        details.setNastaveni(getNastaveni());
        
        if (getSkupina() != null) {
            details.setSkupinaId(getSkupina().getId());
        }
        
        Vector roles = new Vector();

        Iterator<LocalRole> it = getRole().iterator();
        
        while (it.hasNext()) {
            LocalRole role = it.next();
            roles.add(role.getId());
        }
        
        details.setRole(roles);
        details.setPushKeys(getPushKeysDetails());

        return details;
    }
    
    private List<PushKeyDetails> getPushKeysDetails() {
        Collection<LocalPushKey> pushKeys = getPushKeys();
        List<PushKeyDetails> keys = new ArrayList<PushKeyDetails>();

        if (pushKeys != null) {

            for (LocalPushKey pk : pushKeys) {
                keys.add(pk.getDetails());
            }
        }

        return keys;
    }

    // Entity methods ------------------------------------------------------------------------------
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(UzivatelFullDetails uzivatel) throws CreateException {
        setLogin(uzivatel.getLogin());
        setHeslo(uzivatel.getHeslo());
        setAktivni(uzivatel.getAktivni());
        setAktivacniToken(uzivatel.getAktivacniToken());
        String authKey = uzivatel.getAuthKey();
        if (authKey == null || authKey.isEmpty()) {
            authKey = UzivatelUtil.generateGUID(this);            
        }
        setAuthKey(authKey);
        setJmeno(uzivatel.getJmeno());
        setNastaveni(uzivatel.getNastaveni());
        return null;
    }

    public void ejbPostCreate(UzivatelFullDetails uzivatel) throws CreateException {
        try {
            if (uzivatel.getSkupinaId() != null) {
                try {
                    LocalSkupina skupina
                            = SkupinaUtil.getLocalHome().findByPrimaryKey(uzivatel.getSkupinaId());
                    setSkupina(skupina);
                } catch (Exception e) {
                    throw new CreateException("Could not set Skupina.");
                }
            } else {
                if (!SystemUser.UZIVATEL_SYSTEM.equals(uzivatel.getLogin())) {
                    throw new CreateException("Uzivatel skupina must be set.");
                }
            }
            setPushKeysInternal(uzivatel.getPushKeys());
        } catch (Exception ex) {
            entityContext.setRollbackOnly();
            throw new CreateException("Uzivatel entity could not be created.");
        }
    }

    private void setPushKeysInternal(List<PushKeyDetails> pushKeys)
            throws NamingException, CreateException, RemoveException {
        //create map of old push keys
        Map<String, LocalPushKey> oldKeys = new HashMap<String, LocalPushKey>();
        for (LocalPushKey keysLocal : getPushKeys()) {
            oldKeys.put(keysLocal.getId(), keysLocal);
        }

        List<LocalPushKey> keysFinal = new ArrayList<LocalPushKey>();
        LocalPushKeyHome pushKeyHome = PushKeyUtil.getLocalHome();

        if (pushKeys != null) {
            //update and create new keys
            LocalPushKey keyLocal = null;
            for (PushKeyDetails keyDet : pushKeys) {
                if (keyDet.getId() != null) {
                    //update existing number
                    keyLocal = oldKeys.remove(keyDet.getId());
                    if (keyLocal != null) {
                        keyLocal.setDetails(keyDet);
                    }
                } else {
                    try {
                        keyLocal = pushKeyHome.findByKey(keyDet.getKey());
                        keyLocal.setDetails(keyDet);
                    } catch (FinderException fe) {
                        keyLocal = pushKeyHome.create(keyDet);
                    }
                }

                if (keyLocal != null) {
                    keysFinal.add(keyLocal);
                }
            }
        }
        setPushKeys(keysFinal);

        //remove old keys that are not presented in pushKeys list
        for (String id : oldKeys.keySet()) {
            pushKeyHome.remove(id);
        }
    }
}
