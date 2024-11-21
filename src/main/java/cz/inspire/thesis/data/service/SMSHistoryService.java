package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.SMSHistoryDetails;
import cz.inspire.thesis.data.model.SMSHistoryEntity;
import cz.inspire.thesis.data.repository.SMSHistoryRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SMSHistoryService {

    @Inject
    private SMSHistoryRepository smsHistoryRepository;

    public SMSHistoryDetails getDetails(SMSHistoryEntity entity) {
        SMSHistoryDetails details = new SMSHistoryDetails();
        details.setId(entity.getId());
        details.setDate(entity.getDate());
        details.setGroups(deserializeCollection(entity.getGroups()));
        details.setRecipients(deserializeCollection(entity.getRecipients()));
        details.setMoreRecipients(deserializeCollection(entity.getMorerecipients()));
        details.setMessage(entity.getMessage());
        details.setAutomatic(entity.getAutomatic());

        return details;
    }

    public String ejbCreate(SMSHistoryDetails details) throws CreateException {
        try {
            SMSHistoryEntity entity = new SMSHistoryEntity();

            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setDate(details.getDate());
            entity.setGroups(serializeCollection(details.getGroups()));
            entity.setRecipients(serializeCollection(details.getRecipients()));
            entity.setMorerecipients(serializeCollection(details.getMoreRecipients()));
            entity.setMessage(details.getMessage());
            entity.setAutomatic(details.getAutomatic());

            smsHistoryRepository.save(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed to create SMSHistory entity", e);
        }
    }


    public List<SMSHistoryDetails> findByDate(Date dateFrom, Date dateTo) {
        List<SMSHistoryEntity> entities = smsHistoryRepository.findByDate(dateFrom, dateTo);
        return entities.stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<SMSHistoryDetails> findByDateAutomatic(Date dateFrom, Date dateTo, boolean automatic) {
        List<SMSHistoryEntity> entities = smsHistoryRepository.findByDateAutomatic(dateFrom, dateTo, automatic);
        return entities.stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    private byte[] serializeCollection(List<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return String.join(",", collection).getBytes();
    }

    private List<String> deserializeCollection(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        String csv = new String(data);
        return List.of(csv.split(","));
    }
}
