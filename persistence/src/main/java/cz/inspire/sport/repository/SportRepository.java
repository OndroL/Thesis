package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Limit;

import java.util.List;

@Repository
public interface SportRepository extends BaseRepository<SportEntity, String> {

    @Query("""
        SELECT s FROM SportEntity s
        ORDER BY s.id
    """)
    List<SportEntity> findAllOrdered();

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport.id = :parentId AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByParent(String parentId, String jazyk);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport.id = :parentId AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByParentWithLimit(String parentId, String jazyk, @Limit int count, @Offset int offset);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie.id = :kategorieId
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByCategory(String kategorieId, @Limit int count, @Offset int offset);

    @Query("""
        SELECT s FROM SportEntity s
        WHERE s.zboziId = :zboziId
    """)
    List<SportEntity> findByZbozi(String zboziId, @Limit int count, @Offset int offset);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<SportEntity> findRoot(String jazyk);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<SportEntity> findRootWithLimit(String jazyk, @Limit int count, @Offset int offset);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie IS NULL
        ORDER BY loc.nazev
    """)
    List<SportEntity> findCategoryRoot(@Limit int count, @Offset int offset);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s WHERE s.sportKategorie IS NULL
    """)
    Long countCategoryRoot();

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s WHERE s.sportKategorie.id = :categoryId
    """)
    Long countAllByCategory(String categoryId);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NOT NULL AND s.nadrazenySport.id = :parentId AND loc.jazyk = :language
    """)
    Long countAllByParentAndLanguage(String parentId, String language);

    @Query("""
        SELECT s.id FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NOT NULL AND s.nadrazenySport.id = :parentId AND loc.jazyk = :language
        ORDER BY loc.nazev
    """)
    List<String> getAllIdsByParentAndLanguage(String parentId, String language);

    @Query("""
        SELECT COUNT(s.id) FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = :language
    """)
    Long countRootByLanguage(String language);

    @Query("""
        SELECT s.id FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.nadrazenySport IS NULL AND loc.jazyk = :language
        ORDER BY loc.nazev
    """)
    List<String> getRootIdsByLanguage(String language);
}
