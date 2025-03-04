package cz.inspire.sms.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SMSHistoryRepository extends BaseRepository<SMSHistoryEntity, String> {
    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= :dateFrom) AND (p.date <= :dateTo)")
    List<SMSHistoryEntity> findByDate(Timestamp dateFrom, Timestamp dateTo);

    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= :dateFrom) AND (p.date <= :dateTo) AND (p.automatic = :automatic)")
    List<SMSHistoryEntity> findByDateAutomatic(Timestamp dateFrom, Timestamp dateTo, boolean automatic);
}