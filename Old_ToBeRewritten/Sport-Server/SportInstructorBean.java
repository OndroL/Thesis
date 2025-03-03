/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * SportInstructorBean.java
 * Created on: 7.2.2013
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 SportInstructor Enterprise Bean. Entita reprezentuje konkretni cinnost (napr. sportovni aktivitu s trenerem).
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="SportInstructor"
 *      local-jndi-name="ejb/sport/LocalSportInstructor"
 *      display-name="SportInstructorEJB"
 *      view-type="local"
 *      primkey-field="id"
 * @ejb.value-object
 *      name="SportInstructor"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportInstructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findBySport(java.lang.String sportId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findBySport(java.lang.String sportId)"
 *          where="sport_id = {0} AND deleted = false"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportInstructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByInstructor(java.lang.String instructorId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByInstructor(java.lang.String instructorId)"
 *          where="instructor_id = {0} AND deleted = false"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportInstructor o"
 *      result-type-mapping="Local"
 *      signature="LocalSportInstructor findBySportAndInstructor(java.lang.String sportId, java.lang.String instructorId)"
 *      @jboss.declared-sql
 *          signature="LocalSportInstructor findBySportAndInstructor(java.lang.String sportId, java.lang.String instructorId)"
 *          where="sport_id = {0} AND instructor_id = {1} AND deleted = false"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportInstructor o"
 *      result-type-mapping="Local"
 *      signature="LocalSportInstructor findBySportWithoutInstructor(java.lang.String sportId)"
 *      @jboss.declared-sql
 *          signature="LocalSportInstructor findBySportWithoutInstructor(java.lang.String sportId)"
 *          where="sport_id = {0} AND instructor_id IS NULL AND deleted = false"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM SportInstructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findByActivity(java.lang.String activityId)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findByActivity(java.lang.String activityId)"
 *          where="activity_id = {0}"
 * @ejb.persistence
 *      table-name="sport_instructor"
 *      @jboss.persistence
 *          create-table="true"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.container-configuration
 *      name="Optimistic CMP EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class SportInstructorBean implements EntityBean {
    
    private static final Logger logger = Logger.getLogger(SportInstructorBean.class);
    private EntityContext entityContext = null;
    
    // Entity fields -------------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.pk-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract String getId();
    public abstract void setId(String id);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract String getActivityId();
    public abstract void setActivityId(String activityId);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract String getOldSportId();
    public abstract void setOldSportId(String oldSportId);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     */
    public abstract boolean getDeleted();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setDeleted(boolean deleted);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="sport-sport_instructor"
     *      role-name="sport_instructor-is-sport"
     *      cascade-delete="yes"
     * @jboss.relation
     *      related-pk-field="id"
     *      fk-column="sport_id"
     *      fk-constraint="true"
     */
    public abstract LocalSport getSport();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setSport(LocalSport sportLocal);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="instructor-sport_instructor"
     *      role-name="sport_instructor-has-instructor"
     *      cascade-delete="yes"
     * @jboss.relation
     *      related-pk-field="id"
     *      fk-column="instructor_id"
     *      fk-constraint="true"
     * @ejb.value-object
     *    match="*"
     *    aggregate="String"
     *    aggregate-name="InstructorId"
     */
    public abstract LocalInstructor getInstructor();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setInstructor(LocalInstructor instructorLocal);
    
    // Entity methods -----------------------------------------------------------------------------
    
    /**
     * @ejb.create-method
     */
    public String ejbCreate(SportInstructorDetails details, LocalSport sport) throws CreateException {
        String id = details.getId();
        if (id == null) {
            id = SportInstructorUtil.generateGUID(this);
        }
        setId(id);
        setDeleted(details.getDeleted());
        setOldSportId(details.getOldSportId());
        return id;
    }
    
    public void ejbPostCreate(SportInstructorDetails details, LocalSport sport) throws CreateException {
        String instructorId = details.getInstructorId();
        if (instructorId != null) {
            try {
                LocalInstructor instructor = InstructorUtil.getLocalHome().findByPrimaryKey(instructorId);
                setInstructor(instructor);
            } catch (Exception ex) {
                throw new CreateException("Nepodarilo se priradit instruktora!");
            }
        }
        setSport(sport);
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public String getSportId() {
        LocalSport sport = getSport();
        return sport == null ? null : sport.getId();
    }
    
    /**
     * Gets the EntityContext. To be used by classes extending this.
     * @return the EntityContext of the EJB
     */
    protected EntityContext getEntityContext() {
        return entityContext;
    }
    
    /** Required to implement EntityBean. Sets the EntityContext. */
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }
    /** Required to implement EntityBean. Sets the EntityContext to null. */
    public void unsetEntityContext() throws EJBException {
        entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
        
    }

    public void ejbActivate() throws EJBException, RemoteException {
        
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        
    }

    public void ejbLoad() throws EJBException, RemoteException {
        
    }

    public void ejbStore() throws EJBException, RemoteException {
        
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(sportInstr.id) FROM SportInstructor sportInstr WHERE sportInstr.sport.id = ?1 AND sportInstr.deleted = FALSE"
     */
    public abstract Long ejbSelectCountSportInstructors(String sportId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountSportInstructors(String sportId) throws FinderException {
        return ejbSelectCountSportInstructors(sportId);
    }
}
