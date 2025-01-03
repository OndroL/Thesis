package cz.inspire.common.service;

import cz.inspire.common.model.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.Logger;


@ApplicationScoped
public class NastaveniService {

    private final NastaveniRepository nastaveniRepository;
    private final Logger logger;

    @Inject
    public NastaveniService(Logger logger, NastaveniRepository nastaveniRepository) {
        this.logger = logger;
        this.nastaveniRepository = nastaveniRepository;
    }

    @Transactional
    public String create(NastaveniEntity entity) throws CreateException {
        try {
            nastaveniRepository.save(entity);
            return entity.getKey();

        } catch (Exception e) {
            logger.error("Failed to create NastaveniEntity", e);
            throw new CreateException();
        }
    }

    @Transactional
    public void update(NastaveniEntity entity) throws SystemException {
        try {
            nastaveniRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update NastaveniEntity", e);
            throw new SystemException("Failed to update NastaveniEntity", e);
        }
    }

    @Transactional
    public void remove(NastaveniEntity entity) throws SystemException {
        try {
            nastaveniRepository.remove(entity);
        } catch (Exception e) {
            logger.error("Failed to remove NastaveniEntity", e);
            throw new SystemException("Failed to remove NastaveniEntity", e);
        }
    }

}
