package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.ExternalClientEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalClientRepository extends EntityRepository<ExternalClientEntity, String> {

    List<ExternalClientEntity> findAll();

    /// Here "limit 1" is replaced with param @MaxResult as LIMIT is not supported by queries directly,
    /// which will be set in Service layer to 1
    /// It shouldn't be required as clientId should be unique, but when tests and forced to return
    /// multiple Entities it throws Exception
    /// There is multiple workarounds but this seems most consistent with oldBeans
    @Query("""
            SELECT a FROM ExternalClientEntity a
            JOIN a.oAuth2ClientSetting oa
            WHERE oa.clientId = ?1
            """)
    Optional<ExternalClientEntity> findByOAuth2ClientId(String clientId, @MaxResults int setToOne);
}
