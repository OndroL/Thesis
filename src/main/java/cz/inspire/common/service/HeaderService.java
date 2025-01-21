package cz.inspire.common.service;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class HeaderService extends BaseService<HeaderEntity, String, HeaderRepository> {

    public HeaderService() {
    }

    @Inject
    public HeaderService(HeaderRepository repository) {
        super(repository);
    }

    public List<HeaderEntity> findValidAttributes()
    {
        return repository.findValidAttributes();
    }
}
