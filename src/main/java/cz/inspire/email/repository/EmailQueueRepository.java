package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailQueueEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.FirstResult;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailQueueRepository extends EntityRepository<EmailQueueEntity,String> {

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created DESC")
    List<EmailQueueEntity> findAll();

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created ASC")
    List<EmailQueueEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query(value="SELECT p FROM EmailQueueEntity p ORDER BY priority DESC, created ASC",max=1)
    Optional<EmailQueueEntity> findFirstMail();

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.emailHistory = ?1")
    List<EmailQueueEntity> findByHistory(String historyId);

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.dependentEmailHistory = ?1")
    List<EmailQueueEntity> findByDependentHistory(String historyId);
}
