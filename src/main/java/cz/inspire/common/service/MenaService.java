package cz.inspire.common.service;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class MenaService extends BaseService<MenaEntity, String, MenaRepository> {

    public MenaService() {
    }

    @Inject
    public MenaService(MenaRepository menaRepository) {
        super(menaRepository);
    }

    public List<MenaEntity> findByCode(String code) throws FinderException {
        return wrapDBException(() -> repository.findByCode(code), "Failed to find MenaEntity by Code");
    }

    public List<MenaEntity> findByCodeNum(int codeNum) throws FinderException {
        return wrapDBException(() ->repository.findByCodeNum(codeNum), "Failed to find MenaEntity by CodeNum");
    }

}
