package cz.inspire.common.service;

import cz.inspire.common.model.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import cz.inspire.exception.SystemException;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class HeaderService {

    private final Logger logger;
    private final HeaderRepository headerRepository;

    @Inject
    public HeaderService(Logger logger, HeaderRepository headerRepository) {
        this.logger = logger;
        this.headerRepository = headerRepository;
    }

    @Transactional
    public void create(HeaderEntity entity) throws CreateException {
        try {
            headerRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to create HeaderEntity", e);
            throw new CreateException();
        }
    }

    @Transactional
    public void update(HeaderEntity entity) throws SystemException {
        try {
            headerRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update HeaderEntity", e);
            throw new SystemException("Failed to update HeaderEntity", e);
        }
    }

    @Transactional
    public void remove(HeaderEntity entity) throws SystemException {
        try {
            headerRepository.remove(entity);
        } catch (Exception e) {
            logger.error("Failed to remove HeaderEntity", e);
            throw new SystemException("Failed to remove HeaderEntity", e);
        }
    }

    public List<HeaderEntity> findValidAtributes()
    {
        return headerRepository.findValidAtributes();
    }
}
