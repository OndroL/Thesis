/**
 * Clubspire, (c) Inspire CZ 2004-2006
 *
 * SequenceBean.java
 * Created on 20.1.2004
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospisil</a>
 */
package cz.inspire.enterprise.module.sequence;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.text.ParseException;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Sequence Enterprise Bean.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Sequence"
 *      local-jndi-name="ejb/sequence/LocalSequence"
 *      display-name="SequenceEJB"
 *      view-type="local"
 *      primkey-field="name"
 * @ejb.value-object
 *      name="Sequence"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sequence o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="name"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sequence o"
 *      result-type-mapping="Local"
 *      signature="LocalSequence findBySkladType(java.lang.String skladId, int type)"
 *      @jboss.declared-sql
 *          signature="LocalSequence findBySkladType(java.lang.String skladId, int type)"
 *          from=",sklad_seq"
 *          where="sklad_seq.sklad = {0} AND sklad_seq.sekvence = seq.name AND seq.type={1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sequence o"
 *      result-type-mapping="Local"
 *      signature="LocalSequence findByPokladnaType(java.lang.String pokladnaId, int type)"
 *      @jboss.declared-sql
 *          signature="LocalSequence findByPokladnaType(java.lang.String pokladnaId, int type)"
 *          from=", pokladna_ucet_seq"
 *          where="pokladna_ucet_seq.pokladna = {0} AND pokladna_ucet_seq.sekvence = seq.name AND seq.type={1}"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Sequence o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByType(int type,int offset,int limit)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByType(int type,int offset,int limit)"
 *          where="type={0}"
 *          other="LIMIT {2} OFFSET {1}"
 * @ejb.persistence
 *      table-name="seq"
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
public abstract class SequenceBean extends BaseEntityBean implements EntityBean {
    
    private static final Logger logger = Logger.getLogger(SequenceBean.class);
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getName();
    public abstract void setName(String name);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getPattern();
    public abstract void setPattern(String pattern);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getMinValue();
    public abstract void setMinValue(int minValue);

    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract String getLast();
    public abstract void setLast(String last);
    
    /**
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract int getType();
    public abstract void setType(int type);

    
    // Entity relations ----------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     * @ejb.relation
     *    name="sequence-sequence"
     *    role-name="stornoseq-patri-k-seq"
     *    target-cascade-delete="true"
     *    target-ejb="Sequence"
     *    target-role-name="seq-ma-storno-seq"
     * @jboss.relation
     *    related-pk-field="name"
     *    fk-column="stornoseq"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.lang.String"
     *    aggregate-name="StornoSeqName"
     */
    public abstract LocalSequence getStornoSeq();
    public abstract void setStornoSeq(LocalSequence sequence);



    // Business methods ----------------------------------------------------------------------------

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
     public void setDetails(SequenceDetails details) {
         setPattern(details.getPattern());
         setMinValue(details.getMinValue());
         logger.debug("setDetails - setting last: " + details.getLast());
         setLast(details.getLast());
         setType(details.getType());
         if (details.getStornoSeqName() != null) {
             LocalSequenceHome seqHome = (LocalSequenceHome) entityContext.getEJBLocalHome();
             try {
                 LocalSequence seq = seqHome.findByPrimaryKey(details.getStornoSeqName());
                 setStornoSeq(seq);
             } catch (Exception ex) {
                 logger.error("Cannot find storno sequence");
             }
         }
     }

    /**
     * @ejb.interface-method
     *    view-type="local"
     */
     public SequenceDetails getDetails() {
         SequenceDetails details = new SequenceDetails();
         details.setName(getName());
         details.setPattern(getPattern());
         details.setMinValue(getMinValue());
         details.setLast(getLast());
         details.setType(getType());
         if (getStornoSeq() != null){
             details.setStornoSeqName(getStornoSeq().getName());
         }
         
         return details;
     }
     
     private String preInit(Date time) {
         SequencePattern pattern = new SequencePattern(getPattern());
         int serial = getMinValue();
         pattern.setSerial(serial);
         pattern.setTime(time);
         return pattern.format();
     }
     
    /**
     * Initializes the sequence.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public void init(Date time) {
         String last = preInit(time);
         logger.debug("Init - setting last: " + last);
         setLast(last);
     }
     
    /**
     * Returns next value of the sequence, but do not increment.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public String preview() {
         Date time = new Date();
         
         if (getLast() == null) {
             return preInit(time);
         }
         
         SequencePattern pattern = new SequencePattern(getPattern());
         pattern.setTime(time);
         try {
            pattern.parse(getLast());
         } catch (ParseException pe) {
             return preInit(time);
         }
         int year = pattern.getYear();
         int month = pattern.getMonth();
         int day = pattern.getDay();         
         pattern.setTime(time);
         int serial = pattern.getSerial();
         serial++;
         if ((pattern.getYear() != year) || (pattern.getMonth() != month) || (pattern.getDay() != day)) {
             serial = getMinValue();
         }
         pattern.setSerial(serial);
         return pattern.format();
     }
     
    /**
     * Returns next valuo of the sequence.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public String next(Date time) {
         if (getLast() == null) {
             init(time);
             return getLast();
         }
         SequencePattern pattern = new SequencePattern(getPattern());
         pattern.setTime(time);
         try {
            pattern.parse(getLast());
         } catch (ParseException pe) {
             init(time);
             return getLast();
         }
         int year = pattern.getYear();
         int month = pattern.getMonth();
         int day = pattern.getDay();         
         pattern.setTime(time);
         int serial = pattern.getSerial();
         serial++;
         if ((pattern.getYear() != year) || (pattern.getMonth() != month) || (pattern.getDay() != day)) {
             serial = getMinValue();
         }
         pattern.setSerial(serial);
         String last = pattern.format();
         logger.debug("Setting last: " + last);
         setLast(last);
         return getLast();
     }
     
    /**
     * Returns next valuo of the sequence.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public void undo(Date time, String actual) {
         if (getLast() == null) {
             return ;
         }
         if (! getLast().equals(actual)) { // nerovnaj se, mezitim nekdo radu posunul, nic neprovedu             
             return ;
         }
         SequencePattern pattern = new SequencePattern(getPattern());
         pattern.setTime(time);
         try {
            pattern.parse(getLast());
         } catch (ParseException pe) {
             return;
         }
         int year = pattern.getYear();
         int month = pattern.getMonth();
         int day = pattern.getDay();         
         pattern.setTime(time);
         int serial = pattern.getSerial();
         serial--;
         if ((pattern.getYear() != year) || (pattern.getMonth() != month) || (pattern.getDay() != day)) {
             serial = getMinValue();
         }
         pattern.setSerial(serial);
         String last = pattern.format();
         logger.debug("Setting undo last: " + last);
         setLast(last);
     }

    /**
     * Returns next valuo of the sequence.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public String next() {
         return next(new Date());
     }
     
    /**
     * Returns next valuo of the sequence.
     *
     * @ejb.interface-method
     *    view-type="local"
     */
     public void undo(String actual) {
         undo(new Date(), actual);
     }     
     
    
    // Entity methods ------------------------------------------------------------------------------

    /**
     * EJB create method.
     *
     * @return the primary key of the new instance
     * @ejb.create-method
     */
    public String ejbCreate(SequenceDetails details) throws CreateException {
        if (details.getName() == null) {
            details.setName(SequenceUtil.generateGUID(this));
        }
        
        setName(details.getName());
        setPattern(details.getPattern());
        setMinValue(details.getMinValue());
        setLast(null);
        setType(details.getType());
        return details.getName();
    }
    
    public void ejbPostCreate(SequenceDetails details) throws CreateException {
        if (details.getStornoSeqName() != null) {
            LocalSequenceHome seqHome = (LocalSequenceHome) entityContext.getEJBLocalHome();
            try {
                LocalSequence seq = seqHome.findByPrimaryKey(details.getStornoSeqName());
                setStornoSeq(seq);
            } catch (Exception ex) {
                logger.error("Cannot find storno sequence");
            }
        }
    }
    
}