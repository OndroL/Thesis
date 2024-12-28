package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.*;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class InstructorService {

    @Inject
    private InstructorRepository instructorRepository;

    @Transactional
    public InstructorEntity create(InstructorDetails details) throws CreateException {
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
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create Instructor entity", e);
        }
    }

    @Transactional
    public void save(InstructorEntity entity) throws ApplicationException {
        try {
            instructorRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save InstructorEntity ", e );
        }
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
