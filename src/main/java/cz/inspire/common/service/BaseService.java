package cz.inspire.common.service;

import com.google.common.reflect.TypeToken;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.data.repository.CrudRepository;
import jakarta.ejb.CreateException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseService<E, PK extends Serializable, R extends CrudRepository<E, PK>> {

    private Logger logger;
    protected R repository;
    private final TypeToken<E> typeToken = new TypeToken<>(getClass()) {};
    
    public BaseService() {
    }

    public BaseService(R repository) {
        this.repository = repository;
        logger = LogManager.getLogger(getClass());
    }

    // CRUD operations

    public void create(E entity) throws CreateException {
        try {

            repository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to create " + getEntityType().getSimpleName(), e);
            throw new CreateException("Failed to create " + getEntityType().getSimpleName());
        }
    }

    public void update(E entity) throws SystemException {
        try {
            repository.save(entity);
        } catch (Exception e) {
            logger.error("Failed to update " +  getEntityType().getSimpleName(), e);
            throw new SystemException("Failed to update " + getEntityType().getSimpleName(), e);
        }
    }

    public void delete(E entity) throws SystemException {
        try {
            repository.delete(entity);
        } catch (Exception e) {
            logger.error("Failed to remove " + getEntityType().getSimpleName(), e);
            throw new SystemException("Failed to remove " + getEntityType().getSimpleName(), e);
        }
    }

    // Basic finders

    public List<E> findAll() {
        return repository.findAll().toList();
    }

    public Optional<E> findByPK(PK pk) {
        return repository.findById(pk);
    }


    // Entity type Helper for errors and Exceptions
    // If we are confident that typeToken.getRawType() always returns the correct Class<E>, the suppression is safe
    // Otherwise we will have everywhere warnings for
    // Unchecked cast: 'java. lang. Class<capture<? super E>>' to 'java. lang. Class<E>'
    @SuppressWarnings("unchecked")
    private Class<E> getEntityType() {
        return (Class<E>) typeToken.getRawType();
    }
}