package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailQueueEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailQueueRepository extends CrudRepository<EmailQueueEntity,String> {

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created DESC")
    List<EmailQueueEntity> findAllOrdered();

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created ASC")
    List<EmailQueueEntity> findAll(Limit limit);

    @Query(value="SELECT p FROM EmailQueueEntity p ORDER BY priority DESC, created ASC")
    Optional<EmailQueueEntity> findFirstMail(Limit limit); // in service set to 1

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.emailHistory = ?1")
    List<EmailQueueEntity> findByHistory(String historyId);

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.dependentEmailHistory = ?1")
    List<EmailQueueEntity> findByDependentHistory(String historyId);
}
