package cz.inspire.common.facade;

import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.mapper.MenaMapper;
import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.service.MenaService;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
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
            throw new CreateException(("Menae couldn't be created: " + e.getMessage()));
        }
    }

    public MenaDto mapToDto(MenaEntity entity) {
        return menaMapper.toDto(entity);
    }


    public List<MenaDto> findByCode(String code) {
        return menaService.findByCode(code).stream().map(menaMapper::toDto).toList();
    }

    public List<MenaDto> findByCodeNum(int codeNum) {
        return menaService.findByCodeNum(codeNum).stream().map(menaMapper::toDto).toList();
    }

    public List<MenaDto> findAll() {
        return menaService.findAll().stream().map(menaMapper::toDto).toList();
    }
    
    public MenaDto findByPK(String menaId) {
        return menaMapper.toDto(menaService.findByPK(menaId).orElse(null));
    }

}
