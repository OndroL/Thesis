package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuEntity;
import org.apache.deltaspike.data.api.*;

import java.util.Date;
import java.util.List;

@Repository
public interface OtviraciDobaObjektuRepository extends EntityRepository<OtviraciDobaObjektuEntity, String> {

    @Query("""
        SELECT o FROM OtviraciDobaObjektuEntity o
    """)
    List<OtviraciDobaObjektuEntity> findAll();

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
    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId, @FirstResult int offset, @MaxResults int count);

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
}
