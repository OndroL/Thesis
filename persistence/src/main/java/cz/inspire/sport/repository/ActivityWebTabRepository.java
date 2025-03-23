package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface ActivityWebTabRepository extends BaseRepository<ActivityWebTabEntity, String> {

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.sportId = :sportId")
    List<ActivityWebTabEntity> findBySport(String sportId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.activityId = :activityId")
    List<ActivityWebTabEntity> findByActivity(String activityId);

    @Query("SELECT aw FROM ActivityWebTabEntity aw WHERE aw.objectId = :objectId")
    List<ActivityWebTabEntity> findByObject(String objectId);
}

