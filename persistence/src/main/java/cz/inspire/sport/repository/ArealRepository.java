package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.ArealEntity;

import java.util.List;

@Repository
public interface ArealRepository extends BaseRepository<ArealEntity, String> {

    @Query("SELECT a FROM ArealEntity a ORDER BY a.id")
    List<ArealEntity> findAllOrdered();

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal.id = :parentId AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findByParent(String parentId, String jazyk);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal IS NULL AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findRoot(String jazyk);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal.id = :parentId AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findByParentWithLimit(String parentId, String jazyk, @Limit int count, @Offset int offset);

    @Query("""
        SELECT a FROM ArealEntity a
        JOIN a.localeData loc
        WHERE a.nadrazenyAreal IS NULL AND loc.jazyk = :jazyk
        ORDER BY loc.nazev
    """)
    List<ArealEntity> findRootWithLimit(String jazyk, @Limit int count, @Offset int offset);

    @Query("""
        SELECT a FROM ArealEntity a
        WHERE a.id = :childId AND FUNCTION('areal_isChild', :parentId, :childId) = TRUE
    """)
    ArealEntity findIfChild(String childId, String parentId);

    @Query("SELECT a.id FROM ArealEntity a WHERE a.nadrazenyAreal.id = :arealId")
    List<String> getArealIdsByParent(String arealId);

}

