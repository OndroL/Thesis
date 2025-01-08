/**
 * Clubspire, (c) Inspire CZ 2004-2013
 *
 * InstructorBean.java
 * Created on: 7.2.2013
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.sport.ejb;

import cz.inspire.enterprise.module.common.BaseEntityBean;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.*;
import org.apache.log4j.Logger;

/**
 * EJB 2.0 Instructor Enterprise Bean. Entita reprezentuje trenera.
 *
 * @ejb.bean
 *      type="CMP"
 *      cmp-version="2.x"
 *      name="Instructor"
 *      local-jndi-name="ejb/sport/LocalInstructor"
 *      display-name="InstructorEJB"
 *      view-type="local"
 *      primkey-field="id"
 *      reentrant="true"
 * @ejb.value-object
 *      name="Instructor"
 *      match="*"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Instructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll()"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll()"
 *          order="index ASC"
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Instructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAll(int offset, int count, boolean deleted)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAll(int offset, int count, boolean deleted)"
 *          where="deleted={2}"
 *          other="LIMIT {1} OFFSET {0}"
 *          order="index ASC"
 * 
 * @ejb.finder
 *      method-intf="LocalHome"
 *      query="SELECT OBJECT(o) FROM Instructor o"
 *      result-type-mapping="Local"
 *      signature="java.util.Collection findAllByActivity(java.lang.String activityId, int offset, int count, boolean deleted)"
 *      @jboss.declared-sql
 *          signature="java.util.Collection findAllByActivity(java.lang.String activityId, int offset, int count, boolean deleted)"
 *          from=", instructor_activity AS ia"
 *          where="deleted={3} AND ia.activity_id = {0} AND ia.instructor_id = id"
 *          other="LIMIT {2} OFFSET {1}"
 *          order="index ASC"
 * @ejb.persistence
 *      table-name="instructor"
 *      @jboss.persistence
 *          create-table="true"
 * @ejb.permission
 *      unchecked="true"
 * @jboss.load-group
 *   name="base"
 * @jboss.load-group
 *   name="lazydata"
 * @jboss.lazy-load-group
 *   name="lazydata"
 * @jboss.container-configuration
 *      name="Optimistic CMP EntityBean"
 * @jboss.method-attributes
 *      pattern="get*"
 *      read-only="true"
 * @ejb.util
 *      generate="physical"
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public abstract class InstructorBean extends BaseEntityBean implements EntityBean {

    private static Logger logger = Logger.getLogger(InstructorBean.class);
    private Set<SportDetails> sportSet;
    
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
    public abstract String getFirstName();
    public abstract void setFirstName(String firstName);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getLastName();
    public abstract void setLastName(String lastName);
    
        /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getIndex();
    
     /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setIndex(int index);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getEmail();
    public abstract void setEmail(String email);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getPhoneCode();
    public abstract void setPhoneCode(String phoneCode);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getPhoneNumber();
    public abstract void setPhoneNumber(String phoneNumber);
    
        /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getEmailInternal();
    public abstract void setEmailInternal(String email);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getPhoneCodeInternal();
    public abstract void setPhoneCodeInternal(String phoneCode);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getPhoneNumberInternal();
    public abstract void setPhoneNumberInternal(String phoneNumber);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getInfo();
    public abstract void setInfo(String info);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getColor();
    public abstract void setColor(String color);
    
     /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @jboss.load-group
     *    name="lazydata"
     */
    public abstract byte[] getPhoto();
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public abstract void setPhoto(byte[] photo);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract boolean getDeleted();
    public abstract void setDeleted(boolean deleted);       

    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract String getGoogleCalendarId();
    public abstract void setGoogleCalendarId(String googleCalendarId);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract boolean getGoogleCalendarNotification();
    public abstract void setGoogleCalendarNotification(boolean googleCalendarNotification);   
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.persistent-field
     * @ejb.value-object
     *      match="*"
     * @jboss.load-group
     *    name="base"
     */
    public abstract int getGoogleCalendarNotificationBefore();
    public abstract void setGoogleCalendarNotificationBefore(int googleCalendarNotificationBefore);  
    
    // Entity relations ---------------------------------------------------------------------------
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="activity-instructor"
     *      role-name="instructor-instruct-activities"
     * @jboss.relation
     *      fk-column="activity_id"
     *      related-pk-field="id"
     *      fk-constraint="true"
     * @jboss.relation-table
     *      table-name="instructor_activity"
     * @ejb.value-object
     *    match="*"
     *    aggregate="java.util.Set<ActivityDetails>"
     *    aggregate-name="Activities"
     */
    public abstract Collection<LocalActivity> getActivities();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setActivities(Collection<LocalActivity> activities);
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     * @ejb.relation
     *      name="instructor-sport_instructor"
     *      role-name="instructor-instruct-sport_instructors"
     *      cascade-delete=false
     */
    public abstract Collection<LocalSportInstructor> getSportInstructors();
    
    /**
     * @ejb.interface-method
     *      view-type="local"
     */
    public abstract void setSportInstructors(Collection<LocalSportInstructor> sportInstructors);
    
    // Entity methods -----------------------------------------------------------------------------
    
    /**
     * @ejb.value-object
     *      match="*"
     */
    public Set<SportDetails> getSports() {
        return sportSet;
    }
    
    public void setSports(Set<SportDetails> sports) {
        sportSet = sports;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public InstructorDetails getDetails() {
        InstructorDetails details = new InstructorDetails();
        details.setId(getId());
        details.setFirstName(getFirstName());
        details.setLastName(getLastName());
        details.setIndex(getIndex());
        details.setColor(getColor());
        details.setEmail(getEmail());
        details.setPhoneCode(getPhoneCode());
        details.setPhoneNumber(getPhoneNumber());
        details.setEmailInternal(getEmailInternal());
        details.setPhoneCodeInternal(getPhoneCodeInternal());
        details.setPhoneNumberInternal(getPhoneNumberInternal());
        details.setInfo(getInfo());
        details.setGoogleCalendarId(getGoogleCalendarId());
        details.setGoogleCalendarNotification(getGoogleCalendarNotification());
        details.setGoogleCalendarNotificationBefore(getGoogleCalendarNotificationBefore());
        
//        aktivity
        Iterator<LocalActivity> iter = getActivities().iterator();
        Set<ActivityDetails> activitySet = new HashSet<ActivityDetails>();
        while (iter.hasNext()) {
            LocalActivity activity = iter.next();
            ActivityDetails det = new ActivityDetails();
            det.setId(activity.getId());
            det.setName(activity.getName());
            det.setIconId(activity.getIconId());
            activitySet.add(det);
        }
        details.setActivities(activitySet);
        
//      Odfiltrovani smazanych polozek
        Set<SportDetails> sports = new HashSet<SportDetails>();
        Collection<LocalSportInstructor> sportInstructors = getSportInstructors();
        for (LocalSportInstructor localSportInstructor : sportInstructors) {
            LocalSport sport = localSportInstructor.getSport();
            if (sport != null && !localSportInstructor.getDeleted()) {
                SportDetails sportDet = new SportDetails();
                sportDet.setId(sport.getId());
                sportDet.setActivityId(sport.getActivityId());
                sports.add(sportDet);
            }
        }
        setSports(sports);
        details.setSports(sports);
        
        return details;
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setDetails(InstructorDetails details) {
        setFirstName(details.getFirstName());
        setLastName(details.getLastName());
        setIndex(details.getIndex());
        setColor(details.getColor());
        setPhoto(details.getPhoto());
        setEmail(details.getEmail());
        setPhoneCode(details.getPhoneCode());
        setPhoneNumber(details.getPhoneNumber());
        setEmailInternal(details.getEmailInternal());
        setPhoneCodeInternal(details.getPhoneCodeInternal());
        setPhoneNumberInternal(details.getPhoneNumberInternal());
        setInfo(details.getInfo());
        setGoogleCalendarId(details.getGoogleCalendarId());
        setGoogleCalendarNotification(details.getGoogleCalendarNotification());
        setGoogleCalendarNotificationBefore(details.getGoogleCalendarNotificationBefore());
        
//        aktivity
        getActivities().clear();
        if (details.getActivities() != null) {
            try {
                LocalActivityHome activityHome = ActivityUtil.getLocalHome();
                Iterator<ActivityDetails> iter = details.getActivities().iterator();
                
                while(iter.hasNext()) {
                    ActivityDetails activityDetails = iter.next();
                    LocalActivity activity = activityHome.findByPrimaryKey(activityDetails.getId());
                    getActivities().add(activity);
                }
            } catch (Exception ex) {
                logger.error("Nepodarilo se nastavit instruktory!", ex);
            }
        }

        updateSportInstructor(details);
        setSports(details.getSports());
    }
    
    /**
     * @ejb.interface-method
     *    view-type="local"
     */
    public void setAsDeleted(boolean deleted) {
        setDeleted(deleted);
        getActivities().clear();
        setSportInstructorsDeleted(deleted);
    }
    
    private void updateSportInstructor(InstructorDetails instructor) {
        //get old sports
        Set<String> oldSportIds = new HashSet<String>();
        for (LocalSportInstructor sportInstructor : getSportInstructors()) {
            if (!sportInstructor.getDeleted()) {
                oldSportIds.add(sportInstructor.getSport().getId());
            }
            logger.info("old sportId:" + sportInstructor.getSport().getId());
        }

        //get new sports, each sport's activity has to be presented in instructor's activities
        Set<String> activityIds = new HashSet<String>();
        for (ActivityDetails activity : instructor.getActivities()) {
            activityIds.add(activity.getId());
        }
        Set<String> newSportIds = new HashSet<String>();
        for (SportDetails sportDetails : (Set<SportDetails>) instructor.getSports()) {
            if (activityIds.contains(sportDetails.getActivityId())) {
                newSportIds.add(sportDetails.getId());
            }
        }

        Set<String> deleted = new HashSet<String>(oldSportIds);
        deleted.removeAll(newSportIds);
        if (!deleted.isEmpty()) {
            for (String sportIdToDelete : deleted) {
                try {
                    LocalSportInstructor sportInstructor = SportInstructorUtil.getLocalHome()
                            .findBySportAndInstructor(sportIdToDelete, instructor.getId());
                    sportInstructor.setDeleted(true);
                    //add NONE instructor if neccessary
                    sportInstructor.getSport().checkSportWithoutInstructor();
                } catch (Exception ex) {
                    logger.error("Nepodarilo se oznacit SportInstructor[sportId=" + sportIdToDelete + 
                            ", instructorId=" + instructor.getId() + "] jako smazany!", ex);
                }
            }
        }

        newSportIds.removeAll(oldSportIds);
        if (!newSportIds.isEmpty()) {
            for (String sportId : newSportIds) {
                try {
                    SportInstructorDetails sid = new SportInstructorDetails();
                    LocalSport localSport = SportUtil.getLocalHome().findByPrimaryKey(sportId);
                    sid.setActivityId(localSport.getActivity().getId());
                    sid.setDeleted(false);
                    sid.setInstructorId(instructor.getId());

                    SportInstructorUtil.getLocalHome().create(sid, localSport);
                } catch (Exception ex) {
                    logger.error("Nepodarilo se vytvorit sport instructor!", ex);
                }
            }
        }
    }
    
    private void setSportInstructorsDeleted(boolean deleted) {
        try {
            Collection<LocalSportInstructor> sportInstructors = SportInstructorUtil.getLocalHome().findByInstructor(getId());
            for (LocalSportInstructor localSportInstructor : sportInstructors) {
                localSportInstructor.setDeleted(deleted);
                //add NONE instructor if neccessary
                localSportInstructor.getSport().checkSportWithoutInstructor();
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }
    
    /**
     * @ejb.create-method
     */
    public String ejbCreate(InstructorDetails instructor) throws CreateException {
        String id = instructor.getId();
        if (id == null) {
            id = InstructorUtil.generateGUID(this);
        }
        setId(id);
        setFirstName(instructor.getFirstName());
        setLastName(instructor.getLastName());
        setIndex(instructor.getIndex());
        setColor(instructor.getColor());
        setPhoto(instructor.getPhoto());
        setEmail(instructor.getEmail());
        setPhoneNumber(instructor.getPhoneNumber());
        setEmailInternal(instructor.getEmailInternal());
        setPhoneNumberInternal(instructor.getPhoneNumberInternal());
        setInfo(instructor.getInfo());
        setGoogleCalendarId(instructor.getGoogleCalendarId());
        setGoogleCalendarNotification(instructor.getGoogleCalendarNotification());
        setGoogleCalendarNotificationBefore(instructor.getGoogleCalendarNotificationBefore());
        
        return id;
    }
    
    public void ejbPostCreate(InstructorDetails instructor) throws CreateException {
//        aktivity
        Set<ActivityDetails> activities = instructor.getActivities();
        if (activities != null && !activities.isEmpty()) {
            try {
                LocalActivityHome activityHome = ActivityUtil.getLocalHome();

                Collection<LocalActivity> activityCol = new HashSet<LocalActivity>();
                for (ActivityDetails activityDetails : activities) {
                    LocalActivity activity = activityHome.findByPrimaryKey(activityDetails.getId());
                    activityCol.add(activity);
                }
                setActivities(activityCol);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                throw new CreateException(ex.getMessage());
            }
        }
        
        
//        LocalSport instance = (LocalSport) getEntityContext().getEJBLocalObject();
        Set<SportDetails> sports = instructor.getSports();
        try {
            if (sports != null && !sports.isEmpty()) {
                for (SportDetails sportDetails : sports) {
                    SportInstructorDetails sid = new SportInstructorDetails();
                    LocalSport localSport = SportUtil.getLocalHome().findByPrimaryKey(sportDetails.getId());
                    sid.setActivityId(localSport.getActivity().getId());
                    sid.setDeleted(false);
                    sid.setInstructorId(instructor.getId());
                    SportInstructorUtil.getLocalHome().create(sid, localSport);
                }
            }
        } catch (Exception ex) {
            logger.error("Nepodarilo se nastavit vazbu trenera na sport!", ex);
        }
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(inst.id) FROM Instructor inst WHERE inst.deleted = ?1"
     */
    public abstract Long ejbSelectCountInstructors(boolean deleted) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountInstructors(boolean deleted) throws FinderException {
        return ejbSelectCountInstructors(deleted);
    }
    
    /**
     * @ejb.select
     * query="SELECT COUNT(instr.id) FROM Instructor instr, IN (instr.activities) AS activity WHERE activity.id = ?1 AND instr.deleted = ?2"
     */
    public abstract Long ejbSelectCountInstructorsByActivity(String activityId, boolean deleted) throws FinderException;
    
    /**
     * @ejb.home-method
     * view-type = "local"
     */
    public Long ejbHomeCountInstructorsByActivity(String activityId, boolean deleted) throws FinderException {
        return ejbSelectCountInstructorsByActivity(activityId, deleted);
    } 
}
