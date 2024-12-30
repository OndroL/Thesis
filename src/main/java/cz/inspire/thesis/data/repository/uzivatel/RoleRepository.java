package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.RoleEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends EntityRepository<RoleEntity, String> {
    @Query("""
            SELECT a FROM RoleEntity a
            ORDER BY a.nazev
            """)
    List<RoleEntity> findAll();
}
