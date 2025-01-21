package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import jakarta.data.Limit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmailQueueService extends BaseService<EmailQueueEntity, String, EmailQueueRepository> {

    public EmailQueueService() {
    }

    @Inject
    public EmailQueueService(EmailQueueRepository repository) {
        super(repository);
    }

    public List<EmailQueueEntity> findAll() { return repository.findAllOrdered(); }

    public List<EmailQueueEntity> findAll(int offset, int count) {
        return repository.findAll(new Limit(count, offset));
    }

    public Optional<EmailQueueEntity> findFirstMail() { return repository.findFirstMail(Limit.of(1)); }

    public List<EmailQueueEntity> findByHistory(String historyId) { return repository.findByHistory(historyId); }

    public List<EmailQueueEntity> findByDependentHistory(String historyId){
        return repository.findByDependentHistory(historyId);
    }
}

