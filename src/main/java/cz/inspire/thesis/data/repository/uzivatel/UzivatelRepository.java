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
            WHERE a.skupina.id = ?1
            """)
    List<UzivatelEntity> findBySkupina(String skupinaId);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.aktivni = ?1
            """)
    List<UzivatelEntity> findByAktivni(Boolean aktivni);

    /// Here "limit 1" is replaced with param @MaxResult as LIMIT is not supported by queries directly,
    /// which will be set in Service layer to 1, because if it returns multiple Entities it throws Exception
    /// There is multiple workarounds but this seems most consistent with oldBeans
    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.authKey = ?1
            """)
    Optional<UzivatelEntity> findByAuthKey(String authKey, @MaxResults int setToOne);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.aktivacniToken = ?1
            """)
    Optional<UzivatelEntity> findByAktivacniToken(String aktivacniToken);

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.skupina.id <> 'web'
            """)
    List<UzivatelEntity> findAllNotWeb();

    @Query("""
            SELECT a FROM UzivatelEntity a
            WHERE a.skupina.id = ?1
            """)
    List<UzivatelEntity> findBySkupina(String skupinaId, @FirstResult int offset, @MaxResults int count);
}
