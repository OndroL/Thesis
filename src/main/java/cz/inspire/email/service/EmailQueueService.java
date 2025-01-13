package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.data.api.FirstResult;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmailQueueService extends BaseService<EmailQueueEntity, EmailQueueRepository> {

    @Inject
    public EmailQueueService(Logger logger, EmailQueueRepository repository) {
        super(logger, repository, EmailQueueEntity.class);
    }

    public List<EmailQueueEntity> findAll() { return repository.findAll(); }

    public List<EmailQueueEntity> findAll(@FirstResult int offset, @MaxResults int count) {
        return repository.findAll(offset, count);
    }

    public Optional<EmailQueueEntity> findFirstMail() { return repository.findFirstMail(); }

    public List<EmailQueueEntity> findByHistory(String historyId) { return repository.findByHistory(historyId); }

    public List<EmailQueueEntity> findByDependentHistory(String historyId){
        return repository.findByDependentHistory(historyId);
    }
}

