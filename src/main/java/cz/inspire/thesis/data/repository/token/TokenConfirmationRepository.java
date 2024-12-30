package cz.inspire.thesis.data.repository.token;

import cz.inspire.thesis.data.model.token.TokenConfirmationEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface TokenConfirmationRepository extends EntityRepository<TokenConfirmationEntity, String> {

    @Query("""
            SELECT a FROM TokenConfirmationEntity a
            WHERE a.cas >= ?1 AND a.cas <= ?2
            ORDER BY a.cas DESC
            """)
    List<TokenConfirmationEntity> findByDate(Date from, Date to);
}
