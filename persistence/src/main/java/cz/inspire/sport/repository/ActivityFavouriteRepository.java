package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityFavouriteRepository extends BaseRepository<ActivityFavouriteEntity, String> {

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = :zakaznikId
        ORDER BY af.pocet DESC, af.datumPosledniZmeny DESC
    """)
    List<ActivityFavouriteEntity> findByZakaznik(String zakaznikId, @Limit int count, @Offset int offset);

    @Query("""
        SELECT af FROM ActivityFavouriteEntity af
        WHERE af.zakaznikId = :zakaznikId AND af.activityId = :activityId
    """)
    Optional<ActivityFavouriteEntity> findByZakaznikAktivita(String zakaznikId, String activityId);
}
