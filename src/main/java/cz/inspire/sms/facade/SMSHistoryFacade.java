package cz.inspire.sms.facade;

import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.mapper.SMSHistoryMapper;
import cz.inspire.sms.service.SMSHistoryService;
import cz.inspire.sms.utils.SMSHistoryUtil;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class SMSHistoryFacade {
    @Inject
    private SMSHistoryService smsHistoryService;
    @Inject
    private SMSHistoryMapper smsHistoryMapper;

    public String create(SMSHistoryDto dto) throws CreateException {
        try {
            if (dto.getId() == null) {
                dto.setId(SMSHistoryUtil.generateGUID(dto));
            }
            SMSHistoryEntity entity = smsHistoryMapper.toEntity(dto);

            smsHistoryService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public SMSHistoryDto mapToDto(SMSHistoryEntity entity) { return smsHistoryMapper.toDto(entity); }

    public List<SMSHistoryEntity> findByDate(Date dateFrom, Date dateTo) { return smsHistoryService.findByDate(dateFrom, dateTo); }

    public List<SMSHistoryEntity> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic) {
        return smsHistoryService.findByDateAutomatic(dateFrom, dateTo, automatic) ;}
}

