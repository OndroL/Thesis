package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class EmailQueueService extends BaseService<EmailQueueEntity, String, EmailQueueRepository> {

    public EmailQueueService() {
    }

    @Inject
    public EmailQueueService(EmailQueueRepository repository) {
        super(repository);
    }

    public List<EmailQueueEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all EmailQueueEntity records, Ordered by created"
        );
    }

    public List<EmailQueueEntity> findAll(int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(new Limit(count, offset + 1)),
                "Error retrieving paginated EmailQueueEntity records (offset = " + offset + ", count = " + count + ")"
        );
    }

    public Optional<EmailQueueEntity> findFirstMail() throws FinderException {
        return wrapDBException(
                () -> repository.findFirstMail(Limit.of(1)),
                "Error retrieving the first email from EmailQueueEntity"
        );
    }

    public List<EmailQueueEntity> findByHistory(String historyId) throws FinderException {
        return wrapDBException(
                () -> repository.findByHistory(historyId),
                "Error retrieving EmailQueueEntity by history (historyId = " + historyId + ")"
        );
    }

    public List<EmailQueueEntity> findByDependentHistory(String historyId) throws FinderException {
        return wrapDBException(
                () -> repository.findByDependentHistory(historyId),
                "Error retrieving EmailQueueEntity by dependent history (historyId = " + historyId + ")"
        );
    }
}

