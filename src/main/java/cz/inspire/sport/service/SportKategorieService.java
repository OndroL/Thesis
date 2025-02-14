package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.repository.SportKategorieRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public class SportKategorieService extends BaseService<SportKategorieEntity, String, SportKategorieRepository> {

    public SportKategorieService() {
    }

    @Inject
    public SportKategorieService(SportKategorieRepository repository) {
        super(repository);
    }

    public List<SportKategorieEntity> findAllOrdered() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all SportKategorieEntity records in ordered manner"
        );
    }

    public List<SportKategorieEntity> findRoot() throws FinderException {
        return wrapDBException(
                () -> repository.findRoot(),
                "Error retrieving root SportKategorieEntity records"
        );
    }

    public List<SportKategorieEntity> findAllByNadrazenaKategorie(String nadrazenaKategorieId) throws FinderException {
        return wrapDBException(
                () -> repository.findAllByNadrazenaKategorie(nadrazenaKategorieId),
                "Error retrieving SportKategorieEntity records for nadrazenaKategorieId=" + nadrazenaKategorieId
        );
    }

    public List<SportKategorieEntity> findAll(int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(Limit.range(offset + 1, count)),
                "Error retrieving paginated SportKategorieEntity records (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public List<SportKategorieEntity> findAllByMultisportFacilityId(String multisportFacilityId) throws FinderException {
        return wrapDBException(
                () -> repository.findAllByMultisportFacilityId(multisportFacilityId),
                "Error retrieving SportKategorieEntity records for multisportFacilityId=" + multisportFacilityId
        );
    }

    public Long count() throws FinderException {
        return wrapDBException(
                () -> repository.count(),
                "Error retrieving total count of SportKategorieEntity records"
        );
    }

    public Long countRoot() throws FinderException {
        return wrapDBException(
                () -> repository.countRoot(),
                "Error retrieving count of root SportKategorieEntity records"
        );
    }

    public Long countByNadrazenaKategorie(String nadrazenaKategorieId) throws FinderException {
        return wrapDBException(
                () -> repository.countByNadrazenaKategorie(nadrazenaKategorieId),
                "Error retrieving count of SportKategorieEntity records for nadrazenaKategorieId=" + nadrazenaKategorieId
        );
    }
}
