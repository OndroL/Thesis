package cz.inspire.sms.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Timestamp;
import java.util.List;
import java.util.Date;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class SMSHistoryService extends BaseService<SMSHistoryEntity, String, SMSHistoryRepository> {

    public SMSHistoryService () {
    }

    @Inject
    public SMSHistoryService (SMSHistoryRepository repository) {
        super(repository);
    }

    public List<SMSHistoryEntity> findByDate(Date from, Date to) throws FinderException {
        return wrapDBException(
                () -> repository.findByDate(new Timestamp(from.getTime()), new Timestamp(to.getTime())),
                "Error retrieving SMSHistoryEntity records by date range (from = " + from + ", to = " + to + ")"
        );
    }

    public List<SMSHistoryEntity> findByDateAutomatic(Date from, Date to, boolean automatic) throws FinderException {
        return wrapDBException(
                () -> repository.findByDateAutomatic(new Timestamp(from.getTime()), new Timestamp(to.getTime()), automatic),
                "Error retrieving SMSHistoryEntity records by date range and automatic flag (from = " + from + ", to = " + to + ", automatic = " + automatic + ")"
        );
    }
}
