package cz.inspire.sport.facade;

import cz.inspire.sport.dto.ObjektSportDto;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.mapper.ObjektSportMapper;
import cz.inspire.sport.service.ObjektSportService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ObjektSportFacade {
    @Inject
    ObjektSportService objektSportService;
    @Inject
    ObjektSportMapper objektSportMapper;

    public ObjektSportDto mapToDto(ObjektSportEntity entity) {
        return objektSportMapper.toDto(entity);
    }

    public ObjektSportDto findByPrimaryKey(ObjektSportPK id) throws FinderException {
        return mapToDto(objektSportService.findByPrimaryKey(id));
    }

    public List<ObjektSportDto> findByObjekt(String objektId) throws FinderException {
        return objektSportService.findByObjekt(objektId).stream().map(this::mapToDto).toList();
    }
}
