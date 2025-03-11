package cz.inspire.sport.facade;

import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.mapper.SportKategorieMapper;
import cz.inspire.sport.service.SportKategorieService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SportKategorieFacade {
    @Inject
    SportKategorieService sportKategorieService;
    @Inject
    SportKategorieMapper sportKategorieMapper;

    public SportKategorieDto mapToDto(SportKategorieEntity entity) {
        return sportKategorieMapper.toDto(entity);
    }

    public List<SportKategorieDto> findAll() throws FinderException {
        return sportKategorieService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<SportKategorieDto> findRoot() throws FinderException {
        return sportKategorieService.findRoot().stream().map(this::mapToDto).toList();
    }

    public List<SportKategorieDto> findAllByNadrazenaKategorie(String nadrazenaKategorieId) throws FinderException {
        return sportKategorieService.findAllByNadrazenaKategorie(nadrazenaKategorieId).stream().map(this::mapToDto).toList();
    }

    public List<SportKategorieDto> findAll(int offset, int count) throws FinderException {
        return sportKategorieService.findAll(count, offset).stream().map(this::mapToDto).toList();
    }

    public List<SportKategorieDto> findAllByMultisportFacilityId(String multisportFacilityId) throws FinderException {
        return sportKategorieService.findAllByMultisportFacilityId(multisportFacilityId).stream().map(this::mapToDto).toList();
    }

    public Long count() throws FinderException {
        return sportKategorieService.count();
    }

    public Long countRoot() throws FinderException {
        return sportKategorieService.countRoot();
    }

    public Long countByNadrazenaKategorie(String nadrazenaKategorieId) throws FinderException {
        return sportKategorieService.countByNadrazenaKategorie(nadrazenaKategorieId);
    }
}
