/**
* Clubspire, (c) Inspire CZ 2004-2013
*
* MenaBean.java
* Created on: 1.4.2014
* Author: Tomáš Kramec
*
*/
package cz.inspire.enterprise.module.common;
 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Mena Enterprise Bean. Entita reprezentuje menu pro ceny, objednavky, pokladne...
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Mena"
 *      local-jndi-name="ejb/bs/LocalMena"
 *      display-name="MenaEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="Mena"
 *      match="ent"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Mena o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByCode(java.lang.String code)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByCode(java.lang.String code)"
 *          where="kod = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Mena o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByCodeNum(int codeNum)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByCodeNum(int codeNum)"
 *          where="kodnum = {0}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Mena o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 * @ejb.persistence
 *      table-name="mena"
 *      @jboss.persistence
 *          create-table="true"
 *          data-source="jdbc/BookingSystemDB"
 * @jboss.load-group
 *   name="base"
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
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public abstract class MenaBean extends BaseEntityBean implements EntityBean {

    private static Logger logger = Logger.getLogger(BaseEntityBean.class);
    @Override
    protected Logger getLogger() {
        return logger;
    }
    private static final String VYCETKA_DELIM = ";";    
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="ent"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getKod();
    public abstract void setKod(String kod);   
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getKodNum();
    public abstract void setKodNum(int kod);   
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getZaokrouhleniHotovost();
    public abstract void setZaokrouhleniHotovost(int zaokrouhleni);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="ent"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getZaokrouhleniKarta();
    public abstract void setZaokrouhleniKarta(int zaokrouhleni);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="def"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getVycetka();
    public abstract void setVycetka(String vycetka);
    
    /**
     * @ejb.value-object
     *    match="ent"
     *    aggregate="java.util.List<java.math.BigDecimal>"
     *    aggregate-name="VycetkaList"
     */
    public abstract Collection<BigDecimal> getVycetkaList();
    
    private List<BigDecimal> parseVycetkaList() {
        List<BigDecimal> vycetka = new ArrayList<BigDecimal>();
        String vycetkaDef = getVycetka();
        if (vycetkaDef != null && !vycetkaDef.isEmpty()) {
            String[] vycetkaSplit = vycetkaDef.split(VYCETKA_DELIM);
            for (String v : vycetkaSplit) {
                try {
                    vycetka.add(new BigDecimal(v.trim()));
                } catch (NumberFormatException e) {
                    logger.warn("Cannot parse vycetka: " + v);
                }
            }
        }
        return vycetka;
    }
    
    // Business methods ----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public MenaDetails getDetails() {
        MenaDetails mena = new MenaDetails();
        mena.setId(getId());
        mena.setKod(getKod());
        mena.setKodNum(getKodNum());
        mena.setVycetkaList(parseVycetkaList());
        mena.setZaokrouhleniHotovost(getZaokrouhleniHotovost());
        mena.setZaokrouhleniKarta(getZaokrouhleniKarta());
        return mena;
    }
        
    // Entity method ------------------------------------------------------------------------------
    
//    /**
//     * EJB create method.
//     *
//     * @return the primary key of the new instance
//     * @ejb.create-method
//     */
//    public String ejbCreate(MenaDetails mena) throws CreateException {
//    }
//    public void ejbPostCreate(MenaDetails osoba) throws CreateException {
//    }
     
}