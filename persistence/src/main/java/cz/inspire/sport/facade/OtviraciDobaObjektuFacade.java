package cz.inspire.sport.facade;

import cz.inspire.sport.dto.OtviraciDobaObjektuDto;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.mapper.OtviraciDobaObjektuMapper;
import cz.inspire.sport.service.OtviraciDobaObjektuService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class OtviraciDobaObjektuFacade {
    @Inject
    OtviraciDobaObjektuService otviraciDobaObjektuService;
    @Inject
    OtviraciDobaObjektuMapper otviraciDobaObjektuMapper;

    public OtviraciDobaObjektuPK create(OtviraciDobaObjektuDto dto) throws CreateException {
        OtviraciDobaObjektuEntity entity = otviraciDobaObjektuService.create(otviraciDobaObjektuMapper.toEntity(dto));
        // in old bean return null;
        return entity.getEmbeddedId();
    }

    public OtviraciDobaObjektuDto mapToDto(OtviraciDobaObjektuEntity entity) {
        return otviraciDobaObjektuMapper.toDto(entity);
    }

    public OtviraciDobaObjektuDto findByPrimaryKey(OtviraciDobaObjektuPK id) throws FinderException {
        return mapToDto(otviraciDobaObjektuService.findByPrimaryKey(id));
    }

    public List<OtviraciDobaObjektuDto> findAll() throws FinderException {
        return otviraciDobaObjektuService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<OtviraciDobaObjektuDto> findByObjekt(String objektId) throws FinderException {
        return otviraciDobaObjektuService.findByObjekt(objektId).stream().map(this::mapToDto).toList();
    }

    public List<OtviraciDobaObjektuDto> findByObjekt(String objektId, int offset, int count) throws FinderException {
        return otviraciDobaObjektuService.findByObjekt(objektId, count, offset).stream().map(this::mapToDto).toList();
    }

    public OtviraciDobaObjektuDto findCurrent(String objektId, Date day) throws FinderException {
        return mapToDto(otviraciDobaObjektuService.findCurrent(objektId, day));
    }

    public List<OtviraciDobaObjektuDto> findAfter(String objektId, Date day) throws FinderException {
        return otviraciDobaObjektuService.findAfter(objektId, day).stream().map(this::mapToDto).toList();
    }

    public List<LocalDateTime> getCurrentIdsByObjectAndDay(String objektId, Date day) throws FinderException {
        return otviraciDobaObjektuService.getCurrentIdsByObjectAndDay(objektId, day);
    }
}
