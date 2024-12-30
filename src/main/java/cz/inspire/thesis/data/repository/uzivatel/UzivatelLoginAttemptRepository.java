package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelLoginAttemptEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UzivatelLoginAttemptRepository extends EntityRepository<UzivatelLoginAttemptEntity, String> {

    List<UzivatelLoginAttemptEntity> findAll();

    @Query("""
            SELECT a FROM UzivatelLoginAttemptEntity a
            WHERE a.login = ?1 AND a.ip = ?2
            ORDER BY a.created DESC
            LIMIT 1
            """)
    Optional<UzivatelLoginAttemptEntity> findLastByLoginAndIp(String login, String ip);

    @Query("""
            SELECT a FROM UzivatelLoginAttemptEntity a
            WHERE a.login > ?1 AND a.ip = ?2 and a.created > ?3
            ORDER BY a.created DESC
            """)
    List<UzivatelLoginAttemptEntity> findByLoginAndIpFromRecentTime(String login, String ip, Date referenceTime);

    @Query("""
            SELECT a FROM UzivatelLoginAttemptEntity a
            WHERE a.created < ?1 AND (a.blockedTillTime IS NULL)
            """)
    List<UzivatelLoginAttemptEntity> findAttemptsOnlyOlderThan(Date referenceTime);

    @Query("""
            SELECT a FROM UzivatelLoginAttemptEntity a
            WHERE a.blockedTillTime < ?1
            """)
    List<UzivatelLoginAttemptEntity> findBlockingOlderThan(Date referenceTime);


    @Query("""
            SELECT COUNT(a) FROM UzivatelLoginAttemptEntity a
            WHERE a.login = ?1 AND a.ip = ?2 AND a.created > ?3
            """)
    Long countLoginAttemptsInTime(String login, String ip, Date backInTime);
}
