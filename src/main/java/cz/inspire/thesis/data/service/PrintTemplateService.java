package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.PrintTemplateDetails;
import cz.inspire.thesis.data.model.PrintTemplateEntity;
import cz.inspire.thesis.data.repository.PrintTemplateRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class PrintTemplateService {

    @Inject
    private PrintTemplateRepository printTemplateRepository;


    public String ejbCreate(PrintTemplateDetails details) throws CreateException {
        try {
            PrintTemplateEntity entity = new PrintTemplateEntity();

            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setContent(details.getContent());
            entity.setType(details.getType());
            entity.setTemplateName(details.getTemplateName());
            entity.setFilename(details.getFileName());

            printTemplateRepository.save(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed to create PrintTemplate entity", e);
        }
    }

    public PrintTemplateDetails getDetails(PrintTemplateEntity entity) {
        PrintTemplateDetails details = new PrintTemplateDetails();
        details.setId(entity.getId());
        details.setContent(entity.getContent());
        details.setType(entity.getType());
        details.setTemplateName(entity.getTemplateName());
        details.setFileName(entity.getFilename());

        return details;
    }


    public Optional<PrintTemplateEntity> findById(String templateId) {
        return printTemplateRepository.findById(templateId);
    }
}
