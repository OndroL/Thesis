package cz.inspire.thesis.data.repository.sport.activity;

import cz.inspire.thesis.data.model.sport.activity.ActivityWebTabEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ActivityWebTabRepository extends EntityRepository<ActivityWebTabEntity, String> {

    @Query("SELECT aw FROM ActivityWebTabEntity aw")
    List<ActivityWebTabEntity> findAll();

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.sportId = ?1")
    List<ActivityWebTabEntity> findBySport(String sportId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.activityId = ?1")
    List<ActivityWebTabEntity> findByActivity(String activityId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.objectId = ?1")
    List<ActivityWebTabEntity> findByObject(String objectId);
}

