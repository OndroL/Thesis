package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ObjektSportEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportPK;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjektSportRepository extends EntityRepository<ObjektSportEntity, String> {

    @Query("""
        SELECT o FROM ObjektSportEntity o
        WHERE o.objekt.id = ?1
    """)
    List<ObjektSportEntity> findByObjekt(String objektId);

    // Additional finder for remove functionality based on ObjektSportPK
    Optional<ObjektSportEntity> findBy(ObjektSportPK id);
}
