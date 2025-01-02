package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelSessionTokenEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

@Repository
public interface UzivatelSessionTokenRepository extends EntityRepository<UzivatelSessionTokenEntity, String> {

    /// Here "limit 1" is replaced with param @MaxResult as LIMIT is not supported by queries directly,
    /// which will be set in Service layer to 1, because if it returns multiple Entities it throws Exception
    /// There is multiple workarounds but this seems most consistent with oldBeans
    @Query("""
            SELECT a FROM UzivatelSessionTokenEntity a
            WHERE a.username = ?1
            """)
    Optional<UzivatelSessionTokenEntity> findByUsername(String username, @MaxResults int setToOne);

    @Query("""
            SELECT COUNT(a.series) FROM UzivatelSessionTokenEntity a
            WHERE a.username = ?1
            """)
    Long countByUsername(String username);
}
