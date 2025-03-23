package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;

import java.util.List;

@Repository
public interface ObjektSportRepository extends BaseRepository<ObjektSportEntity, ObjektSportPK> {

    @Query("""
        SELECT o FROM ObjektSportEntity o
        WHERE o.objekt.id = :objektId
    """)
    List<ObjektSportEntity> findByObjekt(String objektId);
}
