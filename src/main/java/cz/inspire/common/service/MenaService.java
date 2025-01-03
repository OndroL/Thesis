package cz.inspire.common.service;

import cz.inspire.common.model.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class MenaService {

    private final MenaRepository menaRepository;
    private final Logger logger;

    @Inject
    public MenaService(MenaRepository menaRepository, Logger logger) {
        this.menaRepository = menaRepository;
        this.logger = logger;
    }

    @Transactional
    public void create(MenaEntity entity) throws CreateException {
        try {
            menaRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to create MenaEntity", e);
            throw new CreateException();
        }
    }

    @Transactional
    public void update(MenaEntity entity) throws SystemException {
        try {
            menaRepository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update MenaEntity", e);
            throw new SystemException("Failed to update MenaEntity", e);
        }
    }

    @Transactional
    public void remove(MenaEntity entity) throws SystemException {
        try {
            menaRepository.remove(entity);
        } catch (Exception e) {
            logger.error("Failed to remove MenaEntity", e);
            throw new SystemException("Failed to remove MenaEntity", e);
        }
    }

    public List<MenaEntity> findAll() {
        return menaRepository.findAll();
    }

    public List<MenaEntity> findByCode(String code) {
        return menaRepository.findByCode(code);
    }

    public List<MenaEntity> findByCodeNum(int codeNum) {
        return menaRepository.findByCodeNum(codeNum);
    }

}
