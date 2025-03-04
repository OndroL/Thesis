package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface SportKategorieRepository extends BaseRepository<SportKategorieEntity, String> {

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAllOrdered();

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenaKategorie IS NULL
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findRoot();

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenaKategorie.id = :nadrazenaKategorieId
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAllByNadrazenaKategorie(String nadrazenaKategorieId);

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAll(@Limit int count, @Offset int offset);

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        WHERE s.multiSportFacilityId = :multisportFacilityId
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAllByMultisportFacilityId(String multisportFacilityId);

    @Query("""
        SELECT COUNT(s.id) FROM SportKategorieEntity s
    """)
    Long count();

    @Query("""
        SELECT COUNT(s.id) FROM SportKategorieEntity s
        WHERE s.nadrazenaKategorie IS NULL
    """)
    Long countRoot();

    @Query("""
        SELECT COUNT(s.id) FROM SportKategorieEntity s
        WHERE s.nadrazenaKategorie.id = :nadrazenaKategorieId
    """)
    Long countByNadrazenaKategorie(String nadrazenaKategorieId);
}
