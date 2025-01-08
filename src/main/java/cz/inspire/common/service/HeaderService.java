package cz.inspire.common.service;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import org.apache.logging.log4j.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class HeaderService extends BaseService<HeaderEntity, HeaderRepository> {

    @Inject
    public HeaderService(Logger logger, HeaderRepository repository) {
        super(logger, repository, HeaderEntity.class);
    }

    public List<HeaderEntity> findValidAttributes()
    {
        return repository.findValidAtributes();
    }
}
