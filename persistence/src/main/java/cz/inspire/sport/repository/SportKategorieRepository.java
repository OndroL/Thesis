package cz.inspire.sport.repository;

import cz.inspire.sport.entity.SportKategorieEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SportKategorieRepository extends CrudRepository<SportKategorieEntity, String> {

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
        WHERE s.nadrazenaKategorie.id = ?1
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAllByNadrazenaKategorie(String nadrazenaKategorieId);

    @Query("""
        SELECT s FROM SportKategorieEntity s
        JOIN s.localeData loc
        ORDER BY loc.nazev
    """)
    List<SportKategorieEntity> findAll(Limit limit);

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
