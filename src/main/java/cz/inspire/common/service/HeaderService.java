package cz.inspire.common.service;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class HeaderService extends BaseService<HeaderEntity, String, HeaderRepository> {

    public HeaderService() {
    }

    @Inject
    public HeaderService(HeaderRepository repository) {
        super(repository);
    }

    public List<HeaderEntity> findValidAttributes() throws FinderException, DuplicateKeyException {
        return wrapDBException(() -> repository.findValidAttributes(), "Finding valid attributes in HeaderEntity");
    }
}
