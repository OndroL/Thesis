package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.GeneratedAttachmentDetails;
import cz.inspire.thesis.data.model.GeneratedAttachmentEntity;
import cz.inspire.thesis.data.model.EmailHistoryEntity;
import cz.inspire.thesis.data.model.PrintTemplateEntity;
import cz.inspire.thesis.data.repository.GeneratedAttachmentRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.Optional;

import static cz.inspire.thesis.data.utils.CollectionsHelper.deserializeAttributes;
import static cz.inspire.thesis.data.utils.CollectionsHelper.serializeAttributes;
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class GeneratedAttachmentService {

    @Inject
    private GeneratedAttachmentRepository generatedAttachmentRepository;

    @Inject
    private PrintTemplateService printTemplateService;

    public String ejbCreate(GeneratedAttachmentDetails details, EmailHistoryEntity emailHistory) throws CreateException {
        try {
            GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setEmail(details.getEmail());
            entity.setAttributes(serializeAttributes(details.getAttributes()));
            entity.setEmailHistory(emailHistory);

            generatedAttachmentRepository.save(entity);

            // Call post-create logic
            ejbPostCreate(details, emailHistory, entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create GeneratedAttachment entity", e);
        }
    }


    public void ejbPostCreate(GeneratedAttachmentDetails details, EmailHistoryEntity emailHistory, GeneratedAttachmentEntity entity) throws CreateException {
        try {
            // Set EmailHistory (already set in ejbCreate)
            entity.setEmailHistory(emailHistory);

            // Set PrintTemplate if provided
            String templateId = details.getPrintTemplateId();
            if (templateId != null) {
                Optional<PrintTemplateEntity> template = printTemplateService.findById(templateId);
                if (template.isPresent()) {
                    entity.setPrintTemplate(template.get());
                    generatedAttachmentRepository.save(entity);
                } else {
                    throw new CreateException("PrintTemplate with ID " + templateId + " not found.");
                }
            }
        } catch (Exception e) {
            throw new CreateException("GeneratedAttachment couldn't be created: " + e.getMessage(), e);
        }
    }


    public GeneratedAttachmentDetails getDetails(GeneratedAttachmentEntity entity) {
        GeneratedAttachmentDetails details = new GeneratedAttachmentDetails();
        details.setId(entity.getId());
        details.setEmail(entity.getEmail());
        details.setAttributes(deserializeAttributes(entity.getAttributes()));

        if (entity.getPrintTemplate() != null) {
            details.setPrintTemplateId(entity.getPrintTemplate().getId());
        }

        if (entity.getEmailHistory() != null) {
            details.setEmailHistoryId(entity.getEmailHistory().getId());
        }

        return details;
    }

    public Collection<GeneratedAttachmentDetails> findByHistory(String historyId) {
        return generatedAttachmentRepository.findByHistory(historyId).stream()
                .map(this::getDetails)
                .toList();
    }


    public Collection<GeneratedAttachmentDetails> findByEmailAndHistory(String historyId, String email) {
        return generatedAttachmentRepository.findByEmailAndHistory(historyId, email).stream()
                .map(this::getDetails)
                .toList();
    }


    public Collection<GeneratedAttachmentDetails> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId) {
        return generatedAttachmentRepository.findByEmailAndHistoryAndTemplate(historyId, email, templateId).stream()
                .map(this::getDetails)
                .toList();
    }

}
