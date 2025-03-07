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
}
