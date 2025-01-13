package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailHistoryEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.FirstResult;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.MaxResults;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailHistoryRepository extends EntityRepository<EmailHistoryEntity,String> {
    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll();

    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.sent = true) ORDER BY p.date DESC")
    List<EmailHistoryEntity> findByDate(Date dateFrom, Date dateTo, @FirstResult int offset, @MaxResults int count);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE p.id = ?1")
    Optional<EmailHistoryEntity> findById(String emailHistoryId);
}
