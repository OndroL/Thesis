package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtviraciDobaObjektuRepository extends CrudRepository<OtviraciDobaObjektuEntity, String> {
    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = ?1
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = ?1
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId, Limit limit);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = ?1 AND o.id.platnostOd <= ?2
        ORDER BY o.id.platnostOd DESC
    """)
    OtviraciDobaObjektuEntity findCurrent(String objektId, Date day);

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = ?1 AND o.id.platnostOd >= ?2
        ORDER BY o.id.platnostOd DESC
    """)
    List<OtviraciDobaObjektuEntity> findAfter(String objektId, Date day);

    @Query("""
        SELECT o.id.platnostOd FROM OtviraciDobaObjektuEntity o
        WHERE o.id.objektId = ?1 AND o.id.platnostOd <= ?2
        ORDER BY o.id.platnostOd DESC
    """)
    List<Date> getCurrentIdsByObjectAndDay(String objektId, Date day);


    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
        WHERE o.id = ?1
    """)
    Optional<OtviraciDobaObjektuEntity> findById(OtviraciDobaObjektuPK id);
}
