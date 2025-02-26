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
     * These method overrides are necessary because the entity uses an embedded primary key (`@EmbeddedId`).
     * <p>
     * The default `findById` and `deleteById` methods inherited from `CrudRepository` expect a simple
     * primary key field named `id`. However, in `ObjektSportEntity`, the primary key is an **embedded key**
     * (`@EmbeddedId ObjektSportPK embeddedId`), which Spring Data/Jakarta Data does not automatically recognize.
     * <p>
     * Without explicitly defining these methods, Spring Data will throw an error stating that it cannot find
     * a matching field named `id(this)` for the inherited methods.
     */
    @Override
    @Query("""
        SELECT o
        FROM ObjektSportEntity o
        WHERE o.embeddedId = :pk
    """)
    Optional<ObjektSportEntity> findById(@Param("pk") ObjektSportPK pk);

    @Override
    @Query("""
        DELETE
        FROM ObjektSportEntity o
        WHERE o.embeddedId = :pk
    """)
    void deleteById(@Param("pk") ObjektSportPK pk);

}
