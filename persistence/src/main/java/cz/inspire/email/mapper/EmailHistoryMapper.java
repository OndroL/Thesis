package cz.inspire.email.mapper;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.utils.FileAttributes;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = GeneratedAttachmentMapper.class)
public abstract class EmailHistoryMapper {

    @Inject
    EmailHistoryService emailHistoryService;

    Logger logger = LogManager.getLogger(EmailHistoryMapper.class);

    // Map from DTO to Entity
    @Mapping(target = "attachments", source = "attachments", ignore = true)
    @Mapping(target = "generatedAttachments", source = "generatedAttachments")
    public abstract EmailHistoryEntity toEntity(EmailHistoryDto dto);

    @AfterMapping
    protected void setGeneratedAttachments(EmailHistoryDto dto, @MappingTarget EmailHistoryEntity entity) {
        if(entity.getGeneratedAttachments() != null) {
            entity.getGeneratedAttachments().forEach(attachment -> attachment.setEmailHistory(entity));
        }
    }
    @AfterMapping
    protected void setAttachments(EmailHistoryDto dto, @MappingTarget EmailHistoryEntity entity) {
        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<FileAttributes> savedAttachments = emailHistoryService.saveAttachments(dto.getAttachments());
            entity.setAttachments(savedAttachments); // Save as JSONB
        }
    }

    // Map from Entity to DTO
    @Mapping(target = "attachments", source = "attachments", ignore = true)
    public abstract EmailHistoryDto toDto(EmailHistoryEntity entity);

    @AfterMapping
    protected void setAttachments(EmailHistoryEntity entity, @MappingTarget EmailHistoryDto dto) {
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
    }
}
