package cz.inspire.common.service;

import com.google.common.reflect.TypeToken;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.persistence.EntityExistsException;
import jakarta.ejb.RemoveException;
import jakarta.data.repository.CrudRepository;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.CreateException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public abstract class BaseService<E, PK extends Serializable, R extends CrudRepository<E, PK>> {

    private Logger logger;
    protected R repository;
    private final TypeToken<E> typeToken = new TypeToken<>(getClass()) {};

    @PersistenceContext
    protected EntityManager em;

    public BaseService() {
    }

    public BaseService(R repository) {
        this.repository = repository;
        logger = LogManager.getLogger(getClass());
    }

    public List<E> findAll() throws FinderException {
        return wrapDBException(() -> repository.findAll().toList(), "Failed to findAll for " + getEntityType());
    }

    public E findByPrimaryKey(PK pk) throws FinderException {
        if (pk == null) {
            throw new FinderException("Primary key cannot be null for " + getEntityType());
        }
        return repository.findById(pk)
                .orElseThrow(() -> new FinderException(
                        "Failed to find " + getEntityType() + " with primary key: " + pk
                ));
    }

    public Optional<E> findById(PK pk) {
        if (pk == null) {
            return Optional.empty();
        }
        return repository.findById(pk);
    }

    public void create(E entity) throws CreateException {
        try {
            em.persist(entity);
            em.flush();
        } catch (EntityExistsException e) {
            logger.error("Failed to create " + getEntityType() + " already exists.", e);
            throw new DuplicateKeyException("Failed to create " + getEntityType() + " already exists.");
        } catch (Exception e) {
            logger.error("Failed to create " + getEntityType(), e);
            throw new CreateException("Failed to create " + getEntityType());
        }
    }

    public void update(E entity) throws SystemException {
        try {
            em.merge(entity);
            em.flush();
        } catch (Exception e) {
            logger.error("Failed to update " + getEntityType(), e);
            throw new SystemException("Failed to update " + getEntityType(), e);
        }
    }

    public void delete(E entity) throws RemoveException {
        if (entity == null) {
            throw new RemoveException("Cannot delete null entity in " + getEntityType());
        }
        try {
            em.remove(em.merge(entity));
            em.flush();
        } catch (Exception e) {
            logger.error("Failed to remove " + getEntityType(), e);
            throw new RemoveException("Failed to remove " + getEntityType());
        }
    }

    private String getEntityType() {
        return typeToken.getRawType().getSimpleName();
    }

    /**
     * Setter for tests, can be deleted if tests are not necessary anymore
     * @param em EntityManager
     */
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}