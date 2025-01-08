package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ObjektRepository extends EntityRepository<ObjektEntity, String> {

    @Query("SELECT o FROM ObjektEntity o ORDER BY o.id")
    List<ObjektEntity> findAll();

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
    List<ObjektEntity> findByAreal(String arealId, String jazyk, @FirstResult int offset, @MaxResults int count);

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
    List<ObjektEntity> findBaseByAreal(String arealId, String jazyk, @FirstResult int offset, @MaxResults int count);

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
    List<ObjektEntity> findByPrimyVstup(String jazyk, @FirstResult int offset, @MaxResults int count, boolean primyVstup);

    @Query("SELECT o.id FROM ObjektEntity o WHERE o.areal.id = ?1")
    List<String> findObjektIdsOfAreal(String arealId);

}
