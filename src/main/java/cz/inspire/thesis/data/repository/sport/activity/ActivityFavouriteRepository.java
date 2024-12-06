package cz.inspire.thesis.data.repository.sport.activity;

import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityFavouriteRepository extends EntityRepository<ActivityFavouriteEntity, String> {

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = ?1
        ORDER BY af.pocet DESC, af.datumPosledniZmeny DESC
    """)
    List<ActivityFavouriteEntity> findByZakaznik(String zakaznikId, @MaxResults int limit, @FirstResult int offset);

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = ?1 AND af.activityId = ?2
    """)
    Optional<ActivityFavouriteEntity> findByZakaznikAktivita(String zakaznikId, String activityId);
}
