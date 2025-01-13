package cz.inspire.license.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class LicenseService extends BaseService<LicenseEntity, LicenseRepository> {
    @Inject
    public LicenseService(Logger logger, LicenseRepository repository) {
        super(logger, repository, LicenseEntity.class);
    }

    public List<LicenseEntity> findAll() {return repository.findAll();}
}
