package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjektSportRepository extends CrudRepository<ObjektSportEntity, String> {

    @Query("""
        SELECT o FROM ObjektSportEntity o
        WHERE o.objekt.id = ?1
    """)
    List<ObjektSportEntity> findByObjekt(String objektId);

    @Find
    Optional<ObjektSportEntity> findBy(ObjektSportPK id);
}
