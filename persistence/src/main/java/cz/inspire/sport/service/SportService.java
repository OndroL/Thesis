package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.SportRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class SportService extends BaseService<SportEntity, String, SportRepository> {

    public SportService() {
    }

    @Inject
    public SportService(SportRepository repository) {
        super(repository);
    }


    public List<SportEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAll(),
                "Error retrieving all SportEntity records, Ordered by Id"
        );
    }

    public List<SportEntity> findByParent(String parentId, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findByParent(parentId, jazyk),
                "Error retrieving SportEntity records for parentId = " + parentId + ", jazyk = " + jazyk
        );
    }

    public List<SportEntity> findByParent(String parentId, String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByParent(parentId, jazyk, count, offset),
                "Error retrieving SportEntity records for parentId = " + parentId + ", jazyk = " + jazyk +
                " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public List<SportEntity> findByCategory(String kategorieId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByCategory(kategorieId, count, offset),
                "Error retrieving SportEntity records for kategorieId = " + kategorieId +
                " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public List<SportEntity> findByZbozi(String zboziId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByZbozi(zboziId, count, offset),
                "Error retrieving SportEntity records for zboziId = " + zboziId +
                " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public List<SportEntity> findRoot(String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findRoot(jazyk),
                "Error retrieving root SportEntity records for jazyk = " + jazyk
        );
    }

    public List<SportEntity> findRoot(String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findRoot(jazyk, count, offset),
                "Error retrieving root SportEntity records for jazyk = " + jazyk +
                " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public List<SportEntity> findCategoryRoot(int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findCategoryRoot(count, offset),
                "Error retrieving root category SportEntity records" +
                " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public Long countCategoryRoot() throws FinderException {
        return wrapDBException(
                () -> repository.countCategoryRoot(),
                "Error retrieving count of root category SportEntity records"
        );
    }

    public Long countAllByCategory(String categoryId) throws FinderException {
        return wrapDBException(
                () -> repository.countAllByCategory(categoryId),
                "Error retrieving count of SportEntity records for categoryId = " + categoryId
        );
    }

    public Long countAllByParentAndLanguage(String parentId, String language) throws FinderException {
        return wrapDBException(
                () -> repository.countAllByParentAndLanguage(parentId, language),
                "Error retrieving count of SportEntity records for parentId = " + parentId + ", language = " + language
        );
    }

    public List<String> getAllIdsByParentAndLanguage(String parentId, String language) throws FinderException {
        return wrapDBException(
                () -> repository.getAllIdsByParentAndLanguage(parentId, language),
                "Error retrieving SportEntity IDs for parentId = " + parentId + ", language = " + language
        );
    }

    public Long countRootByLanguage(String language) throws FinderException {
        return wrapDBException(
                () -> repository.countRootByLanguage(language),
                "Error retrieving count of root SportEntity records for language = " + language
        );
    }

    public List<String> getRootIdsByLanguage(String language) throws FinderException {
        return wrapDBException(
                () -> repository.getRootIdsByLanguage(language),
                "Error retrieving root SportEntity IDs for language = " + language
        );
    }
}
