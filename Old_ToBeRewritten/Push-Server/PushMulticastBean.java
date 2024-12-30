/**
 * Clubspire, (c) Inspire CZ 2004-2017
 *
 * PushMulticastBean.java
 * Created on: Sep 7, 2017
 * Author: Iveta Jurcikova
 *
 */
package cz.inspire.enterprise.module.push;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.ArrayList;
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
 * PushMulticastBean
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="PushMulticast"
 *      local-jndi-name="ejb/push/LocalPushMulticast"
 *      display-name="PushMulticastEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="PushMulticast"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushMulticast o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, java.lang.Boolean automatic, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, java.lang.Boolean automatic, int offset, int count)"
 *          order="date DESC"
 *          where="(date >= {0}) AND (date <= {1}) AND (sent = TRUE) AND ({2} IS NULL OR automatic = {2})"
 *          other="LIMIT {4} OFFSET {3}"
 *          strategy="on-find"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM PushMulticast o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByUzivatelDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, java.lang.Boolean automatic, java.lang.String uzivatelId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByUzivatelDateAutomatic(java.util.Date dateFrom, java.util.Date dateTo, java.lang.Boolean automatic, java.lang.String uzivatelId, int offset, int count)"
 *          from=", push_history ph"
 *          order="date DESC"
 *          where="push_multicast.id = ph.pushmulticast AND uzivatelid = {3} AND (date >= {0}) AND (date <= {1}) AND (sent = TRUE) AND ({2} IS NULL OR automatic = {2})"
 *          other="LIMIT {5} OFFSET {4}"
 *          strategy="on-find"
 * @ejb.persistence
 *      table-name="push_multicast"
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
public abstract class PushMulticastBean extends BaseEntityBean implements EntityBean {
    
    private static final Logger LOGGER = Logger.getLogger(PushMulticastBean.class);
    
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }   
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getId();
    public abstract void setId(java.lang.String id);    

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.util.Date getDate();
    public abstract void setDate(java.util.Date date); 

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getBody();
    public abstract void setBody(java.lang.String body);  

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getHistoryBody();
    public abstract void setHistoryBody(java.lang.String historyBody);  

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.String getTitle();
    public abstract void setTitle(java.lang.String title);
    
        /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.util.Collection getGroups();
    public abstract void setGroups(java.util.Collection groups);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     */
    public abstract java.lang.Boolean getAutomatic();
    public abstract void setAutomatic(java.lang.Boolean automatic);
    
        /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract java.lang.Boolean getSent();

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setSent(java.lang.Boolean isSent);
    
    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="push_mutlicast-push_history"
     *      role-name="push_multicast-has-push_history"
     *      target-ejb="PushHistory"
     *      target-role-name="push_history_of_push_multicast"
     *      target-multiple="no"
     *      target-cascade-delete="yes"
     * @ejb.value-object match="*" 
     *  aggregate="java.util.List<PushHistoryDetails>" 
     *  aggregate-name="PushHistory"
     * @jboss.target-relation fk-column="pushmulticast"
     *      fk-constraint="true"
     *      related-pk-field="id"
     */
    public abstract java.util.Collection<LocalPushHistory> getPushHistory();

    /**
     * @ejb.interface-method view-type="local"
     */
    public abstract void setPushHistory(java.util.Collection<LocalPushHistory> pushHistory);

    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PushMulticastDetails getDetailsWithoutHistory() {
        PushMulticastDetails details = new PushMulticastDetails();
        details.setId(getId());
        details.setDate(getDate());
        details.setGroups(getGroups());
        details.setTitle(getTitle());
        details.setBody(getBody());
        details.setHistoryBody(getHistoryBody());
        details.setSent(getSent());
        details.setAutomatic(getAutomatic());        
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public PushMulticastDetails getDetails() {
        PushMulticastDetails details = getDetailsWithoutHistory();       
        Collection pushHistory = getPushHistory();
        List<PushHistoryDetails> history = new ArrayList<PushHistoryDetails>();
        
        if (pushHistory != null) {
            for (Object obj : pushHistory) {
                LocalPushHistory ph = (LocalPushHistory) obj;
                history.add(ph.getDetails());
            }
        }
        
        details.setPushHistory(history);
        
        return details;
    }
    
     /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(PushMulticastDetails details) {        
        setDate(details.getDate());
        setGroups(details.getGroups());
        setTitle(details.getTitle());
        setBody(details.getBody());
        setHistoryBody(details.getHistoryBody());
        setSent(details.getSent());
        setAutomatic(details.getAutomatic());        
        
        try {
            setPushHistoryInternal(details.getPushHistory());
        } catch (Exception ex) {
            LOGGER.error("Nepodarilo se nastavit push historiu", ex);
        }
    }
    
    // Entity methods ------------------------------------------------------------------------------    
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     * @ejb.transaction
     *    type="RequiresNew"
     */
    public String ejbCreate(PushMulticastDetails details) throws CreateException {
        if (details.getId() == null) {
            details.setId(PushMulticastUtil.generateGUID(this));
        }
        
        setId(details.getId());
        setDate(details.getDate());
        setGroups(details.getGroups());
        setTitle(details.getTitle());
        setBody(details.getBody());
        setHistoryBody(details.getHistoryBody());
        setSent(details.getSent());
        setAutomatic(details.getAutomatic());

        return getId();
    }

    public void ejbPostCreate(PushMulticastDetails details) throws CreateException {
        try {
            setPushHistoryInternal(details.getPushHistory());
        } catch (Exception ex) {
            entityContext.setRollbackOnly();
            throw new CreateException("Push mulitcast entity could not be created.");
        }
    }  

    private void setPushHistoryInternal(List<PushHistoryDetails> pushHistory)
            throws NamingException, CreateException, RemoveException, FinderException {
        //create map of old push history
        Map<String, LocalPushHistory> oldHistory = new HashMap<String, LocalPushHistory>();

        for (LocalPushHistory historyLocal : getPushHistory()) {
            oldHistory.put(historyLocal.getId(), historyLocal);
        }

        List<LocalPushHistory> historyFinal = new ArrayList<LocalPushHistory>();
        LocalPushHistoryHome pushHistoryHome = PushHistoryUtil.getLocalHome();

        if (pushHistory != null) {
            //update and create new keys
            LocalPushHistory historyLocal = null;

            for (PushHistoryDetails historyDet : pushHistory) {

                if (historyDet.getId() != null) {
                    //update existing number
                    historyLocal = oldHistory.remove(historyDet.getId());

                    if (historyLocal != null) {
                        historyLocal.setDetails(historyDet);
                    }

                } else {
                    historyLocal = pushHistoryHome.create(historyDet);
                }

                if (historyLocal != null) {
                    historyFinal.add(historyLocal);
                }
            }
        }
        setPushHistory(historyFinal);

        //remove old keys that are not presented in pushKeys list
        for (String id : oldHistory.keySet()) {
            pushHistoryHome.remove(id);
        }
    }
    
}