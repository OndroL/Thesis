package cz.inspire.common.service;

import cz.inspire.common.model.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class NastaveniJsonService {

    private final Logger logger;
    private final NastaveniJsonRepository nastaveniJsonRepository;

    @Inject
    public NastaveniJsonService(Logger logger, NastaveniJsonRepository nastaveniJsonRepository) {
        this.logger = logger;
        this.nastaveniJsonRepository = nastaveniJsonRepository;
    }

    @Transactional
    public void create(NastaveniJsonEntity entity) throws CreateException {
        try {
            nastaveniJsonRepository.save(entity);

        } catch (Exception e) {
            logger.error("Failed to create NastaveniJsonEntity", e);
            throw new CreateException();
        }
    }

    @Transactional
    public void update(NastaveniJsonEntity entity) throws SystemException {
        try {
            nastaveniJsonRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update NastaveniJsonEntity", e);
            throw new SystemException("Failed to update NastaveniJsonEntity", e);
        }
    }

    @Transactional
    public void remove(NastaveniJsonEntity entity) throws SystemException {
        try {
            nastaveniJsonRepository.remove(entity);
        } catch (Exception e) {
            logger.error("Failed to remove NastaveniJsonEntity", e);
            throw new SystemException("Failed to remove NastaveniJsonEntity", e);
        }
    }
}
