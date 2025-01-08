package cz.inspire.thesis.data.repository.sport.sport;

import cz.inspire.thesis.data.model.sport.sport.SportKategorieEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface SportKategorieRepository extends EntityRepository<SportKategorieEntity, String> {

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAll();

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
        WHERE s.nadrazenaKategorie.id = ?1
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAllByNadrazenaKategorie(String nadrazenaKategorieId);

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        WHERE s.multiSportFacilityId = ?1
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
        WHERE s.nadrazenaKategorie.id = ?1
    """)
    Long countByNadrazenaKategorie(String nadrazenaKategorieId);
}
