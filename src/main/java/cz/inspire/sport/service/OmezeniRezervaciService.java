package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.repository.OmezeniRezervaciRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class OmezeniRezervaciService extends BaseService<OmezeniRezervaciEntity, String, OmezeniRezervaciRepository> {
    public OmezeniRezervaciService() {
    }

    @Inject
    public OmezeniRezervaciService(OmezeniRezervaciRepository repository) {
        super(repository);
    }

    public List<OmezeniRezervaciEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all OmezeniRezervaciEntity records, Ordered by objektId"
        );
    }
}
