package cz.inspire.common.service;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MenaService extends BaseService<MenaEntity, String, MenaRepository> {

    public MenaService() {
    }

    @Inject
    public MenaService(MenaRepository menaRepository) {
        super(menaRepository);
    }

    public List<MenaEntity> findByCode(String code) {
        return repository.findByCode(code);
    }

    public List<MenaEntity> findByCodeNum(int codeNum) {
        return repository.findByCodeNum(codeNum);
    }

}
