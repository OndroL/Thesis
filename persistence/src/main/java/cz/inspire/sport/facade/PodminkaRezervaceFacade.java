package cz.inspire.sport.facade;

import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.mapper.PodminkaRezervaceMapper;
import cz.inspire.sport.service.PodminkaRezervaceService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PodminkaRezervaceFacade {
    @Inject
    PodminkaRezervaceService podminkaRezervaceService;
    @Inject
    PodminkaRezervaceMapper podminkaRezervaceMapper;

    public PodminkaRezervaceDto mapToDto(PodminkaRezervaceEntity entity) {
        return podminkaRezervaceMapper.toDto(entity);
    }

    public PodminkaRezervaceDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(podminkaRezervaceService.findByPrimaryKey(id));
    }

    public List<PodminkaRezervaceDto> findAll() throws FinderException {
        return podminkaRezervaceService.findAll().stream().map(this::mapToDto).toList();
    }

    public List<PodminkaRezervaceDto> findAll(int offset, int count) throws FinderException {
        return podminkaRezervaceService.findAll(offset, count).stream().map(this::mapToDto).toList();
    }

    public List<PodminkaRezervaceDto> findByObjekt(String objektId, int offset, int count) throws FinderException {
        return podminkaRezervaceService.findByObjekt(objektId, count, offset).stream().map(this::mapToDto).toList();
    }

    public Long countAllByObject(String objektId) throws FinderException {
        return podminkaRezervaceService.countAllByObject(objektId);
    }

    public Long countAll() throws FinderException {
        return podminkaRezervaceService.countAll();
    }

    public List<String> getObjectIdsByReservationConditionObject(String objektRezervaceId) throws FinderException {
        return podminkaRezervaceService.getObjectIdsByReservationConditionObject(objektRezervaceId);
    }

    public Long getMaxPriority() throws FinderException {
        return podminkaRezervaceService.getMaxPriority();
    }

}
