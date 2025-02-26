package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OvladacObjektuEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface OvladacObjektuRepository extends CrudRepository<OvladacObjektuEntity, String> {

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
