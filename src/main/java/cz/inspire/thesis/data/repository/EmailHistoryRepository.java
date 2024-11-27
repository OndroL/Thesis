package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.EmailHistoryEntity;
import org.apache.deltaspike.data.api.*;

import java.util.Date;
import java.util.List;

@Repository
public interface EmailHistoryRepository extends EntityRepository<EmailHistoryEntity,String> {
    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll();

    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll(@FirstResult int offset, @MaxResults int count);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.sent = true) ORDER BY p.date DESC")
    List<EmailHistoryEntity> findByDate(Date dateFrom, Date dateTo, @FirstResult int offset, @MaxResults int count);
}
