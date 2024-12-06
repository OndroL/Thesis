package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.OvladacObjektuEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface OvladacObjektuRepository extends EntityRepository<OvladacObjektuEntity, String> {

    @Query("""
        SELECT o FROM OvladacObjektuEntity o
        WHERE o.idOvladace = ?1
        ORDER BY o.id
    """)
    List<OvladacObjektuEntity> findWithOvladacObjektu(String idOvladace);

    @Query("""
        SELECT o FROM OvladacObjektuEntity o
        WHERE o.objektId = ?1
        ORDER BY o.id
    """)
    List<OvladacObjektuEntity> findByObjekt(String objektId);
}
