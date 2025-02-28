package cz.inspire.common.facade;

import cz.inspire.common.dto.NastaveniJsonDto;
import cz.inspire.common.mapper.NastaveniJsonMapper;
import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.service.NastaveniJsonService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NastaveniJsonFacade {
    @Inject
    NastaveniJsonService nastaveniJsonService;
    @Inject
    NastaveniJsonMapper nastaveniJsonMapper;

    public String create(String key, String value) throws CreateException {
        try {
            nastaveniJsonService.create(new NastaveniJsonEntity(key, value));
            /// Even that the docs in old Bean said return type to be primary key of the new instance
            /// It was set to return null
            return null;
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public String create(NastaveniJsonDto nastaveniJsonDto) throws CreateException {
        try {
            nastaveniJsonService.create(nastaveniJsonMapper.toEntity(nastaveniJsonDto));
            /// Even that the docs in old Bean said return type to be primary key of the new instance
            /// It was set to return null
            return null;
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public NastaveniJsonDto mapToDto(NastaveniJsonEntity entity) {
        return nastaveniJsonMapper.toDto(entity);
    }

    public NastaveniJsonDto findByPrimaryKey(String id) throws FinderException {
        return mapToDto(nastaveniJsonService.findByPrimaryKey(id));
    }
}
