package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.SportInstructorEntity;

import java.util.List;

@Repository
public interface SportInstructorRepository extends BaseRepository<SportInstructorEntity, String> {

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = :sportId AND s.deleted = FALSE
    """)
    List<SportInstructorEntity> findBySport(String sportId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.instructor.id = :instructorId AND s.deleted = FALSE
    """)
    List<SportInstructorEntity> findByInstructor(String instructorId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = :sportId AND s.instructor.id = :instructorId AND s.deleted = FALSE
    """)
    SportInstructorEntity findBySportAndInstructor(String sportId, String instructorId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.sport.id = :sportId AND s.instructor IS NULL AND s.deleted = FALSE
    """)
    SportInstructorEntity findBySportWithoutInstructor(String sportId);

    @Query("""
        SELECT s FROM SportInstructorEntity s
        WHERE s.activityId = :activityId
    """)
    List<SportInstructorEntity> findByActivity(String activityId);

    @Query("""
        SELECT COUNT(s.id) FROM SportInstructorEntity s
        WHERE s.sport.id = :sportId AND s.deleted = FALSE
    """)
    Long countSportInstructors(String sportId);
}
