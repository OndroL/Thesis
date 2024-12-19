package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportInstructorDetails;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportInstructorRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportInstructorService {

    @Inject
    private SportInstructorRepository sportInstructorRepository;

    @Inject
    private SportRepository sportRepository;

    @Inject
    private InstructorRepository instructorRepository;

    @Transactional
    public String create(SportInstructorDetails details) throws CreateException {
        try {
            SportInstructorEntity entity = new SportInstructorEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            mapToEntity(entity, details);
            sportInstructorRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create SportInstructor", e);
        }
    }

    @Transactional
    public void setDetails(SportInstructorDetails details) throws ApplicationException {
        try {
            SportInstructorEntity entity = sportInstructorRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("SportInstructor entity not found"));
            mapToEntity(entity, details);
            sportInstructorRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update SportInstructor", e);
        }
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

    public Collection<SportInstructorDetails> findAll() {
        return sportInstructorRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<SportInstructorDetails> findBySport(String sportId) {
        return sportInstructorRepository.findBySport(sportId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<SportInstructorDetails> findByInstructor(String instructorId) {
        return sportInstructorRepository.findByInstructor(instructorId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long countSportInstructors(String sportId) {
        return sportInstructorRepository.countSportInstructors(sportId);
    }

    private void mapToEntity(SportInstructorEntity entity, SportInstructorDetails details) throws ApplicationException {
        entity.setActivityId(details.getActivityId());
        entity.setOldSportId(details.getOldSportId());
        entity.setDeleted(details.getDeleted());

        // Map Sport
        if (details.getSportId() != null) {
            SportEntity sport = sportRepository.findOptionalBy(details.getSportId())
                    .orElseThrow(() -> new ApplicationException("Sport entity not found"));
            entity.setSport(sport);
        } else {
            entity.setSport(null);
        }

        // Map Instructor
        if (details.getInstructorId() != null) {
            InstructorEntity instructor = instructorRepository.findOptionalBy(details.getInstructorId())
                    .orElseThrow(() -> new ApplicationException("Instructor entity not found"));
            entity.setInstructor(instructor);
        } else {
            entity.setInstructor(null);
        }
    }
}
