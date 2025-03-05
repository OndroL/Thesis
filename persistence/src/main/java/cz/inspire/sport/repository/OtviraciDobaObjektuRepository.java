package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OtviraciDobaObjektuRepository extends BaseRepository<OtviraciDobaObjektuEntity, OtviraciDobaObjektuPK> {

    @Query("SELECT o FROM OtviraciDobaObjektuEntity o ORDER BY o.embeddedId.objektId")
    List<OtviraciDobaObjektuEntity> findAllOrdered();

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjektWithLimit(String objektId, @Limit int count, @Offset int offset);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId AND o.embeddedId.platnostOd <= :day
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    OtviraciDobaObjektuEntity findCurrent(String objektId, LocalDateTime day, @Limit int count);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId AND o.embeddedId.platnostOd >= :day
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findAfter(String objektId, LocalDateTime day);

    @Query("""
        SELECT o.embeddedId.platnostOd FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId AND o.embeddedId.platnostOd <= :day
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    List<LocalDateTime> getCurrentIdsByObjectAndDay(String objektId, LocalDateTime day);

    /**
     * These method overrides are necessary because the entity uses an embedded primary key (`@EmbeddedId`).
     * <p>
     * The default `findById` and `deleteById` methods inherited from `CrudRepository` expect a simple
     * primary key field named `id`. However, in `ObjektSportEntity`, the primary key is an **embedded key**
     * (`@EmbeddedId OtviraciDobaObjektuEntity embeddedId`), which Spring Data/Jakarta Data does not automatically recognize.
     * <p>
     * Without explicitly defining these methods, Spring Data will throw an error stating that it cannot find
     * a matching field named `id(this)` for the inherited methods.
     */
//    @Override
//    @Query("""
//        SELECT o
//        FROM OtviraciDobaObjektuEntity o
//        WHERE o.embeddedId = :pk
//    """)
//    OtviraciDobaObjektuEntity findById(@Param("pk") OtviraciDobaObjektuPK pk);
//
//    @Query("""
//        DELETE
//        FROM OtviraciDobaObjektuEntity o
//        WHERE o.embeddedId = :pk
//    """)
//    void deleteById(@Param("pk") OtviraciDobaObjektuPK pk);
}
