package cz.inspire.common.service;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class NastaveniJsonService extends BaseService<NastaveniJsonEntity, NastaveniJsonRepository> {

    @Inject
    public NastaveniJsonService(Logger logger, NastaveniJsonRepository nastaveniJsonRepository) {
        super(logger, nastaveniJsonRepository, NastaveniJsonEntity.class);
    }

    @Override
    protected void save(NastaveniJsonEntity entity) throws Exception {
        repository.save(entity);
    }

    @Override
    protected void delete(NastaveniJsonEntity entity) throws Exception {
        repository.remove(entity);
    }
}
