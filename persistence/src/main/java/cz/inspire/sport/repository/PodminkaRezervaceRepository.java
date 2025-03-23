package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface PodminkaRezervaceRepository extends BaseRepository<PodminkaRezervaceEntity, String> {

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAllOrdered();

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findAll(@Limit int count, @Offset int offset);

    @Query("""
        SELECT p FROM PodminkaRezervaceEntity p
        WHERE p.objekt.id = :objektId
        ORDER BY p.priorita
    """)
    List<PodminkaRezervaceEntity> findByObjekt(String objektId, @Limit int count, @Offset int offset);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
        WHERE p.objekt.id = :objektId
    """)
    Long countAllByObject(String objektId);

    @Query("""
        SELECT COUNT(p.id) FROM PodminkaRezervaceEntity p
    """)
    Long countAll();

    @Query("""
        SELECT DISTINCT p.objekt.id FROM PodminkaRezervaceEntity p
        WHERE p.objektRezervaceId = :objektRezervaceId
    """)
    List<String> getObjectIdsByReservationConditionObject(String objektRezervaceId);

    @Query("""
        SELECT MAX(p.priorita) FROM PodminkaRezervaceEntity p
    """)
    Long getMaxPriority();
}
