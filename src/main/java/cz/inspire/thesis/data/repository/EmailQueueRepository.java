package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.EmailQueueEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailQueueRepository extends EntityRepository<EmailQueueEntity,String> {

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created DESC")
    List<EmailQueueEntity> findAll();

    /**
     * Are you in favor of this solution with Annotations of @FirstResult and @MaxResults,
     * or It should be also possible to use nativeQueries e.g. :
     * / @Query(value = "SELECT * FROM EmailQueueEntity ORDER BY created ASC LIMIT ?2 OFFSET ?1", nativeQuery = true);
     *  List<EmailQueueEntity> findAll(int offset, int count);
     */
    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created ASC")
    List<EmailQueueEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.priority DESC, p.created ASC")
    Optional<EmailQueueEntity> findFirstMail();

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.emailhistory = ?1")
    List<EmailQueueEntity> findByHistory(String historyId);

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.dependentemailhistory = ?1")
    List<EmailQueueEntity> findByDependentHistory(String historyId);
}
