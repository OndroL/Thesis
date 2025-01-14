package cz.inspire.email.facade;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.email.utils.GeneratedAttachmentUtil;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class GeneratedAttachmentFacade {
    @Inject
    GeneratedAttachmentService generatedAttachmentService;
    @Inject
    GeneratedAttachmentMapper generatedAttachmentMapper;
    @Inject
    private EmailHistoryService emailHistoryService;
    @Inject
    private PrintTemplateService printTemplateService;

    Logger logger = LogManager.getLogger(GeneratedAttachmentFacade.class);

    // Create in old bean was only called from EmailHistory bean, I left here generic create (same as in PrintedTemplate)
    // with only one parameter for DTO and "same" logic as in Bean. Can be deleted or left if necessary in controllers
    // Difrence in logic is with searching for EmailHistory by its Id and
    public String create(GeneratedAttachmentDto dto) throws CreateException {
        try {
            if (dto.getId() == null) {
               dto.setId(GeneratedAttachmentUtil.generateGUID(dto));
            }

            GeneratedAttachmentEntity entity = generatedAttachmentMapper.toEntity(dto);

            EmailHistoryEntity emailEntity = emailHistoryService.findById(dto.getEmailHistoryId())
                            .orElseThrow(() -> new CreateException("Couldn't find EmailHistoryEntity with id : " + dto.getEmailHistoryId() +
                                    " while trying to create GeneratedAttachmentEntity"));

            entity.setEmailHistory(emailEntity);

            generatedAttachmentService.create(entity);

            setPrintTemplate(entity, dto);

            generatedAttachmentService.update(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException();
        }
    }

    // Because of "generic" create for GeneratedAttachment here is basically identical code for setPrintTemplate
    public void setPrintTemplate(GeneratedAttachmentEntity entity, GeneratedAttachmentDto dto) throws CreateException {
        try {
            if (dto.getPrintTemplateId() != null) {
                PrintTemplateEntity localTemplate = printTemplateService.findById(dto.getPrintTemplateId())
                        .orElseThrow(() -> new CreateException("Couldn't find PrintTemplateEntity with id: " + dto.getPrintTemplateId() +
                                " while trying to create generatedAttachment with id : " + entity.getId()));
                entity.setPrintTemplate(localTemplate);
            }
        } catch (Exception e) {
            logger.error("GeneratedAttachment couldn't be created", e);
            throw new CreateException("GeneratedAttachment couldn't be created: " + e);
        }
    }

    public GeneratedAttachmentDto mapToDto(GeneratedAttachmentEntity entity) {
        return generatedAttachmentMapper.toDto(entity);
    }

    public List<GeneratedAttachmentDto> findByEmailAndHistory(String historyId, String email) {
        return generatedAttachmentService.findByEmailAndHistory(historyId, email).stream().map(this::mapToDto).toList();
    }

    public List<GeneratedAttachmentDto> findByHistory(String historyId) {
        return generatedAttachmentService.findByHistory(historyId).stream().map(this::mapToDto).toList();
    }

    public List<GeneratedAttachmentDto> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId) {
        return generatedAttachmentService.findByEmailAndHistoryAndTemplate(historyId, email, templateId).stream()
                .map(this::mapToDto).toList();
    }

}
