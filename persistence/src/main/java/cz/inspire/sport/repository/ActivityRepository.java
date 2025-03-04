package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseRepository<ActivityEntity, String> {

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAllOrdered();

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAll(@Limit int count, @Offset int offset);

    @Query("""
        SELECT a FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = :instructorId
        ORDER BY a.index ASC
    """)
    List<ActivityEntity> findAllByInstructor(String instructorId, @Limit int count, @Offset int offset);

    @Query("SELECT COUNT(a.id) FROM ActivityEntity a")
    Long countActivities();

    @Query("""
        SELECT COUNT(a.id) FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = :instructorId
    """)
    Long countActivitiesByInstructor(String instructorId);
}
