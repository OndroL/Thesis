package cz.inspire.sms.repository;

import cz.inspire.sms.entity.SMSHistoryEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface SMSHistoryRepository extends EntityRepository<SMSHistoryEntity, String> {
    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2)")
    List<SMSHistoryEntity> findByDate(Date dateFrom, Date dateTo);

    @Query("SELECT p from SMSHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.automatic = ?3)")
    List<SMSHistoryEntity> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic);
}