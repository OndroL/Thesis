package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.EmailQueueDetails;
import cz.inspire.thesis.data.model.EmailQueueEntity;
import cz.inspire.thesis.data.repository.EmailQueueRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class EmailQueueService {

    @Inject
    private EmailQueueRepository emailQueueRepository;

    public String ejbCreate(EmailQueueDetails details) throws CreateException {
        try {
            EmailQueueEntity entity = new EmailQueueEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setCreated(details.getCreated());
            entity.setEmailHistory(details.getEmailHistory());
            entity.setRecipient(details.getRecipient());
            entity.setPriority(details.getPriority());
            entity.setRemoveEmailHistory(details.isRemoveEmailHistory());
            entity.setDependentEmailHistory(details.getDependentEmailHistory());
            emailQueueRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create EmailQueue entity", e);
        }
    }


    public EmailQueueDetails getDetails(EmailQueueEntity entity) {
        EmailQueueDetails details = new EmailQueueDetails();
        details.setId(entity.getId());
        details.setCreated(entity.getCreated());
        details.setEmailHistory(entity.getEmailHistory());
        details.setRecipient(entity.getRecipient());
        details.setPriority(entity.getPriority());
        details.setRemoveEmailHistory(entity.isRemoveEmailHistory());
        details.setDependentEmailHistory(entity.getDependentEmailHistory());
        return details;
    }

    public List<EmailQueueDetails> findAll() {
        return emailQueueRepository.findAll().stream()
                .map(this::getDetails)
                .toList();
    }

    public List<EmailQueueDetails> findAll(int offset, int count) {
        return emailQueueRepository.findAll(offset, count).stream()
                .map(this::getDetails)
                .toList();
    }

    public Optional<EmailQueueDetails> findFirstMail() {
        return emailQueueRepository.findFirstMail().map(this::getDetails);
    }

    public List<EmailQueueDetails> findByHistory(String historyId) {
        return emailQueueRepository.findByHistory(historyId).stream()
                .map(this::getDetails)
                .toList();
    }

    public List<EmailQueueDetails> findByDependentHistory(String historyId) {
        return emailQueueRepository.findByDependentHistory(historyId).stream()
                .map(this::getDetails)
                .toList();
    }
}
