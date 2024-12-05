package cz.inspire.thesis.data.repository.sport.activity;

import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ActivityRepository extends EntityRepository<ActivityEntity, String> {

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAll();

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT a FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = ?1
        ORDER BY a.index ASC
    """)
    List<ActivityEntity> findAllByInstructor(String instructorId, @FirstResult int offset, @MaxResults int count);

    @Query("SELECT COUNT(a.id) FROM ActivityEntity a")
    Long countActivities();

    @Query("""
        SELECT COUNT(a.id) FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = ?1
    """)
    Long countActivitiesByInstructor(String instructorId);
}
