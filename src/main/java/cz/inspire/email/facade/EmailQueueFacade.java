package cz.inspire.email.facade;

import cz.inspire.email.dto.EmailQueueDto;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.mapper.EmailQueueMapper;
import cz.inspire.email.service.EmailQueueService;
import cz.inspire.email.utils.EmailQueueUtil;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmailQueueFacade {
    @Inject
    EmailQueueService emailQueueService;
    @Inject
    EmailQueueMapper emailQueueMapper;

    Logger logger = LogManager.getLogger(EmailQueueFacade.class);

    public String create(EmailQueueDto dto) throws CreateException {
        try {
            if (dto.getId() == null) {
                dto.setId(EmailQueueUtil.generateGUID(dto));
            }

            EmailQueueEntity entity = emailQueueMapper.toEntity(dto);

            emailQueueService.create(entity);
            return entity.getId();
        } catch (Exception e) {
            logger.error("Failed to create EmailQueue entity. {}", e.getMessage());
            throw new CreateException();
        }
    }

    public EmailQueueDto mapToDto(EmailQueueEntity entity) {
        return emailQueueMapper.toDto(entity);
    }

    public List<EmailQueueDto> findAll() throws FinderException {
        return emailQueueService.findAll().stream()
                .map(this::mapToDto).toList();
    }

    public List<EmailQueueDto> findAll(int offset, int count) throws FinderException {
        return emailQueueService.findAll(offset, count).stream()
                .map(this::mapToDto).toList();
    }

    public Optional<EmailQueueDto> findFirstMail() throws FinderException {
        return emailQueueService.findFirstMail().map(this::mapToDto);
    }

    public List<EmailQueueDto> findByHistory(String historyId) throws FinderException {
        return emailQueueService.findByHistory(historyId).stream()
                .map(this::mapToDto).toList();
    }

    public List<EmailQueueDto> findByDependentHistory(String historyId) throws FinderException {
        return emailQueueService.findByDependentHistory(historyId).stream()
                .map(this::mapToDto).toList();
    }
}
