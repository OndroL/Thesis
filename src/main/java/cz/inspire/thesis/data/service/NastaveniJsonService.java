package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.NastaveniJsonDetails;
import cz.inspire.thesis.data.model.NastaveniJsonEntity;
import cz.inspire.thesis.data.repository.NastaveniJsonRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NastaveniJsonService {

    @Inject
    private NastaveniJsonRepository nastaveniJsonRepository;

    public String ejbCreate(String key, String value) throws CreateException {
        try {
            NastaveniJsonEntity entity = new NastaveniJsonEntity();
            entity.setKey(key);
            entity.setValue(value);

            nastaveniJsonRepository.save(entity);

            return key;

        } catch (Exception e) {
            throw new CreateException("Failed to create NastaveniJson entity", e);
        }
    }

    public String ejbCreate(NastaveniJsonDetails details) throws CreateException {
        try {
            return ejbCreate(details.getKey(), details.getValue());
        } catch (Exception e) {
            throw new CreateException("Failed to create NastaveniJson entity", e);
        }
    }

    public NastaveniJsonDetails getDetails(NastaveniJsonEntity entity) {
        NastaveniJsonDetails details = new NastaveniJsonDetails();
        details.setKey(entity.getKey());
        details.setValue(entity.getValue());

        return details;
    }
}
