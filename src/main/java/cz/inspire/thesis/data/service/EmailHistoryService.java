package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.EmailHistoryDetails;
import cz.inspire.thesis.data.dto.GeneratedAttachmentDetails;
import cz.inspire.thesis.data.model.EmailHistoryEntity;
import cz.inspire.thesis.data.repository.EmailHistoryRepository;
import cz.inspire.thesis.data.utils.guidGenerator;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Iterator;

import static cz.inspire.thesis.data.utils.CollectionsHelper.deserializeCollection;
import static cz.inspire.thesis.data.utils.CollectionsHelper.serializeCollection;

@ApplicationScoped
public class EmailHistoryService {

    @Inject
    private EmailHistoryRepository emailHistoryRepository;

    @Inject
    private GeneratedAttachmentService generatedAttachmentService;

    public String ejbCreate(EmailHistoryDetails details) throws CreateException {
        try {
            EmailHistoryEntity entity = new EmailHistoryEntity();
            if (details.getId() == null) {
                details.setId(guidGenerator.generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setDate(details.getDate());
            entity.setText(details.getText());
            entity.setSubject(details.getSubject());
            entity.setGroups(serializeCollection(details.getGroups()));
            entity.setRecipients(serializeCollection(details.getRecipients()));
            entity.setMoreRecipients(serializeCollection(details.getMoreRecipients()));
            entity.setAutomatic(details.getAutomatic());
            entity.setHtml(details.getHtml());
            entity.setSent(details.getSent());
            emailHistoryRepository.save(entity);

            // Call ejbPostCreate after the entity is created
            ejbPostCreate(details, entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create EmailHistory entity", e);
        }
    }

    public void ejbPostCreate(EmailHistoryDetails details, EmailHistoryEntity entityOfEmailHistory) throws CreateException {
        if (details.getGeneratedAttachments() != null) {
            try {
                for (GeneratedAttachmentDetails gad : details.getGeneratedAttachments()) {
                    // Create and save the GeneratedAttachment and Establish relationship
                    generatedAttachmentService.ejbCreate(gad, entityOfEmailHistory);
                }
            } catch (Exception e) {
                throw new CreateException("EmailHistory couldn't be created: " + e.getMessage(), e);
            }
        }
    }

    public EmailHistoryDetails getDetails(EmailHistoryEntity entity) {
        EmailHistoryDetails details = new EmailHistoryDetails();
        details.setId(entity.getId());
        details.setDate(entity.getDate());
        details.setText(entity.getText());
        details.setSubject(entity.getSubject());
        details.setGroups(deserializeCollection(entity.getGroups()));
        details.setRecipients(deserializeCollection(entity.getRecipients()));
        details.setMoreRecipients(deserializeCollection(entity.getMoreRecipients()));
        details.setAutomatic(entity.getAutomatic());
        details.setHtml(entity.getHtml());
        details.setSent(entity.getSent());
        return details;
    }

}
