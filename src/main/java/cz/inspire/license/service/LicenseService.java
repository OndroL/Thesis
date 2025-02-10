package cz.inspire.license.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class LicenseService extends BaseService<LicenseEntity, String, LicenseRepository> {

    public LicenseService() {
    }

    @Inject
    public LicenseService(LicenseRepository repository) {
        super(repository);
    }

    public List<LicenseEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all LicenseEntity records (Ordered)"
        );
    }
}
