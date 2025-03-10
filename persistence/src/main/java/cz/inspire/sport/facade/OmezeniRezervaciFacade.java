package cz.inspire.sport.facade;

import cz.inspire.sport.dto.OmezeniRezervaciDto;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.mapper.OmezeniRezervaciMapper;
import cz.inspire.sport.service.OmezeniRezervaciService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class OmezeniRezervaciFacade {
    @Inject
    OmezeniRezervaciService omezeniRezervaciService;
    @Inject
    OmezeniRezervaciMapper omezeniRezervaciMapper;

    public String create(OmezeniRezervaciDto dto) throws CreateException {
        OmezeniRezervaciEntity entity = omezeniRezervaciService.create(omezeniRezervaciMapper.toEntity(dto));
        return entity.getObjektId();
    }

    public OmezeniRezervaciDto mapToDto(OmezeniRezervaciEntity entity) {
        return omezeniRezervaciMapper.toDto(entity);
    }

    public OmezeniRezervaciDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(omezeniRezervaciService.findByPrimaryKey(id));
    }

    public List<OmezeniRezervaciDto> findAll() throws FinderException {
        return omezeniRezervaciService.findAll().stream().map(this::mapToDto).toList();
    }

}
