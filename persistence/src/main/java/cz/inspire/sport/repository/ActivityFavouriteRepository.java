package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.ActivityFavouriteEntity;

import java.util.List;

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
    ActivityFavouriteEntity findByZakaznikAktivita(String zakaznikId, String activityId);
}
