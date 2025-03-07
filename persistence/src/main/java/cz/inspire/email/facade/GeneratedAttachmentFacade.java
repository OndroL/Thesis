package cz.inspire.email.facade;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.GeneratedAttachmentService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GeneratedAttachmentFacade {
    @Inject
    GeneratedAttachmentService generatedAttachmentService;
    @Inject
    GeneratedAttachmentMapper generatedAttachmentMapper;

    public GeneratedAttachmentDto mapToDto(GeneratedAttachmentEntity entity) {
        return generatedAttachmentMapper.toDto(entity);
    }

    public List<GeneratedAttachmentDto> findByEmailAndHistory(String historyId, String email) throws FinderException {
        return generatedAttachmentService.findByEmailAndHistory(historyId, email).stream().map(this::mapToDto).toList();
    }

    public List<GeneratedAttachmentDto> findByHistory(String historyId) throws FinderException {
        return generatedAttachmentService.findByHistory(historyId).stream().map(this::mapToDto).toList();
    }

    public List<GeneratedAttachmentDto> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId) throws FinderException {
        return generatedAttachmentService.findByEmailAndHistoryAndTemplate(historyId, email, templateId).stream()
                .map(this::mapToDto).toList();
    }

}
