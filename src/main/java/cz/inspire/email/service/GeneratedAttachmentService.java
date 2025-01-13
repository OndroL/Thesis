package cz.inspire.email.service;


import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class GeneratedAttachmentService extends BaseService<GeneratedAttachmentEntity, GeneratedAttachmentRepository> {

    @Inject
    public GeneratedAttachmentService(Logger logger, GeneratedAttachmentRepository repository) {
        super(logger, repository, GeneratedAttachmentEntity.class);
    }

    public List<GeneratedAttachmentEntity> findByEmailAndHistory(String historyId, String email) {
        return repository.findByEmailAndHistory(historyId, email);
    }

    public List<GeneratedAttachmentEntity> findByHistory(String historyId) { return repository.findByHistory(historyId); }

    public List<GeneratedAttachmentEntity> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId) {
        return repository.findByEmailAndHistoryAndTemplate(historyId, email, templateId);
    }
}
