package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.PodminkaRezervaceEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface PodminkaRezervaceRepository extends EntityRepository<PodminkaRezervaceEntity, String> {

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAll();

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        WHERE p.objektId.id = ?1
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findByObjekt(String objektId, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
        WHERE p.objektId.id = ?1
    """)
    Long countAllByObject(String objektId);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
    """)
    Long countAll();

    @Query("""
        SELECT DISTINCT p.objektId.id FROM PodminkaRezervaceEntity p
        WHERE p.objektRezervaceId = ?1
    """)
    List<String> getObjectIdsByReservationConditionObject(String objektRezervaceId);

    @Query("""
        SELECT MAX(p.priorita) FROM PodminkaRezervaceEntity p
    """)
    Long getMaxPriority();
}
