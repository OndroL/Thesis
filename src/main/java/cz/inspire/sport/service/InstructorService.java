package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.repository.InstructorRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public class InstructorService extends BaseService<InstructorEntity, String, InstructorRepository> {

    public InstructorService() {
    }

    @Inject
    public InstructorService(InstructorRepository repository) {
        super(repository);
    }

    public List<InstructorEntity> findAllOrdered() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all InstructorEntity records in ordered manner"
        );
    }

    public List<InstructorEntity> findAll(int offset, int count, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(Limit.range(offset + 1, count), deleted),
                "Error retrieving paginated InstructorEntity records (offset + 1 = " + offset +
                        ", count = " + count + ", deleted = " + deleted + ")"
        );
    }

    public List<InstructorEntity> findAllByActivity(String activityId, int offset, int count, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.findAllByActivity(activityId, Limit.range(offset + 1, count), deleted),
                "Error retrieving InstructorEntity records by activityId=" + activityId +
                        " with pagination (offset + 1 = " + offset + ", count = " + count + ", deleted = " + deleted + ")"
        );
    }

    public Long countInstructors(boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.countInstructors(deleted),
                "Error retrieving total count of InstructorEntity records (deleted = " + deleted + ")"
        );
    }

    public Long countInstructorsByActivity(String activityId, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.countInstructorsByActivity(activityId, deleted),
                "Error retrieving count of InstructorEntity records by activityId=" + activityId +
                        " (deleted = " + deleted + ")"
        );
    }

}
