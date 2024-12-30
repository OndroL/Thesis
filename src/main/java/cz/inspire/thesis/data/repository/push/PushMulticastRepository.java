package cz.inspire.thesis.data.repository.push;

import cz.inspire.thesis.data.model.push.PushMulticastEntity;
import org.apache.deltaspike.data.api.*;

import java.util.Collection;
import java.util.Date;

@Repository
public interface PushMulticastRepository extends EntityRepository<PushMulticastEntity,String>{
    @Query("""
        SELECT a FROM PushMulticastEntity a
        WHERE (a.date >= ?1) AND (a.date <= ?2)
        AND (a.sent = TRUE) AND (?3 IS NULL OR a.automatic = ?3)
        ORDER BY a.date DESC
    """)
    Collection<PushMulticastEntity> findByDateAutomatic(Date dateFrom, Date dateTo, Boolean automatic, @FirstResult int offset, @MaxResults int count);

    @Query("""
        SELECT a FROM PushMulticastEntity a
        JOIN PushHistoryEntity ph ON a.id = ph.multicastId
        WHERE ph.uzivatelId = ?4
        AND (a.date >= ?1) AND (a.date <= ?2)
        AND (a.sent = TRUE) AND (?3 IS NULL OR a.automatic = ?3)
        ORDER BY a.date DESC
    """)
    Collection<PushMulticastEntity>findByUzivatelDateAutomatic(Date dateFrom, Date dateTo, Boolean automatic, String uzivatelId, @FirstResult int offset, @MaxResults int count);
}
