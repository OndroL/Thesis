package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.repository.ArealRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public class ArealService extends BaseService<ArealEntity, String, ArealRepository> {

    public ArealService() {
    }

    @Inject
    public ArealService(ArealRepository repository) {super(repository);}

    public List<ArealEntity> findAllOrdered() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all ArealEntity records in ordered manner"
        );
    }

    public List<ArealEntity> findByParent(String parentId, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findByParent(parentId, jazyk),
                "Error retrieving ArealEntity records by parentId=" + parentId + ", jazyk=" + jazyk
        );
    }

    public List<ArealEntity> findRoot(String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findRoot(jazyk),
                "Error retrieving root ArealEntity records for jazyk=" + jazyk
        );
    }

    public List<ArealEntity> findByParentWithLimit(String parentId, String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByParentWithLimit(parentId, jazyk, Limit.range(offset + 1, count)),
                "Error retrieving ArealEntity records by parentId=" + parentId + ", jazyk=" + jazyk +
                        " with pagination (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public List<ArealEntity> findRootWithLimit(String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findRootWithLimit(jazyk, Limit.range(offset + 1, count)),
                "Error retrieving root ArealEntity records for jazyk=" + jazyk +
                        " with pagination (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public Optional<ArealEntity> findIfChild(String childId, String parentId) throws FinderException {
        return wrapDBException(
                () -> repository.findIfChild(childId, parentId),
                "Error checking if ArealEntity with childId=" + childId + " is a child of parentId=" + parentId
        );
    }

    public List<String> findArealIdsByParent(String arealId) throws FinderException {
        return wrapDBException(
                () -> repository.findArealIdsByParent(arealId),
                "Error retrieving ArealEntity IDs by parentId=" + arealId
        );
    }

}
