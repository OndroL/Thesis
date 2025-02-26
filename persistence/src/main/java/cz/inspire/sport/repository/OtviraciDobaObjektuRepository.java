package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtviraciDobaObjektuRepository extends CrudRepository<OtviraciDobaObjektuEntity, OtviraciDobaObjektuPK> {

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
    List<OtviraciDobaObjektuEntity> findByObjektWithLimit(String objektId, Limit limit);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId.objektId = :objektId AND o.embeddedId.platnostOd <= :day
        ORDER BY o.embeddedId.platnostOd DESC
    """)
    Optional<OtviraciDobaObjektuEntity> findCurrent(String objektId, LocalDateTime day, Limit limit);

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
    @Override
    @Query("""
        SELECT o
        FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId = :pk
    """)
    Optional<OtviraciDobaObjektuEntity> findById(@Param("pk") OtviraciDobaObjektuPK pk);

    @Override
    @Query("""
        DELETE
        FROM OtviraciDobaObjektuEntity o
        WHERE o.embeddedId = :pk
    """)
    void deleteById(@Param("pk") OtviraciDobaObjektuPK pk);
}
