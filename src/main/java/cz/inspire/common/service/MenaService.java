package cz.inspire.common.service;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class MenaService extends BaseService<MenaEntity, MenaRepository> {

    @Inject
    public MenaService(MenaRepository menaRepository, Logger logger) {
        super(logger, menaRepository, MenaEntity.class);
    }

    @Override
    protected void save(MenaEntity entity) throws Exception {
        repository.save(entity);
    }

    @Override
    protected void delete(MenaEntity entity) throws Exception {
        repository.remove(entity);
    }

    public List<MenaEntity> findAll() {
        return repository.findAll();
    }

    public List<MenaEntity> findByCode(String code) {
        return repository.findByCode(code);
    }

    public List<MenaEntity> findByCodeNum(int codeNum) {
        return repository.findByCodeNum(codeNum);
    }

}
