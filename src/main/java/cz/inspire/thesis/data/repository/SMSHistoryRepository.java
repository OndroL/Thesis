package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.SMSHistory;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SMSHistoryRepository extends EntityRepository<SMSHistory, String> {
    @Query("SELECT p from SMSHistory p WHERE (p.date >= ?1) AND (p.date <= ?2)")
    List<SMSHistory> findByDate(Date dateFrom, Date dateTo);
    @Query("SELECT p from SMSHistory p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.automatic = ?3)")
    List<SMSHistory> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic);
}
