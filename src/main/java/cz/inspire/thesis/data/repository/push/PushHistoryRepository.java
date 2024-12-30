package cz.inspire.thesis.data.repository.push;

import cz.inspire.thesis.data.model.push.PushHistoryEntity;
import org.apache.deltaspike.data.api.*;

import java.util.Collection;
import java.util.Date;

@Repository
public interface PushHistoryRepository extends EntityRepository<PushHistoryEntity, String> {
    @Query("""
        SELECT a FROM PushHistoryEntity a
        WHERE (a.uzivatelId = ?1) AND (a.multicastId = ?2)
    """)
    Collection<PushHistoryEntity> findByMulticastIdUzivatel(String uzivatelId, String multicastId);

    @Query("""
        SELECT a FROM PushHistoryEntity a
        JOIN PushMulticastEntity pm ON a.multicastId = pm.id
        WHERE (a.uzivatelId = ?1) AND (a.removed = ?2) AND (pm.sent = TRUE) AND (pm.date > ?3)
        ORDER BY a.read ASC, pm.date DESC
    """)
    Collection<PushHistoryEntity>findSuccessfulSentByUzivatel(String uzivatelId, Boolean removed, Date lastDate, @FirstResult int offset, @MaxResults int count);
}
