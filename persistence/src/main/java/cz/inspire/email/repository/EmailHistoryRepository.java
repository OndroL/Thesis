package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EmailHistoryRepository extends BaseRepository<EmailHistoryEntity,String> {
    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAllOrdered(); // Renamed because of conflict with BaseRepository Stream findAll() func.

    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll(@Limit int count, @Offset int offset);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE (p.date >= :dateFrom) AND (p.date <= :dateTo) AND (p.sent = true) ORDER BY p.date DESC")
    List<EmailHistoryEntity> findByDate(Timestamp dateFrom, Timestamp dateTo, @Limit int count, @Offset int offset);
}
