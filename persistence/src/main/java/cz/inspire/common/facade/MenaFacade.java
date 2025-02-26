package cz.inspire.common.facade;

import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.mapper.MenaMapper;
import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.service.MenaService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MenaFacade {
    @Inject
    MenaService menaService;
    @Inject
    MenaMapper menaMapper;

    public void create(MenaDto mena) throws CreateException {
        try {
            menaService.create(menaMapper.toEntity(mena));
        } catch (Exception e) {
            throw new CreateException(("Failed to create MenaEntity: " + e.getMessage()));
        }
    }

    public MenaDto mapToDto(MenaEntity entity) {
        return menaMapper.toDto(entity);
    }


    public List<MenaDto> findByCode(String code) throws FinderException {
        return menaService.findByCode(code).stream().map(menaMapper::toDto).toList();
    }

    public List<MenaDto> findByCodeNum(int codeNum) throws FinderException {
        return menaService.findByCodeNum(codeNum).stream().map(menaMapper::toDto).toList();
    }

    public List<MenaDto> findAll() throws FinderException {
        return menaService.findAll().stream().map(menaMapper::toDto).toList();
    }
    
    public MenaDto findByPrimaryKey(String menaId) throws FinderException {
        return menaMapper.toDto(menaService.findByPrimaryKey(menaId));
    }

}
