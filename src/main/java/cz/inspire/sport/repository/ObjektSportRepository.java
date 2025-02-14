package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjektSportRepository extends CrudRepository<ObjektSportEntity, ObjektSportPK> {

    @Query("""
        SELECT o FROM ObjektSportEntity o
        WHERE o.objekt.id = ?1
    """)
    List<ObjektSportEntity> findByObjekt(String objektId);

    /**
     * This to methods below are necessary because of Embedded pk
     */
    @Override
    @Query("""
        SELECT o
        FROM ObjektSportEntity o
        WHERE o.embeddedPK = :pk
    """)
    Optional<ObjektSportEntity> findById(@Param("pk") ObjektSportPK pk);

    @Override
    @Query("""
        DELETE
        FROM ObjektSportEntity o
        WHERE o.embeddedPK = :pk
    """)
    void deleteById(@Param("pk") ObjektSportPK pk);
}
