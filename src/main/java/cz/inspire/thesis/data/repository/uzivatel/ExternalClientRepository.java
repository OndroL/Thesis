package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.ExternalClientEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalClientRepository extends EntityRepository<ExternalClientEntity, String> {

    List<ExternalClientEntity> findAll();

    @Query("""
            SELECT a FROM ExternalClientEntity a
            JOIN a.oauth2ClientSetting oa
            WHERE oa.client_id = ?1
            LIMIT 1
            """)
    Optional<ExternalClientEntity> findByOAuth2ClientId(String clientId, setMaxResults(1));
}
