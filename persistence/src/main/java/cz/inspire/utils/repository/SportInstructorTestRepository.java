package cz.inspire.utils.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.SportInstructorEntity;
import jakarta.annotation.Nullable;

import java.util.List;

@Repository
public interface SportInstructorTestRepository extends BaseRepository<SportInstructorEntity, String> {

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.deleted = false")
    List<SportInstructorEntity> findBySport(String sportId);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.instructor.id = :instructorId AND s.deleted = false")
    List<SportInstructorEntity> findByInstructor(@Nullable String instructorId);

    @Query("""
       SELECT s FROM SportInstructorEntity s
       WHERE s.sport.id = :sportId
         AND s.instructor.id = :instructorId
         AND s.deleted = false
       """)
    SportInstructorEntity findBySportAndInstructor(String sportId, @Nullable String instructorId);

    @Query("""
       SELECT s FROM SportInstructorEntity s
       WHERE s.sport.id = :sportId
         AND s.instructor.id = :instructorId
         AND s.deleted = false
       """)
    SportInstructorEntity findBySportAndInstructorWithLimit(@Nullable String sportId, String instructorId,
                                                                      @Limit int limit,
                                                                      @Offset int offset);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.instructor IS NULL AND s.deleted = false")
    SportInstructorEntity findBySportWithoutInstructor(String sportId);

    @Query("SELECT s FROM SportInstructorEntity s WHERE s.activityId = :activityId")
    List<SportInstructorEntity> findByActivity(String activityId);

    @Query("SELECT COUNT(s.id) FROM SportInstructorEntity s WHERE s.sport.id = :sportId AND s.deleted = false")
    Long countSportInstructors(String sportId);
}

