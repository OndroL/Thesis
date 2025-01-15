package cz.inspire.license.facade;

import cz.inspire.license.dto.LicenseDto;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.mapper.LicenseMapper;
import cz.inspire.license.service.LicenseService;
import cz.inspire.license.utils.LicenseUtil;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LicenseFacade {
    @Inject
    LicenseService licenseService;
    @Inject
    LicenseMapper licenseMapper;

    public String create(LicenseDto dto) throws CreateException {
        try {
            LicenseEntity entity = licenseMapper.toEntity(dto);
            if (entity.getId() == null) {
                entity.setId(LicenseUtil.generateGUID(entity));
            }
            licenseService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public LicenseDto mapToDto(LicenseEntity entity) { return licenseMapper.toDto(entity); }

    public LicenseEntity mapToEntity(LicenseDto dto) { return licenseMapper.toEntity(dto); }
}
