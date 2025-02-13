/**
 * OvladacObjektuBean.java
 * Created on 26. září 2005, 12:23
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import cz.inspire.enterprise.module.sport.OvladacObjektuBaseUtil;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 OvladacObjektu Enterprise Bean. Entita reprezentuje nastaveni ovladace objektu.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="OvladacObjektu"
 *      local-jndi-name="ejb/sport/LocalOvladacObjektu"
 *      display-name="OvladacObjektuEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="OvladacObjektu"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OvladacObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findWithOvladacObjektu(java.lang.String idOvladace)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findWithOvladacObjektu(java.lang.String idOvladace)"
 *          where="(idOvladace = {0})"
 *          order="id"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM OvladacObjektu o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByObjekt(java.lang.String objektId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByObjekt(java.lang.String idObjektu)"
 *          where="(objektid = {0})"
 *          order="id"
 * @ejb.persistence
 *      table-name="ovladac_objektu"
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
public abstract class OvladacObjektuBean extends BaseEntityBean implements EntityBean {
    
    private final Logger logger = Logger.getLogger(OvladacObjektuBean.class);

    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getIdOvladace();
    public abstract void setIdOvladace(String idOvladace);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="persi"
     */
    public abstract String getCislaZapojeni();
    public abstract void setCislaZapojeni(String CisloZapojeni);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract Boolean getManual();
    public abstract void setManual(Boolean Manual);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract Boolean getAutomat();
    public abstract void setAutomat(Boolean Automat);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract int getDelkaSepnutiPoKonci();
    public abstract void setDelkaSepnutiPoKonci(int delkaSepnutiPoKonci);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract int getZapnutiPredZacatkem();
    public abstract void setZapnutiPredZacatkem(int zapnutiPredZacatkem);
    
    /**
     * @ejb.value-object match="*"
     */
    public abstract List<Integer> getCislaZapojeniList();
    public abstract void setCislaZapojeniList(List<Integer> cislaZapojeni);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     */
    public abstract String getObjektId();
    public abstract void setObjektId(String objektId);
    
    // Entity relations ---------------------------------------------------------------------------
    
    // Business methods ----------------------------------------------------------------------------
   
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(OvladacObjektuDetails ovladacObjektu) {
        if (ovladacObjektu == null) {
            return;
        }
        
        setIdOvladace(ovladacObjektu.getIdOvladace());
        setCislaZapojeni(OvladacObjektuBaseUtil.encodeNumbersToString(ovladacObjektu.getCislaZapojeniList()));
        setAutomat(ovladacObjektu.getAutomat());
        setManual(ovladacObjektu.getManual());
        setDelkaSepnutiPoKonci(ovladacObjektu.getDelkaSepnutiPoKonci());
        setZapnutiPredZacatkem(ovladacObjektu.getZapnutiPredZacatkem());
        setObjektId(getObjektId());
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public OvladacObjektuDetails getDetails() {
        OvladacObjektuDetails details = new OvladacObjektuDetails(getId(), getIdOvladace(), getManual(), getAutomat(),
                getDelkaSepnutiPoKonci(), getZapnutiPredZacatkem(),
                OvladacObjektuBaseUtil.decodeNumbersFromString(getCislaZapojeni()), getObjektId());
        return details;
    }
    
    // Entity methods ------------------------------------------------------------------------------
    
    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(OvladacObjektuDetails ovladacObjektu) throws CreateException {
        String id = ovladacObjektu.getId();
        if (id == null) {
            id = OvladacObjektuUtil.generateGUID(this);
        }
        
        setId(id);
        setIdOvladace(ovladacObjektu.getIdOvladace());
        setCislaZapojeni(OvladacObjektuBaseUtil.encodeNumbersToString(ovladacObjektu.getCislaZapojeniList()));
        setAutomat(ovladacObjektu.getAutomat());
        setManual(ovladacObjektu.getManual());
        setDelkaSepnutiPoKonci(ovladacObjektu.getDelkaSepnutiPoKonci());
        setZapnutiPredZacatkem(ovladacObjektu.getZapnutiPredZacatkem());
        setObjektId(ovladacObjektu.getObjektId());
        
        return id;
    }
    
    public void ejbPostCreate(OvladacObjektuDetails ovladacObjektu) throws CreateException {
    }
    
}
