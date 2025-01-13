package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmailHistoryService extends BaseService<EmailHistoryEntity, EmailHistoryRepository> {

    @Inject
    public EmailHistoryService(Logger logger, EmailHistoryRepository repository) {
        super(logger, repository, EmailHistoryEntity.class);
    }

    public List<EmailHistoryEntity> findAll() { return repository.findAll(); }

    public List<EmailHistoryEntity> findAll(int offset, int count) { return repository.findAll(offset, count); }

    public List<EmailHistoryEntity> findByDate(Date dateFrom, Date dateTo, int offset, int count) {
        return repository.findByDate(dateFrom, dateTo, offset, count);
    }

    public Optional<EmailHistoryEntity> findById(String emailHistoryId) {return repository.findById(emailHistoryId); }

}
