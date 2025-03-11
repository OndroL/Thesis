package cz.inspire.sport.facade;

import cz.inspire.exception.InvalidParameterException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.mapper.ObjektMapper;
import cz.inspire.sport.service.ObjektService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ObjektFacade {

    @Inject
    ObjektService objektService;
    @Inject
    ObjektMapper objektMapper;

    public ObjektEntity create(ObjektDto dto) throws CreateException, InvalidParameterException {
        return  objektService.create(objektMapper.toEntity(dto));
    }

    public void update(ObjektEntity entity) throws SystemException {
        objektService.update(entity);
    }

    public void delete(ObjektEntity entity) throws RemoveException {
        objektService.delete(entity);
    }

    public ObjektDto mapToDto(ObjektEntity entity) {
        return objektMapper.toDto(entity);
    }

    public ObjektDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(objektService.findByPrimaryKey(id));
    }

    public List<ObjektDto> findAll() throws FinderException {
        return objektService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findByAreal(String arealId, String jazyk) throws FinderException {
        return objektService.findByAreal(arealId, jazyk).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findBaseByAreal(String arealId, String jazyk) throws FinderException {
        return objektService.findBaseByAreal(arealId, jazyk).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findByAreal(String arealId, String jazyk, int offset, int count) throws FinderException {
        return objektService.findByAreal(arealId, jazyk, offset, count).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findByTypRezervace(Integer typRezervace, String jazyk) throws FinderException {
        return objektService.findByTypRezervace(typRezervace, jazyk).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findBaseByAreal(String arealId, String jazyk, int offset, int count) throws FinderException {
        return objektService.findBaseByAreal(arealId, jazyk, offset, count).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findBySport(String sportId, String jazyk) throws FinderException {
        return objektService.findBySport(sportId, jazyk).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findByPrimyVstup(String jazyk, boolean primyVstup) throws FinderException {
        return objektService.findByPrimyVstup(jazyk, primyVstup).stream().map(this::mapToDto).toList();
    }

    public List<ObjektDto> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) throws FinderException {
        return objektService.findByPrimyVstup(jazyk, offset, count, primyVstup).stream().map(this::mapToDto).toList();
    }

}
