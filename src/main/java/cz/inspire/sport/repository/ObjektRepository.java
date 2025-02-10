package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ObjektEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import java.util.List;

@Repository
public interface ObjektRepository extends CrudRepository<ObjektEntity, String> {

    @Query("SELECT o FROM ObjektEntity o ORDER BY o.id")
    List<ObjektEntity> findAllOrdered();

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE o.areal.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findByAreal(String arealId, String jazyk);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE o.areal.id = ?1 AND loc.jazyk = ?2 AND FUNCTION('is_base_objekt', o.id) = TRUE
              AND o.primyVstup = FALSE
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findBaseByAreal(String arealId, String jazyk);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE o.areal.id = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findByAreal(String arealId, String jazyk, Limit limit);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE o.typRezervace = ?1 AND loc.jazyk = ?2
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findByTypRezervace(Integer typRezervace, String jazyk);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE o.areal.id = ?1 AND loc.jazyk = ?2 AND FUNCTION('is_base_objekt', o.id) = TRUE
              AND o.primyVstup = FALSE
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findBaseByAreal(String arealId, String jazyk, Limit limit);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        JOIN o.objektSports sport
        WHERE loc.jazyk = ?2 AND sport.sport.id = ?1
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findBySport(String sportId, String jazyk);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE loc.jazyk = ?1 AND o.primyVstup = ?2
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findByPrimyVstup(String jazyk, boolean primyVstup);

    @Query("""
        SELECT o FROM ObjektEntity o
        JOIN o.localeData loc
        WHERE loc.jazyk = ?1 AND o.primyVstup = ?2
        ORDER BY loc.nazev
    """)
    List<ObjektEntity> findByPrimyVstup(String jazyk, Limit limit, boolean primyVstup);

    @Query("SELECT o.id FROM ObjektEntity o WHERE o.areal.id = ?1")
    List<String> findObjektIdsOfAreal(String arealId);

}
