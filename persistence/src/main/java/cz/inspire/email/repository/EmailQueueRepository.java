package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailQueueRepository extends BaseRepository<EmailQueueEntity,String> {

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created DESC")
    List<EmailQueueEntity> findAllOrdered(); // Renamed because of conflict with BaseRepository Stream findAll() func.

    @Query("SELECT p FROM EmailQueueEntity p ORDER BY p.created ASC")
    List<EmailQueueEntity> findAll(@Limit int count, @Offset int offset);

    @Query(value="SELECT p FROM EmailQueueEntity p ORDER BY priority DESC, created ASC")
    Optional<EmailQueueEntity> findFirstMail(@Limit int count); // in service set to 1

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.emailHistory = :historyId")
    List<EmailQueueEntity> findByHistory(String historyId);

    @Query("SELECT p FROM EmailQueueEntity p WHERE p.dependentEmailHistory = :historyId")
    List<EmailQueueEntity> findByDependentHistory(String historyId);
}
