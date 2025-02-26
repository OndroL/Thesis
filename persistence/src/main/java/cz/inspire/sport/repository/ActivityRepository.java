package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ActivityEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.Limit;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<ActivityEntity, String> {

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAllOrdered();

    @Query("SELECT a FROM ActivityEntity a ORDER BY a.index ASC")
    List<ActivityEntity> findAll(Limit limit);

    @Query("""
        SELECT a FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = ?1
        ORDER BY a.index ASC
    """)
    List<ActivityEntity> findAllByInstructor(String instructorId, Limit limit);

    @Query("SELECT COUNT(a.id) FROM ActivityEntity a")
    Long countActivities();

    @Query("""
        SELECT COUNT(a.id) FROM ActivityEntity a
        JOIN a.instructors i
        WHERE i.id = ?1
    """)
    Long countActivitiesByInstructor(String instructorId);
}
