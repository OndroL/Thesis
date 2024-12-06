package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ArealRepository extends EntityRepository<ArealEntity, String> {

    @Query("SELECT a FROM ArealEntity a ORDER BY a.id")
    List<ArealEntity> findAll();

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findByParent(String parentId, String jazyk);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findRoot(String jazyk);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findByParent(String parentId, String jazyk, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findRoot(String jazyk, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT a FROM ArealEntity a
        WHERE a.id = ?1 AND FUNCTION('areal_isChild', ?2, ?1) = TRUE
    """)
    ArealEntity findIfChild(String childId, String parentId);

    /**
     * Only for testing
     */
    ArealEntity findById(String areal1);
}

