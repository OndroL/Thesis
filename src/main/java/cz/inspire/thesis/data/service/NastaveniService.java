package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.NastaveniDetails;
import cz.inspire.thesis.data.model.NastaveniEntity;
import cz.inspire.thesis.data.repository.NastaveniRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;

@ApplicationScoped
public class NastaveniService {

    @Inject
    private NastaveniRepository nastaveniRepository;

    public String ejbCreate(String key, Serializable value) throws CreateException {
        try {
            NastaveniEntity entity = new NastaveniEntity();
            entity.setKey(key);
            entity.setValue(value);
            nastaveniRepository.save(entity);
            return key;
        } catch (Exception e) {
            throw new CreateException("Failed to create Nastaveni entity", e);
        }
    }

    public NastaveniDetails getDetails(NastaveniEntity entity) {
        NastaveniDetails details = new NastaveniDetails();
        details.setKey(entity.getKey());
        details.setValue(entity.getValue());
        return details;
    }
}
