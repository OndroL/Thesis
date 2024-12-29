package cz.inspire.thesis.data.facade.sport;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportInstructorDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.service.sport.activity.ActivityService;
import cz.inspire.thesis.data.service.sport.sport.InstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportInstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class InstructorFacade {
    @Inject
    private InstructorService instructorService;
    @Inject
    private ActivityService activityService;
    @Inject
    private SportService sportService;
    @Inject
    private SportInstructorService sportInstructorService;

    public String create(InstructorDetails details) throws CreateException {
        try {
            InstructorEntity entity = instructorService.create(details);

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

                        SportEntity sport  = sportService.findOptionalBy(sportDetails.getId())
                               .orElseThrow(() -> new ApplicationException("Sport in postCreate of Instructor not found for Id: " + sportDetails.getId()));

                        sid.setDeleted(false);
                        sid.setSportId(sportDetails.getId());
                        sid.setActivityId(sport.getActivity().getId());

                        /**
                         * In old Bean there is call to SportInstructorUtil.getLocalHome().create(sid, localSport);
                         * I do not know what it is doing, but I'm almost sure I can do exactly the same with calling sportInstructor create
                         * with SportId set
                         */
                        create(sid); //call create(SportInstructorDetails details)
                    }
                } catch (Exception e) {
                    throw new ApplicationException("Relationship wih Sports while creating Instructor couldn't be created: " + e.getMessage(), e);
                }
            }
            instructorService.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed while creating InstructorEntity" , e);
        }
    }

    public String create(SportInstructorDetails details) throws CreateException {
        try {
            SportInstructorEntity entity = sportInstructorService.create(details);

            setSportAndInstructor(entity, details);

            sportInstructorService.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed while creating new SportInstructorEntity ", e);
        }

    }

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

        instructorService.save(entity);
    }

    public void setDetails(SportInstructorDetails details) throws ApplicationException {
        try {
            SportInstructorEntity entity = sportInstructorService.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("SportInstructor entity not found"));
            setSportAndInstructor(entity, details);
            sportInstructorService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update SportInstructor", e);
        }
    }

    public void setAsDeleted(InstructorEntity entity, boolean deleted) throws ApplicationException {
        entity.setDeleted(deleted);
        entity.getActivities().clear();
        setSportInstructorsDeleted(entity, deleted);
        instructorService.save(entity);
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

    public SportInstructorDetails getDetails(SportInstructorEntity entity) {
        SportInstructorDetails details = new SportInstructorDetails();
        details.setId(entity.getId());
        details.setActivityId(entity.getActivityId());
        details.setOldSportId(entity.getOldSportId());
        details.setDeleted(entity.getDeleted());
        details.setSportId(entity.getSport() != null ? entity.getSport().getId() : null);
        details.setInstructorId(entity.getInstructor() != null ? entity.getInstructor().getId() : null);
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
                sportInstructorService.checkSportWithoutInstructor(sport);
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

            // Collect new sport IDs where activity is present in details activities
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
                sportInstructorService.checkSportWithoutInstructor(sportInstructor.getSport());
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

                sportInstructorService.create(getDetails(newSportInstructor));
            }

        } catch (Exception ex) {
            throw new ApplicationException("Failed to update sport instructors for details ID: " + details.getId(), ex);
        }
    }


    private void setSportAndInstructor(SportInstructorEntity entity, SportInstructorDetails details) throws ApplicationException {
        entity.setActivityId(details.getActivityId());
        entity.setOldSportId(details.getOldSportId());
        entity.setDeleted(details.getDeleted());

        // Map Sport
        if (details.getSportId() != null) {
            SportEntity sport = sportService.findOptionalBy(details.getSportId())
                    .orElseThrow(() -> new ApplicationException("Sport entity not found"));

            //This was not present in old Bean
            entity.setActivityId(sport.getActivity().getId());

            entity.setSport(sport);
        } else {
            entity.setSport(null);
        }

        // Map Instructor
        if (details.getInstructorId() != null) {
            InstructorEntity instructor = instructorService.findOptionalBy(details.getInstructorId())
                    .orElseThrow(() -> new ApplicationException("Instructor entity not found"));
            entity.setInstructor(instructor);
        } else {
            entity.setInstructor(null);
        }
    }


    ////////////////////////
    /////// Queries ///////
    //////////////////////

    public Collection<InstructorDetails> findByActivity(String activityId, boolean deleted) {
        return instructorService.findByActivity(activityId, deleted).stream().map(this::getDetails).toList();
    }

    public Collection<InstructorDetails> findAll() {
        return instructorService.findAll().stream().map(this::getDetails).toList();
    }

    public Collection<InstructorDetails> findAll(int offset, int count, boolean deleted) {
        return instructorService.findAll(offset, count, deleted).stream().map(this::getDetails).toList();
    }

    public long countInstructors(boolean deleted) {
        return instructorService.countInstructors(deleted);
    }

    public long countInstructorsByActivity(String activityId, boolean deleted) {
        return instructorService.countInstructorsByActivity(activityId, deleted);
    }


    // Renamed from findAll to findAllSportInstructor
    public Collection<SportInstructorDetails> findAllSportInstructor() {
        return sportInstructorService.findAll().stream().map(this::getDetails).toList();
    }

    public List<SportInstructorDetails> findBySport(String sportId) {
        return sportInstructorService.findBySport(sportId).stream().map(this::getDetails).toList();
    }

    public List<SportInstructorDetails> findByInstructor(String instructorId) {
        return sportInstructorService.findByInstructor(instructorId).stream().map(this::getDetails).toList();
    }

    public Optional<SportInstructorDetails> findBySportAndInstructor(String sportId, String instructorId) {
        return sportInstructorService.findBySportAndInstructor(sportId, instructorId).map(this::getDetails);
    }

    public Optional<SportInstructorDetails> findBySportWithoutInstructor(String sportId) {
        return sportInstructorService.findBySportWithoutInstructor(sportId).map(this::getDetails);
    }

    public Long countSportInstructors(String sportId) {
        return sportInstructorService.countSportInstructors(sportId);
    }


}
