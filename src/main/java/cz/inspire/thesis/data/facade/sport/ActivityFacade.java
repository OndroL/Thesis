package cz.inspire.thesis.data.facade.sport;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.dto.sport.activity.ActivityFavouriteDetails;
import cz.inspire.thesis.data.dto.sport.activity.ActivityWebTabDetails;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import cz.inspire.thesis.data.model.sport.activity.ActivityWebTabEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.service.sport.activity.ActivityFavouriteService;
import cz.inspire.thesis.data.service.sport.activity.ActivityService;
import cz.inspire.thesis.data.service.sport.activity.ActivityWebTabService;
import cz.inspire.thesis.data.service.sport.sport.InstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportInstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



public class ActivityFacade {
    @Inject
    private ActivityService activityService;
    @Inject
    private InstructorService instructorService;
    @Inject
    private SportInstructorService sportInstructorService;
    @Inject
    private SportService sportService;
    @Inject
    private ActivityFavouriteService activityFavouriteService;
    @Inject
    private ActivityWebTabService activityWebTabService;

    //Activity
    public String create(ActivityDetails details) throws CreateException {
        try {
            ActivityEntity entity = activityService.create(details);

            //postCreate
            setInstructors(entity, details);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity", e);
        }
    }

    //ActivityFavourite
    public String create(ActivityFavouriteDetails details) throws CreateException {
        try {
            ActivityFavouriteEntity entity = activityFavouriteService.create(details);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityFavourite entity", e);
        }
    }

    //ActivityWebTab
    public String create(ActivityWebTabDetails details) throws CreateException {
        try {
            ActivityWebTabEntity entity = activityWebTabService.create(details);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityWebTab entity", e);
        }
    }

    public void setDetails(ActivityDetails details) throws ApplicationException {
        try {
            // Find the activity entity by ID or throw an exception if not found
            ActivityEntity entity = activityService.findById(details.getId());
            // Update basic fields
            entity.setName(details.getName());
            entity.setDescription(details.getDescription());
            entity.setIndex(details.getIndex());
            entity.setIconId(details.getIconId());

            // Fetch current instructor IDs from the entity
            Set<String> oldInstructorIds = entity.getInstructors().stream()
                    .map(InstructorEntity::getId)
                    .collect(Collectors.toSet());

            // Clear existing instructors
            entity.getInstructors().clear();

            // Add new instructors if provided
            if (details.getInstructors() != null) {
                Set<InstructorEntity> instructors = details.getInstructors().stream()
                        .map(instructorDetails -> {
                            try {
                                return instructorService.findOptionalBy(instructorDetails.getId())
                                        .orElseThrow(() -> new ApplicationException("Instructor not found for ID: " + instructorDetails.getId()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet());

                entity.setInstructors(instructors);
            }

            // Save the updated entity
            activityService.saveEntity(entity);

            // Update related sport-instructor entities
            updateSportInstructor(details, oldInstructorIds);
        } catch (Exception ex) {
            throw new ApplicationException("Failed to update Activity entity: " + ex.getMessage(), ex);
        }
    }

    public void setDetails(ActivityFavouriteDetails details) throws ApplicationException {
        try {
            ActivityFavouriteEntity entity = activityFavouriteService.findById(details.getId());
            entity.setZakaznikId(details.getZakaznikId());
            entity.setActivityId(details.getActivityId());
            entity.setPocet(details.getPocet());
            entity.setDatumPosledniZmeny(details.getDatumPosledniZmeny());

            activityFavouriteService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update ActivityFavourite entity", e);
        }
    }


    private void setInstructors(ActivityEntity entity, ActivityDetails details) throws ApplicationException {
        Set<InstructorDetails> instructors = (Set<InstructorDetails>) details.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            try {
                Collection<InstructorEntity> instructorSet = new HashSet<InstructorEntity>();
                for (InstructorDetails instructorDetails : instructors) {
                    InstructorEntity instructor = instructorService.findOptionalBy(instructorDetails.getId())
                            .orElseThrow(() -> new ApplicationException("Instructor entity not found for ID: " + instructorDetails.getId()));
                    instructorSet.add(instructor);
                }
                entity.setInstructors(instructorSet);
                activityService.saveEntity(entity);
            } catch (Exception ex) {
                throw new ApplicationException("Failed to associate instructors with activity: " + ex.getMessage(), ex);
            }
        }
    }

    private void updateSportInstructor(ActivityDetails activityDetails, Set<String> oldInstructorIds) throws ApplicationException {
        try {
            // Extract new instructor IDs from ActivityDetails
            Set<String> newInstructorIds = activityDetails.getInstructors() != null
                    ? activityDetails.getInstructors().stream()
                    .map(InstructorDetails::getId)
                    .collect(Collectors.toSet())
                    : new HashSet<>();

            // Determine instructors to delete (oldInstructorIds - newInstructorIds)
            oldInstructorIds.removeAll(newInstructorIds);

            // Iterate through the sports associated with the activity
            for (SportDetails sportDetails : activityDetails.getSports()) {
                // Fetch the corresponding SportEntity
                SportEntity sportEntity = sportService.findOptionalBy(sportDetails.getId())
                        .orElseThrow(() -> new ApplicationException("Sport not found for ID: " + sportDetails.getId()));

                // Iterate through sport-instructor mappings
                for (SportInstructorEntity sportInstructor : sportEntity.getSportInstructors()) {
                    if (sportInstructor.getInstructor() != null
                            && oldInstructorIds.contains(sportInstructor.getInstructor().getId())) {
                        // Mark sport-instructor entity as deleted
                        sportInstructor.setDeleted(true);
                        sportInstructorService.setDetails(sportInstructorService.getDetails(sportInstructor)); // Save the updated entity
                    }
                }

                // Ensure the sport is not left without any instructors if necessary
                sportService.checkSportWithoutInstructor(sportEntity);
            }
        } catch (Exception ex) {
            throw new ApplicationException("Failed to update sport-instructor relationships: " + ex.getMessage(), ex);
        }
    }

    public ActivityDetails getDetails(ActivityEntity entity) {
        ActivityDetails details = new ActivityDetails();
        details.setId(entity.getId());
        details.setName(entity.getName());
        details.setDescription(entity.getDescription());
        details.setIndex(entity.getIndex());
        details.setIconId(entity.getIconId());

        if (entity.getInstructors() != null) {
            Set<InstructorDetails> instructors = entity.getInstructors().stream()
                    .map(inst -> new InstructorDetails(inst.getId(), inst.getFirstName(), inst.getLastName(), inst.getColor()))
                    .collect(Collectors.toSet());
            details.setInstructors(instructors);
        }

        return details;
    }

    public ActivityFavouriteDetails getDetails(ActivityFavouriteEntity entity) {
        return new ActivityFavouriteDetails(
                entity.getId(),
                entity.getZakaznikId(),
                entity.getActivityId(),
                entity.getPocet(),
                entity.getDatumPosledniZmeny()
        );
    }

    public ActivityWebTabDetails getDetails(ActivityWebTabEntity entity) {
        return new ActivityWebTabDetails(
                entity.getId(),
                entity.getSportId(),
                entity.getActivityId(),
                entity.getObjectId(),
                entity.getTabIndex()
        );
    }

    // Activity finders
    public ActivityEntity findById(String id) throws ApplicationException {
        return  activityService.findOptionalBy(id)
                .orElseThrow(() -> new ApplicationException("Activity entity not found for ID: " + id));
    }

    public Collection<ActivityDetails> findAll() {
        return activityService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityDetails> findAll(int offset, int count) {
        return activityService.findAll(offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityDetails> findAllByInstructor(String instructorId, int offset, int count) {
        return activityService.findAllByInstructor(instructorId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long countActivities() {
        return activityService.countActivities();
    }

    public Long countActivitiesByInstructor(String instructorId) {
        return activityService.countActivitiesByInstructor(instructorId);
    }

    // ActivityFavourite finders

    public Collection<ActivityFavouriteDetails> findByZakaznik(String zakaznikId, int limit, int offset) {
        return activityFavouriteService.findByZakaznik(zakaznikId, limit, offset).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Optional<ActivityFavouriteDetails> findByZakaznikAktivita(String zakaznikId, String activityId) {
        return activityFavouriteService.findByZakaznikAktivita(zakaznikId, activityId).map(this::getDetails);
    }

    // ActivityWebTab finders

    // This has different name because of name clash with findAll fo Activity above
    public Collection<ActivityWebTabDetails> findAllActivityWebTab() {
        return activityWebTabService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findBySport(String sportId) {
        return activityWebTabService.findBySport(sportId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findByActivity(String activityId) {
        return activityWebTabService.findByActivity(activityId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findByObject(String objectId) {
        return activityWebTabService.findByObject(objectId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

}
