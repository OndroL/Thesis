package cz.inspire.common.service;

import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.Logger;

public abstract class BaseService<E, R extends EntityRepository<E, ? extends java. io. Serializable>>{

    protected final Logger logger;
    protected final R repository;
    private final Class<E> entityClass;

    public BaseService(Logger logger, R repository, Class<E> entityClass) {
        this.logger = logger;
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Transactional
    public void create(E entity) throws CreateException {
        try {
            repository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to create {}", entityClass.getSimpleName(), e);
            throw new CreateException("Failed to create " + entityClass.getSimpleName());
        }
    }

    @Transactional
    public void update(E entity) throws SystemException {
        try {
            repository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update {}", entityClass.getSimpleName(), e);
            throw new SystemException("Failed to update " + entityClass.getSimpleName(), e);
        }
    }

    @Transactional
    public void remove(E entity) throws SystemException {
        try {
            repository.remove(entity);
        } catch (Exception e) {
            logger.error("Failed to remove {}", entityClass.getSimpleName(), e);
            throw new SystemException("Failed to remove " + entityClass.getSimpleName(), e);
        }
    }
}
