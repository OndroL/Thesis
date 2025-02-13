package cz.inspire.sport.repository;

import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface PodminkaRezervaceRepository extends CrudRepository<PodminkaRezervaceEntity, String> {

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAllOrdered();

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAll(Limit limit);

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        WHERE p.objekt.id = ?1
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findByObjekt(String objektId, Limit limit);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
        WHERE p.objekt.id = ?1
    """)
    Long countAllByObject(String objektId);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
    """)
    Long countAll();

    @Query("""
        SELECT DISTINCT p.objekt.id FROM PodminkaRezervaceEntity p
        WHERE p.objektRezervaceId = ?1
    """)
    List<String> getObjectIdsByReservationConditionObject(String objektRezervaceId);

    @Query("""
        SELECT MAX(p.priorita) FROM PodminkaRezervaceEntity p
    """)
    Long getMaxPriority();
}
