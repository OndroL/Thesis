package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ArealEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface ArealRepository extends CrudRepository<ArealEntity, String> {

    @Query("SELECT a FROM ArealEntity a ORDER BY a.id")
    List<ArealEntity> findAllOrdered();

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
    List<ArealEntity> findByParent(String parentId, String jazyk, Limit limit);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal IS NULL AND loc.jazyk = ?1
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findRoot(String jazyk, Limit limit);

    @Query("""
        SELECT a FROM ArealEntity a
        WHERE a.id = ?1 AND FUNCTION('areal_isChild', ?2, ?1) = TRUE
    """)
    ArealEntity findIfChild(String childId, String parentId);

    @Query("SELECT a.id FROM ArealEntity a WHERE a.nadrazenyAreal.id = ?1")
    List<String> findArealIdsByParent(String arealId);

}

