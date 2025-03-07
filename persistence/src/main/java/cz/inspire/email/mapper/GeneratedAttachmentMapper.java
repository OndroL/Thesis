package cz.inspire.email.mapper;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import jakarta.ejb.CreateException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public abstract class GeneratedAttachmentMapper {

    @Inject
    protected PrintTemplateService printTemplateService;

    private static final Logger logger = LogManager.getLogger(GeneratedAttachmentMapper.class);

    // Map DTO to Entity
    @Mapping(target = "emailHistory", ignore = true)
    @Mapping(target = "printTemplate", ignore = true)
    public abstract  GeneratedAttachmentEntity toEntity(GeneratedAttachmentDto dto) throws CreateException;

    @AfterMapping
    protected void afterToEntitySetPrintTemplate(GeneratedAttachmentDto dto,
                                                 @MappingTarget GeneratedAttachmentEntity entity) throws CreateException {
        try {
            if (dto.getPrintTemplateId() != null) {
                PrintTemplateEntity template = printTemplateService.findByPrimaryKey(dto.getPrintTemplateId());
                entity.setPrintTemplate(template);
            }
        } catch (Exception e) {
            logger.error("Couldn't find PrintTemplateEntity with id: {} while trying to create generatedAttachment. {}", dto.getPrintTemplateId(), e);
            throw new CreateException("Couldn't find PrintTemplateEntity with id: " + dto.getPrintTemplateId() +
                    " while trying to create generatedAttachment." + e);
        }
    }

    // Map Entity to DTO
    @Mapping(target = "emailHistoryId", source = "emailHistory.id")
    @Mapping(target = "printTemplateId", source = "printTemplate.id")
    public abstract GeneratedAttachmentDto toDto(GeneratedAttachmentEntity entity);
}
