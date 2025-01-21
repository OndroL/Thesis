package cz.inspire.template.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class PrintTemplateService extends BaseService<PrintTemplateEntity, String, PrintTemplateRepository> {

    public PrintTemplateService() {
    }

    @Inject
    public PrintTemplateService(PrintTemplateRepository repository) {
        super(repository);
    }

    public Optional<PrintTemplateEntity> findById(String templateId) {
        return repository.findById(templateId);
    }
}
