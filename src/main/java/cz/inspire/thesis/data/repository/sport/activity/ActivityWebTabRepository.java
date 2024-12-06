package cz.inspire.thesis.data.repository.sport.activity;

import cz.inspire.thesis.data.model.sport.activity.ActivityWebTabEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ActivityWebTabRepository extends EntityRepository<ActivityWebTabEntity, String> {

    /**
     * This declaration of query is not necessary here, if it was deleted calling findAll() on this repo
     * would have the same result ... Only kept for consistency with old bean
     */
    @Query("SELECT aw FROM ActivityWebTabEntity aw")
    List<ActivityWebTabEntity> findAll();

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.sportId = ?1")
    List<ActivityWebTabEntity> findBySport(String sportId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.activityId = ?1")
    List<ActivityWebTabEntity> findByActivity(String activityId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.objectId = ?1")
    List<ActivityWebTabEntity> findByObject(String objectId);
}

