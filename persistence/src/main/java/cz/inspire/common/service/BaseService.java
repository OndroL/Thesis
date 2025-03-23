package cz.inspire.common.service;

import com.google.common.reflect.TypeToken;
import cz.inspire.exception.SystemException;
import cz.inspire.repository.BaseRepository;
import jakarta.ejb.CreateException;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public abstract class BaseService<E, PK extends Serializable, R extends BaseRepository<E, PK>> {

    private Logger logger;
    protected R repository;
    private final TypeToken<E> typeToken = new TypeToken<>(getClass()) {};

    public BaseService() {
    }

    public BaseService(R repository) {
        this.repository = repository;
        logger = LogManager.getLogger(getClass());
    }

    public List<E> findAll() throws FinderException {
        return wrapDBException(() -> repository.findAll(), "Failed to findAll for " + getEntityType());
    }

    public E findByPrimaryKey(PK pk) throws FinderException {
        if (pk == null) {
            return null;
        }
        return wrapDBException(
                () -> repository.findByPrimaryKey(pk),
                "Failed to find " + getEntityType() + " with primary key: " + pk
        );
    }

    public E create(E entity) throws CreateException {
        try {
            return repository.create(entity);
        } catch (EntityExistsException e) {
            logger.error("Failed to create " + getEntityType() + " already exists.", e);
            throw new DuplicateKeyException("Failed to create " + getEntityType() + " already exists.");
        } catch (Exception e) {
            logger.error("Failed to create " + getEntityType(), e);
            throw new CreateException("Failed to create " + getEntityType());
        }
    }

    public E update(E entity) throws SystemException {
        try {
            return repository.update(entity);
        } catch (Exception e) {
            logger.error("Failed to update " + getEntityType(), e);
            throw new SystemException("Failed to update " + getEntityType(), e);
        }
    }

    public void delete(E entity) throws RemoveException {
        if (entity == null) {
            throw new RemoveException("Cannot delete null as " + getEntityType());
        }
        try {
            repository.delete(entity);
        } catch (Exception e) {
            logger.error("Failed to remove " + getEntityType(), e);
            throw new RemoveException("Failed to remove " + getEntityType());
        }
    }

    public void deleteByPrimaryKey(PK primaryKey) throws RemoveException {
        if (primaryKey == null) {
            throw new RemoveException("Cannot delete " + getEntityType() + " with null primary key");
        }
        try {
            repository.deleteByPrimaryKey(primaryKey);
        } catch (Exception e) {
            logger.error("Failed to remove " + getEntityType(), e);
            throw new RemoveException("Failed to remove " + getEntityType());
        }
    }

    private String getEntityType() {
        return typeToken.getRawType().getSimpleName();
    }
}