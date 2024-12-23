package cz.inspire.thesis.data.repository.sport.sport;

import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportInstructorRepository extends EntityRepository<SportInstructorEntity, String> {

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = ?1 AND s.deleted = FALSE
    """)
    List<SportInstructorEntity> findBySport(String sportId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.instructor.id = ?1 AND s.deleted = FALSE
    """)
    List<SportInstructorEntity> findByInstructor(String instructorId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = ?1 AND s.instructor.id = ?2 AND s.deleted = FALSE
    """)
    Optional<SportInstructorEntity> findBySportAndInstructor(String sportId, String instructorId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = ?1 AND s.instructor IS NULL AND s.deleted = FALSE
    """)
    Optional<SportInstructorEntity> findBySportWithoutInstructor(String sportId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.activityId = ?1
    """)
    List<SportInstructorEntity> findByActivity(String activityId);

    @Query("""
        SELECT COUNT(s.id) FROM SportInstructorEntity s
        WHERE s.sport.id = ?1 AND s.deleted = FALSE
    """)
    Long countSportInstructors(String sportId);
}
