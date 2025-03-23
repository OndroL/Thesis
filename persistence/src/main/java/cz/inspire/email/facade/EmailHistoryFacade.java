package cz.inspire.email.facade;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.mapper.EmailHistoryMapper;
import cz.inspire.email.service.EmailHistoryService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class EmailHistoryFacade {
    @Inject
    EmailHistoryService emailHistoryService;
    @Inject
    EmailHistoryMapper emailHistoryMapper;

    public String create(EmailHistoryDto dto) throws CreateException {
        try {
            EmailHistoryEntity entity = emailHistoryMapper.toEntity(dto);

            emailHistoryService.create(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed while creating EmailHistory with error : " + e);
        }
    }

    public EmailHistoryDto mapToDto(EmailHistoryEntity entity) {
        return emailHistoryMapper.toDto(entity);
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
