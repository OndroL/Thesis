package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportInstructorDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import cz.inspire.thesis.data.service.sport.activity.ActivityService;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class InstructorService {

    @Inject
    private InstructorRepository instructorRepository;

    @Inject
    private ActivityService activityService;

    @Inject
    private SportService sportService;

    @Inject
    private SportInstructorService sportInstructorService;

    @Transactional
    public String create(InstructorDetails details) throws CreateException {
        try {
            InstructorEntity entity = new InstructorEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setFirstName(details.getFirstName());
            entity.setLastName(details.getLastName());
            entity.setIndex(details.getIndex());
            entity.setEmail(details.getEmail());
            entity.setPhoneCode(details.getPhoneCode());
            entity.setPhoneNumber(details.getPhoneNumber());
            entity.setEmailInternal(details.getEmailInternal());
            entity.setPhoneCodeInternal(details.getPhoneCodeInternal());
            entity.setPhoneNumberInternal(details.getPhoneNumberInternal());
            entity.setInfo(details.getInfo());
            entity.setColor(details.getColor());
            entity.setPhoto(details.getPhoto());
            entity.setDeleted(details.getDeleted());
            entity.setGoogleCalendarId(details.getGoogleCalendarId());
            entity.setGoogleCalendarNotification(details.getGoogleCalendarNotification());
            entity.setGoogleCalendarNotificationBefore(details.getGoogleCalendarNotificationBefore());

            instructorRepository.save(entity);

            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Instructor entity", e);
        }
    }

    @Transactional
    public void postCreate(InstructorDetails details, InstructorEntity entity) throws Exception {
        Set<ActivityDetails> activities = details.getActivities();
        if (activities != null && !activities.isEmpty()) {
            try {
                Collection<ActivityEntity> newActivities = new HashSet<ActivityEntity>();
                for (ActivityDetails activityDetails : activities) {
                    ActivityEntity activity  = activityService.findOptionalBy(activityDetails.getId())
                            .orElseThrow(() -> new ApplicationException("Activity in postCreate of Instructor not found for Id: " + activityDetails.getId()));
                    newActivities.add(activity);
                }
                entity.setActivities(newActivities);
            } catch (Exception e) {
                throw new CreateException("Relationship wih Activities while creating Instructor couldn't be created: " + e.getMessage(), e);
            }
        }

        Set<SportDetails> sports = details.getSports();
        if (sports != null && !sports.isEmpty()) {
            try {
                for (SportDetails sportDetails : sports) {
                    SportInstructorDetails sid = new SportInstructorDetails();
                    sid.setDeleted(false);
                    sid.setInstructorId(details.getId());
                    sid.setSportId(sportDetails.getId());

//                   Here is how it was done in old Bean, but it makes more sense to check this while creating SportInstructorEntity
//
//                   SportEntity sport  = sportService.findOptionalBy(sportDetails.getId())
//                           .orElseThrow(() -> new ApplicationException("Sport in postCreate of Instructor not found for Id: " + sportDetails.getId()));
//
//                   And for some reason in activityId is set for details but in SportInstructor bean it is not set into entity
//
//                   sid.setActivityId(sport.getActivity().getId());
//                   sportInstructorService.create(sid, sport);
                    sportInstructorService.create(sid);

                }
            } catch (Exception e) {
                throw new ApplicationException("Relationship wih Sports while creating Instructor couldn't be created: " + e.getMessage(), e);
            }
        }
        instructorRepository.save(entity);
    }
    @Transactional
    public void setDetails(InstructorDetails details, InstructorEntity entity) throws ApplicationException {
        entity.setFirstName(details.getFirstName());
        entity.setLastName(details.getLastName());
        entity.setIndex(details.getIndex());
        entity.setColor(details.getColor());
        entity.setPhoto(details.getPhoto());
        entity.setEmail(details.getEmail());
        entity.setPhoneCode(details.getPhoneCode());
        entity.setPhoneNumber(details.getPhoneNumber());
        entity.setEmailInternal(details.getEmailInternal());
        entity.setPhoneCodeInternal(details.getPhoneCodeInternal());
        entity.setPhoneNumberInternal(details.getPhoneNumberInternal());
        entity.setInfo(details.getInfo());
        entity.setGoogleCalendarId(details.getGoogleCalendarId());
        entity.setGoogleCalendarNotification(details.getGoogleCalendarNotification());
        entity.setGoogleCalendarNotificationBefore(details.getGoogleCalendarNotificationBefore());

        //aktivity
        try {
            Collection<ActivityEntity> currentActivities = entity.getActivities();
            if (currentActivities != null) {
                currentActivities.clear();
            }
            if (details.getActivities() != null) {
                Collection<ActivityEntity> newActivities = details.getActivities().stream()
                        .map(activityDetails -> {
                            try {
                                return activityService.findOptionalBy(activityDetails.getId())
                                        .orElseThrow(() -> new ApplicationException("Activity not found for ID: " + activityDetails.getId()));
                            } catch (ApplicationException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet());
                entity.setActivities(newActivities);
            }
        } catch (Exception ex) {
            throw new ApplicationException("Failed to set activities for InstructorEntity with ID: " + entity.getId(), ex);
        }

        updateSportInstructor(details, entity);
        entity.setSportSet(details.getSports());

        instructorRepository.save(entity);
    }

    public InstructorDetails getDetails(InstructorEntity entity) {
        InstructorDetails details = new InstructorDetails();
        details.setId(entity.getId());
        details.setFirstName(entity.getFirstName());
        details.setLastName(entity.getLastName());
        details.setIndex(entity.getIndex());
        details.setEmail(entity.getEmail());
        details.setPhoneCode(entity.getPhoneCode());
        details.setPhoneNumber(entity.getPhoneNumber());
        details.setEmailInternal(entity.getEmailInternal());
        details.setPhoneCodeInternal(entity.getPhoneCodeInternal());
        details.setPhoneNumberInternal(entity.getPhoneNumberInternal());
        details.setInfo(entity.getInfo());
        details.setColor(entity.getColor());
        details.setPhoto(entity.getPhoto());
        details.setDeleted(entity.getDeleted());
        details.setGoogleCalendarId(entity.getGoogleCalendarId());
        details.setGoogleCalendarNotification(entity.getGoogleCalendarNotification());
        details.setGoogleCalendarNotificationBefore(entity.getGoogleCalendarNotificationBefore());

        // aktivity
        Iterator<ActivityEntity> iter = entity.getActivities().iterator();
        Set<ActivityDetails> activitySet = new HashSet<ActivityDetails>();
        while (iter.hasNext()) {
            ActivityEntity activity = iter.next();
            ActivityDetails det = new ActivityDetails();
            det.setId(activity.getId());
            det.setName(activity.getName());
            det.setIconId(activity.getIconId());
            activitySet.add(det);
        }
        details.setActivities(activitySet);

//      Odfiltrovani smazanych polozek
        Set<SportDetails> sports = new HashSet<SportDetails>();
        Collection<SportInstructorEntity> sportInstructors = entity.getSportInstructors();
        for (SportInstructorEntity localSportInstructor : sportInstructors) {
            SportEntity sport = localSportInstructor.getSport();
            if (sport != null && !localSportInstructor.getDeleted()) {
                SportDetails sportDet = new SportDetails();
                sportDet.setId(sport.getId());
                sportDet.setActivityId(sportService.getActivityId(sport));
                sports.add(sportDet);
            }
        }
        entity.setSportSet(sports);
        details.setSports(sports);
        return details;
    }

    private void setSportInstructorsDeleted(InstructorEntity entity, boolean deleted) throws ApplicationException {
        try {
            Collection<SportInstructorEntity> sportInstructors = sportInstructorService.findByInstructor(entity.getId());
            for (SportInstructorEntity sportInstructor : sportInstructors) {
                sportInstructor.setDeleted(deleted);
                //add NONE instructor if neccessary
                SportEntity sport = sportService.findOptionalBy(sportInstructor.getSport().getId())
                        .orElseThrow(() -> new ApplicationException("Sport not found for ID: " + sportInstructor.getSport().getId()));
                sportService.checkSportWithoutInstructor(sport);
            }
        } catch (Exception ex) {
            throw new ApplicationException("Failed while setting Sport Instructor as deleted " + ex.getMessage());
        }
    }

    public void updateSportInstructor(InstructorDetails details, InstructorEntity entity) throws ApplicationException {
        try {
            // Get current sports associated with the details
            Set<String> oldSportIds = entity.getSportInstructors().stream()
                    .filter(sportInstructor -> !sportInstructor.getDeleted())
                    .map(sportInstructor -> sportInstructor.getSport().getId())
                    .collect(Collectors.toSet());

            //logger.info("Old Sport IDs: " + oldSportIds);

            // Collect activity IDs from details's activities
            Set<String> activityIds = details.getActivities().stream()
                    .map(ActivityDetails::getId)
                    .collect(Collectors.toSet());

            // Collect new sport IDs where activity is present in details's activities
            Set<String> newSportIds = details.getSports().stream()
                    .filter(sportDetails -> activityIds.contains(sportDetails.getActivityId()))
                    .map(SportDetails::getId)
                    .collect(Collectors.toSet());

            // Determine sports to be removed
            Set<String> sportsToDelete = new HashSet<>(oldSportIds);
            sportsToDelete.removeAll(newSportIds);

            // Mark sports as deleted
            for (String sportIdToDelete : sportsToDelete) {
                SportInstructorEntity sportInstructor = sportInstructorService.findBySportAndInstructor(sportIdToDelete, details.getId())
                        .orElseThrow(() -> new ApplicationException("SportInstructor not found for sportId: " + sportIdToDelete + " and instructorId: " + details.getId()));
                sportInstructorService.setDeleted(sportInstructor);

                // Check if the sport is left without any instructors and handle it
                sportService.checkSportWithoutInstructor(sportInstructor.getSport());
            }

            // Determine sports to be added
            Set<String> sportsToAdd = new HashSet<>(newSportIds);
            sportsToAdd.removeAll(oldSportIds);

            // Add new sports
            for (String sportId : sportsToAdd) {
                SportEntity sport = sportService.findOptionalBy(sportId)
                        .orElseThrow(() -> new ApplicationException("Sport not found for ID: " + sportId));

                SportInstructorEntity newSportInstructor = new SportInstructorEntity();
                newSportInstructor.setSport(sport);
                newSportInstructor.setInstructor(entity);
                newSportInstructor.setDeleted(false);

                sportInstructorService.create(sportInstructorService.getDetails(newSportInstructor));
            }

        } catch (Exception ex) {
            throw new ApplicationException("Failed to update sport instructors for details ID: " + details.getId(), ex);
        }
    }

    @Transactional
    public void setAsDeleted(InstructorEntity entity, boolean deleted) throws ApplicationException {
        entity.setDeleted(deleted);
        entity.getActivities().clear();
        setSportInstructorsDeleted(entity, deleted);
        instructorRepository.save(entity);
    }


    public Collection<InstructorEntity> findByActivity(String activityId, boolean deleted) {
        return instructorRepository.findAllByActivity(activityId, 0, Integer.MAX_VALUE, deleted);
    }

    public Collection<InstructorEntity> findAll() {
        return instructorRepository.findAll();
    }

    public Collection<InstructorEntity> findAll(int offset, int count, boolean deleted) {
        return instructorRepository.findAll(offset, count, deleted);
    }

    public long countInstructors(boolean deleted) {
        return instructorRepository.countInstructors(deleted);
    }

    public long countInstructorsByActivity(String activityId, boolean deleted) {
        return instructorRepository.countInstructorsByActivity(activityId, deleted);
    }

    public Optional<InstructorEntity> findOptionalBy(String id) {
        return instructorRepository.findOptionalBy(id);
    }
}
