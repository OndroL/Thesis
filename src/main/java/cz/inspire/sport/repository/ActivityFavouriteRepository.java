package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ActivityFavouriteEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityFavouriteRepository extends CrudRepository<ActivityFavouriteEntity, String> {

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = ?1
        ORDER BY af.pocet DESC, af.datumPosledniZmeny DESC
    """)
    List<ActivityFavouriteEntity> findByZakaznik(String zakaznikId, Limit limit);

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = ?1 AND af.activityId = ?2
    """)
    Optional<ActivityFavouriteEntity> findByZakaznikAktivita(String zakaznikId, String activityId);
}
