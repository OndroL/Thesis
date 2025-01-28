package cz.inspire.license.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LicenseService extends BaseService<LicenseEntity, String, LicenseRepository> {

    public LicenseService() {
    }

    @Inject
    public LicenseService(LicenseRepository repository) {
        super(repository);
    }

    public List<LicenseEntity> findAll() { return repository.findAllOrdered(); }
}
