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

    /**
     * These method overrides are necessary because the entity uses an embedded primary key (`@EmbeddedId`).
     * <p>
     * The default `findById` and `deleteById` methods inherited from `CrudRepository` expect a simple
     * primary key field named `id`. However, in `ObjektSportEntity`, the primary key is an **embedded key**
     * (`@EmbeddedId ObjektSportPK embeddedId`), which Spring Data/Jakarta Data does not automatically recognize.
     * <p>
     * Without explicitly defining these methods, Spring Data will throw an error stating that it cannot find
     * a matching field named `id(this)` for the inherited methods.
     */
//    @Override
//    @Query("""
//        SELECT o
//        FROM ObjektSportEntity o
//        WHERE o.embeddedId = :pk
//    """)
//    ObjektSportEntity findById(@Param("pk") ObjektSportPK pk);
//
//    @Query("""
//        DELETE
//        FROM ObjektSportEntity o
//        WHERE o.embeddedId = :pk
//    """)
//    void deleteById(@Param("pk") ObjektSportPK pk);

}
