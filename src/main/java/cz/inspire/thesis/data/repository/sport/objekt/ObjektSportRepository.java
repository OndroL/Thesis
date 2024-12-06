package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ObjektSportEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ObjektSportRepository extends EntityRepository<ObjektSportEntity, String> {

    @Query("""
        SELECT o FROM ObjektSportEntity o
        WHERE o.objekt.id = ?1
    """)
    List<ObjektSportEntity> findByObjekt(String objektId);
}
