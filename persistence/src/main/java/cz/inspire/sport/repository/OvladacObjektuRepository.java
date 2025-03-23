package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface OvladacObjektuRepository extends BaseRepository<OvladacObjektuEntity, String> {

    @Query("""
        SELECT o FROM OvladacObjektuEntity o
        WHERE o.idOvladace = :idOvladace
        ORDER BY o.id
    """)
    List<OvladacObjektuEntity> findWithOvladacObjektu(String idOvladace);

    @Query("""
        SELECT o FROM OvladacObjektuEntity o
        WHERE o.objektId = :objektId
        ORDER BY o.id
    """)
    List<OvladacObjektuEntity> findByObjekt(String objektId);
}
