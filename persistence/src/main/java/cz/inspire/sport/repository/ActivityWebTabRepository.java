package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ActivityWebTabEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface ActivityWebTabRepository extends CrudRepository<ActivityWebTabEntity, String> {

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.sportId = ?1")
    List<ActivityWebTabEntity> findBySport(String sportId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.activityId = ?1")
    List<ActivityWebTabEntity> findByActivity(String activityId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.objectId = ?1")
    List<ActivityWebTabEntity> findByObject(String objectId);
}

