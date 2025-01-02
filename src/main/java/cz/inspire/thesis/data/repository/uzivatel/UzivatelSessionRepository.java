package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelSessionEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UzivatelSessionRepository extends EntityRepository<UzivatelSessionEntity, String> {

    @Query
    List<UzivatelSessionEntity> findAll();

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            WHERE a.loginTime IN ( SELECT MAX(a.loginTime) FROM UzivatelSessionEntity a WHERE a.uzivatel.login = ?1)
            """)
    Optional<UzivatelSessionEntity> findLast(String login);

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            WHERE a.loginTime IN ( SELECT MAX(a.loginTime) FROM UzivatelSessionEntity a WHERE a.uzivatel.login = ?1 AND a.loginType = 3)
            """)
    Optional<UzivatelSessionEntity> findLastRestLogin(String login);

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            JOIN a.uzivatel uz
            WHERE (a.loginTime <= ?2) AND (?1 <= a.logoutTime OR a.logoutTime IS NULL)
            """)
    List<UzivatelSessionEntity> findAllInDates(Date from, Date to);

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            JOIN a.uzivatel uz
            WHERE uz.skupina.id != 'web' AND ((a.loginTime <= ?2) AND (?1 <= a.logoutTime OR (logoutTime IS NULL))) AND uz.login = ?3
            """)
    List<UzivatelSessionEntity> findAllInDatesWithLogin(Date from, Date to, String loginName);

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            JOIN a.uzivatel uz
            WHERE a.logoutTime IS NULL
            """)
    List<UzivatelSessionEntity> findLogged();

    @Query("""
            SELECT a FROM UzivatelSessionEntity a
            JOIN a.uzivatel uz
            WHERE a.loginTime IS NULL AND a.loginType = ?1 AND uz.login <> 'admin'
            """)
    List<UzivatelSessionEntity> findLogged(int type);

    @Query("""
            SELECT COUNT(a.id) FROM UzivatelSessionEntity a
            WHERE a.loginTime > ?1
            """)
    Long countNewerSessions(Date date);
}
