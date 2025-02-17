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

    @Query("SELECT o FROM OtviraciDobaObjektuEntity o ORDER BY o.id.objektId")
    List<OtviraciDobaObjektuEntity> findAllOrdered();

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = :objektId
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = :objektId
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjektWithLimit(String objektId, Limit limit);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = :objektId AND o.id.platnostOd <= :day
        ORDER BY o.id.platnostOd DESC
    """)
    Optional<OtviraciDobaObjektuEntity> findCurrent(String objektId, LocalDateTime day, Limit limit);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = :objektId AND o.id.platnostOd >= :day
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findAfter(String objektId, LocalDateTime day);

    @Query("""
        SELECT o.id.platnostOd FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = :objektId AND o.id.platnostOd <= :day
        ORDER BY o.id.platnostOd DESC
    """)
    List<LocalDateTime> getCurrentIdsByObjectAndDay(String objektId, LocalDateTime day);

    /**
     * This to methods below are necessary because of Embedded pk
     */
    @Override
    @Query("""
        SELECT o
        FROM OtviraciDobaObjektuEntity o
        WHERE o.id = :pk
    """)
    Optional<OtviraciDobaObjektuEntity> findById(@Param("pk") OtviraciDobaObjektuPK pk);

    @Override
    @Query("""
        DELETE
        FROM OtviraciDobaObjektuEntity o
        WHERE o.id = :pk
    """)
    void deleteById(@Param("pk") OtviraciDobaObjektuPK pk);
}
