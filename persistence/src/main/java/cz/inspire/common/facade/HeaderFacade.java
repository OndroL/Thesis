package cz.inspire.common.facade;

import cz.inspire.common.dto.HeaderDto;
import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.mapper.HeaderMapper;
import cz.inspire.common.service.HeaderService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class HeaderFacade {
    @Inject
    HeaderService headerService;
    @Inject
    HeaderMapper headerMapper;

    public String create(String id, int field, int location) throws CreateException {
        try {
            HeaderDto headerDto = new HeaderDto(id, field, location);
            headerService.create(headerMapper.toEntity(headerDto));
            return headerDto.getId();

        } catch (Exception e) {
            throw new CreateException("Failed to create HeaderEntity: " + e.getMessage());
        }
    }

    public HeaderDto mapToDto(HeaderEntity entity) { return headerMapper.toDto(entity);}

    // Typo (missing 't' in Attributes) in old bean signature="java.util.Collection findValidAtributes()"
    public List<HeaderDto> findValidAtributes() throws FinderException {
        return headerService.findValidAttributes().stream().map(this::mapToDto).toList();
    }

}
