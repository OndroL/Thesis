package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface UzivatelRepository extends EntityRepository<UzivatelEntity, String> {

    List<UzivatelEntity> findAll();

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.skupina = ?1
            """)
    List<UzivatelEntity> findBySkupina(String skupinaId);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.aktivni = ?1
            """)
    List<UzivatelEntity> findByAktivni(Boolean aktivni);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.auth_key = ?1
            LIMIT 1
            """)
    Optional<UzivatelEntity> findByAuthKey(String authKey);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.aktivacnitoke = ?1
            """)
    Optional<UzivatelEntity> findByAktivacniToken(String aktivacniToken);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.skupina.id <> 'web'
            """)
    List<UzivatelEntity> findAllNotWeb();

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.skupina = ?1
            """)
    List<UzivatelEntity> findBySkupina(String skupinaId, @FirstResult int offset, @MaxResults int count);
}
