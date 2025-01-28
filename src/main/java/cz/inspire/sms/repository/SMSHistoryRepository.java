package cz.inspire.sms.repository;

import cz.inspire.sms.entity.SMSHistoryEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface SMSHistoryRepository extends CrudRepository<SMSHistoryEntity, String> {
    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2)")
    List<SMSHistoryEntity> findByDate(Date dateFrom, Date dateTo);

    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.automatic = ?3)")
    List<SMSHistoryEntity> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic);
}