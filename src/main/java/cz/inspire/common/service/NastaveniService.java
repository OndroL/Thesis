package cz.inspire.common.service;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;


@ApplicationScoped
public class NastaveniService extends BaseService<NastaveniEntity, NastaveniRepository> {

    @Inject
    public NastaveniService(Logger logger, NastaveniRepository nastaveniRepository) {
        super(logger, nastaveniRepository, NastaveniEntity.class);
    }

    @Override
    protected void save(NastaveniEntity entity) throws Exception {
        repository.save(entity);
    }

    @Override
    protected void delete(NastaveniEntity entity) throws Exception {
        repository.remove(entity);
    }

}
