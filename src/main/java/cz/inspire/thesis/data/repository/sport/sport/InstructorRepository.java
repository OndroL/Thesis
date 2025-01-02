package cz.inspire.thesis.data.repository.sport.sport;

import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface InstructorRepository extends EntityRepository<InstructorEntity, String> {

    @Query("SELECT i FROM InstructorEntity i ORDER BY i.index ASC")
    List<InstructorEntity> findAll();

    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = ?1 ORDER BY i.index ASC")
    List<InstructorEntity> findAll(@FirstResult int offset, @MaxResults int count, boolean deleted);

    @Query("""
        SELECT i FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = ?1 AND i.deleted = ?2
        ORDER BY i.index ASC
    """)
    List<InstructorEntity> findAllByActivity(String activityId, @FirstResult int offset, @MaxResults int count, boolean deleted);

    @Query("SELECT COUNT(i.id) FROM InstructorEntity i WHERE i.deleted = ?1")
    Long countInstructors(boolean deleted);

    @Query("""
        SELECT COUNT(i.id) FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = ?1 AND i.deleted = ?2
    """)
    Long countInstructorsByActivity(String activityId, boolean deleted);
}

