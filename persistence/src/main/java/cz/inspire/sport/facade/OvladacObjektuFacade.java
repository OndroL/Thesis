package cz.inspire.sport.facade;

import cz.inspire.sport.dto.OvladacObjektuDto;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.mapper.OvladacObjektuMapper;
import cz.inspire.sport.service.OvladacObjektuService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class OvladacObjektuFacade {
    @Inject
    OvladacObjektuService ovladacObjektuService;
    @Inject
    OvladacObjektuMapper ovladacObjektuMapper;

    public OvladacObjektuDto mapToDto(OvladacObjektuEntity entity) {
        return ovladacObjektuMapper.toDto(entity);
    }

    public OvladacObjektuDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(ovladacObjektuService.findByPrimaryKey(id));
    }

    public List<OvladacObjektuDto> findWithOvladacObjektu(String idOvladace) throws FinderException {
        return ovladacObjektuService.findWithOvladacObjektu(idOvladace).stream().map(this::mapToDto).toList();
    }

    public List<OvladacObjektuDto> findByObjekt(String objektId) throws FinderException {
        return ovladacObjektuService.findByObjekt(objektId).stream().map(this::mapToDto).toList();
    }
}
