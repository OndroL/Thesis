package cz.inspire.thesis.data.repository.sport.sport;

import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface SportRepository extends EntityRepository<SportEntity, String> {

    @Query("""
        SELECT s FROM SportEntity s
        ORDER BY s.id
    """)
    List<SportEntity> findAll();

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
    List<SportEntity> findByParent(String parentId, String jazyk, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie.id = ?1
        ORDER BY loc.nazev
    """)
    List<SportEntity> findByCategory(String kategorieId, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT s FROM SportEntity s
        WHERE s.zboziId = ?1
    """)
    List<SportEntity> findByZbozi(String zboziId, @FirstResult int offset, @MaxResults int count);

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
    List<SportEntity> findRoot(String jazyk, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT s FROM SportEntity s
        JOIN s.localeData loc
        WHERE s.sportKategorie IS NULL
        ORDER BY loc.nazev
    """)
    List<SportEntity> findCategoryRoot(@FirstResult int offset, @MaxResults int count);
}
