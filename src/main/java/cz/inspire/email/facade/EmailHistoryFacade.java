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
import cz.inspire.utils.File;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Logger logger = LogManager.getLogger(EmailHistoryFacade.class);

    public String create(EmailHistoryDto dto) throws CreateException {
        try {
            if (dto.getId() == null) {
                dto.setId(EmailHistoryUtil.generateGUID(dto));
            }
            EmailHistoryEntity entity = emailHistoryMapper.toEntity(dto);

            if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
                List<File> savedAttachments = emailHistoryService.saveAttachments(dto.getAttachments());
                entity.setAttachments(savedAttachments); // Save as JSONB
            }
            emailHistoryService.create(entity);

            //PostCreate logic
            setGeneratedAttachments(entity, dto);

            emailHistoryService.update(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed while creating EmailHistory with error : " + e);
        }
    }

    public void setGeneratedAttachments (EmailHistoryEntity entity, EmailHistoryDto dto) throws CreateException {
        if (dto.getGeneratedAttachments() != null && !dto.getGeneratedAttachments().isEmpty()) {
            try {
                for (GeneratedAttachmentDto gadDto : dto.getGeneratedAttachments()) {
                    if(gadDto.getId() == null){
                        gadDto.setId(GeneratedAttachmentUtil.generateGUID(gadDto));
                    }
                    GeneratedAttachmentEntity gadEntity = generatedAttachmentMapper.toEntity(gadDto);

                    generatedAttachmentService.create(gadEntity);

                    gadEntity.setEmailHistory(entity);

                    setPrintedTemplate(gadEntity, gadDto);

                    generatedAttachmentService.update(gadEntity);
                }
            } catch (Exception e) {
                logger.error("EmailHistory couldn't be created", e);
                throw new CreateException("EmailHistory couldn't be created: " + e.getMessage());
            }
        }
    }

    public void setPrintedTemplate(GeneratedAttachmentEntity entity, GeneratedAttachmentDto dto) throws CreateException {
        try {
            if (dto.getPrintTemplateId() != null) {
                PrintTemplateEntity localTemplate = printTemplateService.findByPrimaryKey(dto.getPrintTemplateId());
                entity.setPrintTemplate(localTemplate);
            }
        } catch (Exception e) {
            logger.error("Couldn't find PrintTemplateEntity with id: {} while trying to create generatedAttachment with id : {}, {}", dto.getPrintTemplateId(), entity.getId(), e);
            throw new CreateException("Couldn't find PrintTemplateEntity with id: " + dto.getPrintTemplateId() +
                    " while trying to create generatedAttachment with id : " + entity.getId() + e);
        }
    }

    public EmailHistoryDto mapToDto(EmailHistoryEntity entity) {
        EmailHistoryDto dto = emailHistoryMapper.toDto(entity);

        // Reconstruct files from file paths
        if (entity.getAttachments() != null) {
            Map<String, byte[]> attachments = new HashMap<>();
            entity.getAttachments().forEach((file) -> {
                try {
                    byte[] fileContent = emailHistoryService.readFile(file.getFilePath());
                    attachments.put(file.getFileName(), fileContent);
                } catch (IOException e) {
                    logger.error("Failed to read file with name : {} and file path :{}", file.getFileName(), file.getFilePath(), e);
                }
            });
            dto.setAttachments(attachments);
        }
        return dto;
    }

    public List<EmailHistoryDto> findAll() throws FinderException {
        return emailHistoryService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<EmailHistoryDto> findAll(int offset, int count) throws FinderException {
        return emailHistoryService.findAll(offset, count).stream().map(this::mapToDto).toList();
    }

    public List<EmailHistoryDto> findByDate(Date dateFrom, Date dateTo, int offset, int count) throws FinderException {
        return emailHistoryService.findByDate(dateFrom, dateTo, offset, count).stream().map(this::mapToDto).toList();
    }

    public EmailHistoryDto findByPrimaryKey(String emailHistoryId) throws FinderException {
        return mapToDto(emailHistoryService.findByPrimaryKey(emailHistoryId));
    }


}
