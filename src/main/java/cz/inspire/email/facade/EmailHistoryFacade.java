package cz.inspire.email.facade;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.mapper.EmailHistoryMapper;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.email.utils.EmailHistoryUtil;
import cz.inspire.email.utils.GeneratedAttachmentUtil;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static cz.inspire.common.mapper.MenaMapper.logger;

@ApplicationScoped
public class EmailHistoryFacade {
    @Inject
    EmailHistoryService emailHistoryService;
    @Inject
    EmailHistoryMapper emailHistoryMapper;
    @Inject
    GeneratedAttachmentService generatedAttachmentService;
    @Inject
    GeneratedAttachmentMapper generatedAttachmentMapper;
    @Inject
    PrintTemplateService printTemplateService;

    public String create(EmailHistoryDto dto) throws CreateException {
        try {
            if (dto.getId() == null) {
                dto.setId(EmailHistoryUtil.generateGUID(dto));
            }
            EmailHistoryEntity entity = emailHistoryMapper.toEntity(dto);

            emailHistoryService.create(entity);

            setGeneratedAttachments(entity, dto);

            emailHistoryService.update(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public void setGeneratedAttachments (EmailHistoryEntity entity, EmailHistoryDto dto) throws CreateException {
        if (dto.getGeneratedAttachments() != null && !dto.getGeneratedAttachments().isEmpty()) {
            try {
                for (GeneratedAttachmentDto gad : dto.getGeneratedAttachments()) {
                    if(gad.getId() == null){
                        gad.setId(GeneratedAttachmentUtil.generateGUID(gad));
                    }
                    GeneratedAttachmentEntity gadEntity = generatedAttachmentMapper.toEntity(gad);

                    generatedAttachmentService.create(gadEntity);

                    gadEntity.setEmailHistory(entity);

                    setPrintedTemplate(gadEntity);

                    generatedAttachmentService.update(gadEntity);
                }
            } catch (Exception e) {
                logger.error("EmailHistory couldn't be created", e);
                throw new CreateException("EmailHistory couldn't be created: " + e.getMessage());
            }
        }
    }

    public void setPrintedTemplate(GeneratedAttachmentEntity entity) throws CreateException {
        try {
            if (entity.getPrintTemplate() != null) {
                PrintTemplateEntity localTemplate = printTemplateService.findById(entity.getPrintTemplate().getId());
                entity.setPrintTemplate(localTemplate);
            }

        } catch (Exception e) {
            logger.error("GeneratedAttachment couldn't be created, couldn't set PrintTemplate with id : " + entity.getPrintTemplate(), e);
            throw new CreateException("GeneratedAttachment couldn't be created: " + e.toString());
        }
    }
}
