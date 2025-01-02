package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.PushKeyEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushKeyRepository extends EntityRepository<PushKeyEntity,String> {

    List<PushKeyEntity> findAll();

    /// Here I would think about changing it to List as it can return multiple entities with same suffix
    /// for example "android_key123" and "ios_key123" would both be returned and query will end up in Exception
    /// or the result could be limited to 1

    @Query("""
            SELECT a FROM PushKeyEntity a
            WHERE (a.key = ?1 OR a.key = CONCAT('android_', ?1) OR a.key = CONCAT('ios_', ?1))
            AND a.uzivatel IS NOT NULL
            """)
    Optional<PushKeyEntity> findByKey(String key);

}
