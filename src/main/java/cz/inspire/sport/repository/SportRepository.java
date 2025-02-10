package cz.inspire.sport.repository;

import cz.inspire.sport.entity.SportEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface SportRepository extends CrudRepository<SportEntity, String> {

    @Query("""
        SELECT s FROM SportEntity s
        ORDER BY s.id
    """)
    List<SportEntity> findAllOrdered();

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByParent(String parentId, String jazyk);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByParent(String parentId, String jazyk, Limit limit);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie.id = ?1
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByCategory(String kategorieId, Limit limit);

    @Query("""
        SELECT s FROM SportEntity s
        WHERE s.zboziId = ?1
    """)
    List<SportEntity> findByZbozi(String zboziId, Limit limit);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<SportEntity> findRoot(String jazyk);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<SportEntity> findRoot(String jazyk, Limit limit);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie IS NULL
        ORDER BY loc.nazev
    """)
    List<SportEntity> findCategoryRoot(Limit limit);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s WHERE s.sportKategorie IS NULL
    """)
    Long countCategoryRoot();

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s WHERE s.sportKategorie.id = ?1
    """)
    Long countAllByCategory(String categoryId);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NOT NULL AND s.nadrazenySport.id = ?1 AND loc.jazyk = ?2
    """)
    Long countAllByParentAndLanguage(String parentId, String language);

    @Query("""
        SELECT s.id FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NOT NULL AND s.nadrazenySport.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<String> getAllIdsByParentAndLanguage(String parentId, String language);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = ?1
    """)
    Long countRootByLanguage(String language);

    @Query("""
        SELECT s.id FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<String> getRootIdsByLanguage(String language);
}
