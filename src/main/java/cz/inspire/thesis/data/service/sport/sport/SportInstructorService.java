package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportInstructorDetails;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportInstructorRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportInstructorService {

    @Inject
    private SportInstructorRepository sportInstructorRepository;


    @Transactional
    public SportInstructorEntity create(SportInstructorDetails details) throws CreateException {
        try {
            SportInstructorEntity entity = new SportInstructorEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            sportInstructorRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create SportInstructor", e);
        }
    }

    @Transactional
    public void save(SportInstructorEntity entity) throws ApplicationException {
        try {
            sportInstructorRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save SportInstructorEntity ", e);
        }
    }

    @Transactional
    public void setDeleted(SportInstructorEntity entity) throws ApplicationException{
        try {
            entity.setDeleted(true);
            sportInstructorRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to set SportInstructor as deleted" + e);
        }
    }

    // Moved from SportService here

    public void checkSportWithoutInstructor(SportEntity sport) throws CreateException {
        try {
            Long instructorsCount = sportInstructorRepository.countSportInstructors(sport.getId());
            if (instructorsCount == null || instructorsCount == 0) {
                //add special instructor representing NONE instructor
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivity().getId());
                sid.setDeleted(false);
                sid.setInstructorId(null);
                create(sid);
            }
        } catch (CreateException ex) {
            throw new CreateException("Failed to create SportInstructorEntity without Instructor for sport : " + sport.getId(), ex);
        }
//        } catch (ApplicationException ex) {
//            throw new ApplicationException("Nepodarilo sa zistit pocet instruktorov sportu " + sport.getId(), ex);
//        }
    }

    public Collection<SportInstructorEntity> findAll() {
        return sportInstructorRepository.findAll();
    }

    public List<SportInstructorEntity> findBySport(String sportId) {
        return sportInstructorRepository.findBySport(sportId);
    }

    public List<SportInstructorEntity> findByInstructor(String instructorId) {
        return sportInstructorRepository.findByInstructor(instructorId);
    }

    public Optional<SportInstructorEntity> findBySportAndInstructor(String sportId, String instructorId) {
        return sportInstructorRepository.findBySportAndInstructor(sportId, instructorId);
    }

    public Optional<SportInstructorEntity> findBySportWithoutInstructor(String sportId) {
        return sportInstructorRepository.findBySportWithoutInstructor(sportId);
    }

    public Long countSportInstructors(String sportId) {
        return sportInstructorRepository.countSportInstructors(sportId);
    }

    public Optional<SportInstructorEntity> findOptionalBy(String id) {
        return sportInstructorRepository.findOptionalBy(id);
    }

}
