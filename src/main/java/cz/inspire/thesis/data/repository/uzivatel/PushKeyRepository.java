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

    @Query("""
            SELECT a FROM PushKeyEntity a
            WHERE (a.key = ?1 OR a.key = CONCAT('android_', ?1) OR a.key = CONCAT('ios_', ?1))
            AND uzivatel_id IS NOT NULL
            """)
    Optional<PushKeyEntity> findByKey(String key);

}
