package cz.inspire.sport.facade;

import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.mapper.ArealMapper;
import cz.inspire.sport.service.ArealService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ArealFacade {
    @Inject
    ArealService arealService;
    @Inject
    ArealMapper arealMapper;


    public String create(ArealDto dto) throws CreateException {
        try {
            ArealEntity entity = arealService.create(arealMapper.toEntity(dto));
            return entity.getId();
        } catch (Exception ex) {
            throw new CreateException("Failed to complete create operations for Areal: " + ex.getMessage());
        }
    }

    public void update(ArealDto dto) throws SystemException, FinderException {
        arealService.update(arealMapper.toEntity(dto));
    }

    public void delete(ArealEntity areal) throws RemoveException {
        arealService.delete(areal);
    }

    public ArealDto mapToDto(ArealEntity entity) {
        return arealMapper.toDto(entity);
    }

    public ArealDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(arealService.findByPrimaryKey(id));
    }

    public List<ArealDto> findAll() throws FinderException {
        return arealService.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ArealDto> findByParent(String parentId, String jazyk) throws FinderException {
        return arealService.findByParent(parentId, jazyk).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ArealDto> findRoot(String jazyk) throws FinderException {
        return arealService.findRoot(jazyk).stream()
                .map(this::mapToDto)
                .toList();
    }

    public ArealDto findIfChild(String childId, String parentId) throws FinderException {
        return mapToDto(arealService.findIfChild(childId, parentId));
    }

    public List<ArealDto> findByParent(String parentId, String jazyk, int offset, int count) throws FinderException {
        return arealService.findByParent(parentId, jazyk, offset, count).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ArealDto>  findRoot(String jazyk, int offset, int count) throws FinderException {
        return arealService.findRoot(jazyk, offset, count).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<String> getArealIdsByParent(String arealId) throws FinderException {
        return arealService.getArealIdsByParent(arealId);
    }

}
