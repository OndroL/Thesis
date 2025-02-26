package cz.inspire.utils.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.MyRepository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.QueryParam;
import cz.inspire.sport.entity.SportInstructorEntity;

import java.util.List;
import java.util.Optional;

@MyRepository
public interface SportInstructorTestRepository extends BaseRepository<SportInstructorEntity, String> {

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.deleted = false")
    List<SportInstructorEntity> findBySport(@QueryParam("sportId") String sportId);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.instructor.id = :instructorId AND s.deleted = false")
    List<SportInstructorEntity> findByInstructor(@QueryParam("instructorId") String instructorId);

    @Query("""
       SELECT s FROM SportInstructorEntity s
       WHERE s.sport.id = :sportId
         AND s.instructor.id = :instructorId
         AND s.deleted = false
       """)
    Optional<SportInstructorEntity> findBySportAndInstructor(@QueryParam("sportId") String sportId,
                                                   @QueryParam("instructorId") String instructorId);

    @Query("""
       SELECT s FROM SportInstructorEntity s
       WHERE s.sport.id = :sportId
         AND s.instructor.id = :instructorId
         AND s.deleted = false
       """)
    Optional<SportInstructorEntity> findBySportAndInstructorWithLimit(@QueryParam("sportId") String sportId,
                                                             @QueryParam("instructorId") String instructorId,
                                                             @Limit int limit,
                                                             @Offset int offset);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.instructor IS NULL AND s.deleted = false")
    Optional<SportInstructorEntity> findBySportWithoutInstructor(@QueryParam("sportId") String sportId);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.activityId = :activityId")
    List<SportInstructorEntity> findByActivity(@QueryParam("activityId") String activityId);

    @Query("SELECT COUNT(s.id) FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.deleted = false")
    Long countSportInstructors(@QueryParam("sportId") String sportId);
}

