package cz.inspire.sms.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Date;

@ApplicationScoped
public class SMSHistoryService extends BaseService<SMSHistoryEntity, String, SMSHistoryRepository> {

    public SMSHistoryService () {
    }

    @Inject
    public SMSHistoryService (SMSHistoryRepository repository) {
        super(repository);
    }

    public List<SMSHistoryEntity> findByDate(Date from, Date to) { return repository.findByDate(from, to); }

    public List<SMSHistoryEntity> findByDateAutomatic(Date from, Date to, boolean automatic) {
        return repository.findByDateAutomatic(from, to, automatic);
    }
}
