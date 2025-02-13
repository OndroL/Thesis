/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * ActivityBean.java
 * Created on: 6.2.2013
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Activity Enterprise Bean. Entita reprezentuje typ aktivity.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Activity"
 *      local-jndi-name="ejb/sport/LocalActivity"
 *      display-name="ActivityEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="Activity"
 *      match="*"
 * 
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Activity o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="index ASC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Activity o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count)"
 *          other="LIMIT {1} OFFSET {0}"
 *          order="index ASC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Activity o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllByInstructor(java.lang.String instructorId, int offset, int count)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllByInstructor(java.lang.String instructorId, int offset, int count)"
 *          from=", instructor_activity AS ia"
 *          where="ia.instructor_id = {0} AND ia.activity_id = id"
 *          other="LIMIT {2} OFFSET {1}"
 *          order="index ASC"
 * 
 * @ejb.persistence
 *      table-name="activity"
 *      @jboss.persistence
 *          create-table="true"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.load-group
 *   name="base"
 * @jboss.container-configuration
 *      name="Standard CMP 2.x EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class ActivityBean implements EntityBean {
    
    private static Logger logger = Logger.getLogger(ActivityBean.class);
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
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getName();
    public abstract void setName(String name);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getDescription();
    public abstract void setDescription(String description);
    
    /**
     * @ejb.interface-method view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object match="*"
     * @jboss.load-group name="base"
     */
    public abstract int getIndex();
    public abstract void setIndex(int index);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getIconId();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setIconId(String iconId);
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="activity-sport"   
     *      role-name="activity-has-sports"
     *      cascade-delete=false
     */
    public abstract Collection<LocalSport> getSports();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setSports(Collection<LocalSport> sports);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="activity-instructor"
     *      role-name="activity-has-instructors"
     * @jboss.relation
     *      fk-column="instructor_id"
     *      related-pk-field="id"
     *      fk-constraint="true"
     * @jboss.relation-table
     *      table-name="instructor_activity"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.util.Set<InstructorDetails>"
     *    aggregate-name="Instructors"
     */
    public abstract Collection<LocalInstructor> getInstructors();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setInstructors(Collection<LocalInstructor> instructors);
        
    // Entity methods -----------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public ActivityDetails getDetails() {
        ActivityDetails details = new ActivityDetails();
        details.setId(getId());
        details.setName(getName());
        details.setDescription(getDescription());
        details.setIndex(getIndex());
        details.setIconId(getIconId());
        
        Iterator<LocalInstructor> iter = getInstructors().iterator();
        Set<InstructorDetails> instructorSet = new HashSet<InstructorDetails>();
        while (iter.hasNext()) {
            LocalInstructor instructor = iter.next();
            InstructorDetails det = new InstructorDetails();
            det.setId(instructor.getId());
            det.setFirstName(instructor.getFirstName());
            det.setLastName(instructor.getLastName());
            det.setColor(instructor.getColor());
            instructorSet.add(det);
        }
        details.setInstructors(instructorSet);
        
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(ActivityDetails activity) {
        setName(activity.getName());
        setDescription(activity.getDescription());
        setIndex(activity.getIndex());
        setIconId(activity.getIconId());
        
//        instruktori
        Set<String> oldInstructorIds = new HashSet<String>();
        for (LocalInstructor instr : getInstructors()) {
            oldInstructorIds.add(instr.getId());
        }
        getInstructors().clear();
        if (activity.getInstructors() != null) {
            try {
                LocalInstructorHome instructorHome = InstructorUtil.getLocalHome();
                Iterator<InstructorDetails> iter = activity.getInstructors().iterator();
                
                while(iter.hasNext()) {
                    InstructorDetails instructorDetails = iter.next();
                    LocalInstructor instructor = instructorHome.findByPrimaryKey(instructorDetails.getId());
                    getInstructors().add(instructor);
                }
            } catch (Exception ex) {
                logger.error("Nepodarilo se nastavit instruktory!", ex);
            }
        }
        updateSportInstructor(activity, oldInstructorIds);
    }
    
    private void updateSportInstructor(ActivityDetails activity, Set<String> oldInstructorIds) {
        Set<String> newInstructorIds = new HashSet<String>();
        if (activity.getInstructors() != null) {
            for (InstructorDetails instr : activity.getInstructors()) {
                newInstructorIds.add(instr.getId());
            }            
        }
        //get instructors to delete
        oldInstructorIds.removeAll(newInstructorIds);
        //set all sport-instructor entities of this activity 
        //where instructor id is in deleted instructors as deleted
        for (LocalSport sport : getSports()) {
            for (LocalSportInstructor sportInstructor : (Collection<LocalSportInstructor>) sport.getSportInstructors()) {
                if (sportInstructor.getInstructor() != null
                        && oldInstructorIds.contains(sportInstructor.getInstructor().getId())) {
                    sportInstructor.setDeleted(true);                    
                }
            }
            //add NONE instructor if neccessary
            sport.checkSportWithoutInstructor();
        }
    }
    
    /**
     * @ejb.create-method
     */
    public String ejbCreate(ActivityDetails activity) throws CreateException {
        String id = activity.getId();
        if (id == null) {
            id = ActivityUtil.generateGUID(this);
        }
        setId(id);
        setName(activity.getName());
        setDescription(activity.getDescription());
        setIndex(activity.getIndex());
        setIconId(activity.getIconId());
        
        return id;
    }
    
    public void ejbPostCreate(ActivityDetails activity) throws CreateException {
        Set<InstructorDetails> instructors = activity.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            try {
                LocalInstructorHome instructorHome = InstructorUtil.getLocalHome();

                Collection<LocalInstructor> instructorCol = new HashSet<LocalInstructor>();
                for (InstructorDetails instructorDetails : instructors) {
                    LocalInstructor instructor = instructorHome.findByPrimaryKey(instructorDetails.getId());
                    instructorCol.add(instructor);
                }
                setInstructors(instructorCol);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                throw new CreateException(ex.getMessage());
            }
        }
    }
    
    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }
     
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
     * query="SELECT COUNT(act.id) FROM Activity act"
     */
    public abstract Long ejbSelectCountActivities() throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountActivities() throws FinderException {
        return ejbSelectCountActivities();
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(act.id) FROM Activity act, IN (act.instructors) AS instructor WHERE instructor.id = ?1"
     */
    public abstract Long ejbSelectCountActivitiesByInstructor(String instructorId) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountActivitiesByInstructor(String instructorId) throws FinderException {
        return ejbSelectCountActivitiesByInstructor(instructorId);
    }
}
