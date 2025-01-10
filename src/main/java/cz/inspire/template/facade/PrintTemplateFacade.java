package cz.inspire.template.facade;

import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.mapper.PrintTemplateMapper;
import cz.inspire.template.service.PrintTemplateService;
import cz.inspire.template.utils.PrintTemplateUtil;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PrintTemplateFacade {
    @Inject
    PrintTemplateService printTemplateService;

    @Inject
    PrintTemplateMapper printTemplateMapper;

    public String create (PrintTemplateDto dto) throws CreateException {
        try {
            PrintTemplateEntity entity = printTemplateMapper.toEntity(dto);
            if (entity.getId() == null) {
                entity.setId(PrintTemplateUtil.generateGUID(entity));
            }

            printTemplateService.create(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException();
        }
    }
    public PrintTemplateDto mapToDto(PrintTemplateEntity entity){ return printTemplateMapper.toDto(entity);}
}
