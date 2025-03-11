package cz.inspire.sport.facade;

import cz.inspire.exception.InvalidParameterException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.mapper.SportMapper;
import cz.inspire.sport.service.SportService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SportFacade {
    @Inject
    SportService sportService;
    @Inject
    SportMapper sportMapper;

    public String create(SportDto dto) throws CreateException {
        try {
            SportEntity sport = sportService.create(sportMapper.toEntity(dto));
            return sport.getId();
        } catch (Exception e) {
            throw new CreateException("Sport entity could not be created. " + e.getMessage());
        }
    }

    public void update(SportDto dto) throws InvalidParameterException, CreateException, SystemException {
        sportService.update(sportMapper.toEntity(dto));
    }


    public SportDto mapToDto(SportEntity entity) {
        return sportMapper.toDto(entity);
    }

    public SportDto findByPrimaryKey(String pk) throws FinderException {
        return mapToDto(sportService.findByPrimaryKey(pk));
    }

    public List<SportDto> findAll() throws FinderException {
        return sportService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findByParent(String parentId, String jazyk) throws FinderException {
        return sportService.findByParent(parentId, jazyk).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findByParent(String parentId, String jazyk, int offset, int count) throws FinderException {
        return sportService.findByParent(parentId, jazyk, count, offset).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findByCategory(String kategorieId, int offset, int count) throws FinderException {
        return sportService.findByCategory(kategorieId, count, offset).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findByZbozi(String zboziId, int offset, int count) throws FinderException {
        return sportService.findByZbozi(zboziId, count, offset).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findRoot(String jazyk) throws FinderException {
        return sportService.findRoot(jazyk).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findRoot(String jazyk, int offset, int count) throws FinderException {
        return sportService.findRoot(jazyk, count, offset).stream().map(this::mapToDto).toList();
    }

    public List<SportDto> findCategoryRoot(int offset, int count) throws FinderException {
        return sportService.findCategoryRoot(count, offset).stream().map(this::mapToDto).toList();
    }

    public Long countCategoryRoot() throws FinderException {
        return sportService.countCategoryRoot();
    }

    public Long countAllByCategory(String categoryId) throws FinderException {
        return sportService.countAllByCategory(categoryId);
    }

    public Long countAllByParentAndLanguage(String parentId, String language) throws FinderException {
        return sportService.countAllByParentAndLanguage(parentId, language);
    }

    public List<String> getAllIdsByParentAndLanguage(String parentId, String language) throws FinderException {
        return sportService.getAllIdsByParentAndLanguage(parentId, language);
    }

    public Long countRootByLanguage(String language) throws FinderException {
        return sportService.countRootByLanguage(language);
    }

    public List<String> getRootIdsByLanguage(String language) throws FinderException {
        return sportService.getRootIdsByLanguage(language);
    }

}
