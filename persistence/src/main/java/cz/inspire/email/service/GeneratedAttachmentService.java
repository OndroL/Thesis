package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class GeneratedAttachmentService extends BaseService<GeneratedAttachmentEntity, String, GeneratedAttachmentRepository> {

    public GeneratedAttachmentService() {
    }

    @Inject
    public GeneratedAttachmentService(GeneratedAttachmentRepository repository) {
        super(repository);
    }

    public List<GeneratedAttachmentEntity> findByEmailAndHistory(String historyId, String email) throws FinderException {
        return wrapDBException(
                () -> repository.findByEmailAndHistory(historyId, email),
                "Error retrieving GeneratedAttachmentEntity by email and history (historyId = " + historyId + ", email = " + email + ")"
        );
    }

    public List<GeneratedAttachmentEntity> findByHistory(String historyId) throws FinderException {
        return wrapDBException(
                () -> repository.findByHistory(historyId),
                "Error retrieving GeneratedAttachmentEntity by history (historyId = " + historyId + ")"
        );
    }

    public List<GeneratedAttachmentEntity> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId) throws FinderException {
        return wrapDBException(
                () -> repository.findByEmailAndHistoryAndTemplate(historyId, email, templateId),
                "Error retrieving GeneratedAttachmentEntity by email, history, and template (historyId = " + historyId
                        + ", email = " + email + ", templateId = " + templateId + ")"
        );
    }
}
