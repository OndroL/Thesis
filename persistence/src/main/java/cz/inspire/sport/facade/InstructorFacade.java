package cz.inspire.sport.facade;

import cz.inspire.exception.ApplicationException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.mapper.InstructorMapper;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class InstructorFacade {
    @Inject
    InstructorService instructorService;
    @Inject
    InstructorMapper instructorMapper;
    @Inject
    SportInstructorService sportInstructorService;

    static final Logger logger = LogManager.getLogger(InstructorFacade.class);

    public String create(InstructorDto dto) throws CreateException, IOException, FinderException, ApplicationException {
        InstructorEntity entity = instructorService.create(instructorMapper.toEntity(dto));
        if (dto.getPhoto() != null) {
            instructorService.savePhoto(dto.getPhoto());
        }
        return entity.getId();
    }

    public void delete(String id) throws FinderException, RemoveException, IOException {
        instructorService.deleteFile(instructorService.findByPrimaryKey(id).getPhoto().getFilePath());
        instructorService.delete(instructorService.findByPrimaryKey(id));
    }

    public void setAsDeleted(boolean deleted, String instructorId) throws FinderException, SystemException {
        if (instructorId != null) {
            InstructorEntity instructor = instructorService.findByPrimaryKey(instructorId);
            instructor.setDeleted(deleted);
            instructorService.update(instructor);
            this.setSportInstructorsDeleted(deleted, instructorId);
        } else {
            logger.info("Unable to set Instructor as deleted, because instructorId is null");
        }
    }

    public void setSportInstructorsDeleted(boolean deleted, String instructorId) {
        try {
            List<SportInstructorEntity> sportInstructors = sportInstructorService.findByInstructor(instructorId);
            for (SportInstructorEntity sportInstructor : sportInstructors) {
                sportInstructor.setDeleted(deleted);
                sportInstructorService.update(sportInstructor);
                sportInstructorService.checkSportWithoutInstructor(sportInstructor.getSport());
            }
        } catch (Exception ex) {
            logger.error("Error setting SportInstructorEntity deleted status for instructorId: {}", instructorId, ex);
        }
    }

    public void update(InstructorDto dto) throws SystemException, FinderException, ApplicationException {
        instructorService.update(instructorMapper.toEntity(dto));
    }

    public InstructorDto mapToDto(InstructorEntity entity) {
        return instructorMapper.toDto(entity);
    }

    public List<InstructorDto> findAll() throws FinderException {
        return instructorService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<InstructorDto> findAll(int offset, int count, boolean deleted) throws FinderException {
        return instructorService.findAll(count, offset, deleted).stream().map(this::mapToDto).toList();
    }

    public List<InstructorDto> findAllByActivity(String activityId, int offset, int count, boolean deleted) throws FinderException {
        return instructorService.findAllByActivity(activityId, count, offset, deleted).stream().map(this::mapToDto).toList();
    }

    public Long countInstructors(boolean deleted) throws FinderException {
        return instructorService.countInstructors(deleted);
    }

    public Long countInstructorsByActivity(String activityId, boolean deleted) throws FinderException {
        return instructorService.countInstructorsByActivity(activityId, deleted);
    }
}
