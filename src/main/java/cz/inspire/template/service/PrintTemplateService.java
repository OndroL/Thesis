package cz.inspire.template.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class PrintTemplateService extends BaseService<PrintTemplateEntity, PrintTemplateRepository> {

    @Inject
    public PrintTemplateService(Logger logger, PrintTemplateRepository repository) {
        super(logger, repository, PrintTemplateEntity.class);
    }

    public Optional<PrintTemplateEntity> findById(String templateId) {
        return repository.findById(templateId);
    }
}
