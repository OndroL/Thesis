package cz.inspire.sms.facade;

import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.mapper.SMSHistoryMapper;
import cz.inspire.sms.service.SMSHistoryService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class SMSHistoryFacade {
    @Inject
    SMSHistoryService smsHistoryService;
    @Inject
    SMSHistoryMapper smsHistoryMapper;

    public String create(SMSHistoryDto dto) throws CreateException {
        try {
            SMSHistoryEntity entity = smsHistoryMapper.toEntity(dto);

            smsHistoryService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public SMSHistoryDto mapToDto(SMSHistoryEntity entity) { return smsHistoryMapper.toDto(entity); }

    public List<SMSHistoryEntity> findByDate(Date dateFrom, Date dateTo) throws FinderException { return smsHistoryService.findByDate(dateFrom, dateTo); }

    public List<SMSHistoryEntity> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic) throws FinderException {
        return smsHistoryService.findByDateAutomatic(dateFrom, dateTo, automatic) ;}
}

