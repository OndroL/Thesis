package cz.inspire.common.facade;

import cz.inspire.common.dto.NastaveniDto;
import cz.inspire.common.mapper.NastaveniMapper;
import cz.inspire.common.model.NastaveniEntity;
import cz.inspire.common.service.NastaveniService;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;

@ApplicationScoped
public class NastaveniFacade {
    @Inject
    NastaveniService nastaveniService;
    @Inject
    NastaveniMapper nastaveniMapper;

    public String create(String key, Serializable value) throws CreateException {
        try {
            nastaveniService.create(new NastaveniEntity(key, value));
            /// Even that the docs in old Bean said return type to be primary key of the new instance
            /// It is set to return null
            return null;

        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public NastaveniDto mapToDto(NastaveniEntity entity) {
        return nastaveniMapper.toDto(entity);
    }
}
