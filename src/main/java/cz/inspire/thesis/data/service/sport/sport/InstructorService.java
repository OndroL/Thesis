package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
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
    public void postCreate(InstructorDetails details, InstructorEntity entity) throws CreateException {
        try {
            // Additional logic for post-create, if needed.
        } catch (Exception e) {
            throw new CreateException("Instructor couldn't be created: " + e.getMessage(), e);
        }
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

        return details;
    }

    public Collection<InstructorDetails> findByActivity(String activityId, boolean deleted) {
        return instructorRepository.findAllByActivity(activityId, 0, Integer.MAX_VALUE, deleted).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<InstructorDetails> findAll() {
        return instructorRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<InstructorDetails> findAll(int offset, int count, boolean deleted) {
        return instructorRepository.findAll(offset, count, deleted).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public long countInstructors(boolean deleted) {
        return instructorRepository.countInstructors(deleted);
    }

    public long countInstructorsByActivity(String activityId, boolean deleted) {
        return instructorRepository.countInstructorsByActivity(activityId, deleted);
    }
}
