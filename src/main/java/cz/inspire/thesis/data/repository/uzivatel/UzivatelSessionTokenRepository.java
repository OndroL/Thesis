package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelSessionTokenEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

@Repository
public interface UzivatelSessionTokenRepository extends EntityRepository<UzivatelSessionTokenEntity, String> {

    @Query("""
            SELECT a FROM UzivatelSessionTokenEntity a
            WHERE a.username = ?1
            LIMIT 1
            """)
    Optional<UzivatelSessionTokenEntity> findByUsername(String username);

    @Query("""
            SELECT COUNT(a.series) FROM UzivatelSessionTokenEntity a
            WHERE a.username = ?1
            """)
    Long countByUsername(String username);
}
