package cz.inspire.sequence.facade;

import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.mapper.SequenceMapper;
import cz.inspire.sequence.service.SequenceService;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * TODO : Add missing business functionality - will be done with PokladnaBean
 */


@ApplicationScoped
public class SequenceFacade {
    @Inject
    SequenceService sequenceService;

    @Inject
    SequenceMapper sequenceMapper;

    Logger logger = LogManager.getLogger(SequenceFacade.class);

    public String create(SequenceDto dto) throws CreateException {
        try {
            SequenceEntity entity = sequenceMapper.toEntity(dto);
            sequenceService.create(entity);
            entity = toEntityWithRelationships(dto);
            sequenceService.update(entity);
            return entity.getName();
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public SequenceDto mapToDto(SequenceEntity entity) {
        return sequenceMapper.toDto(entity);
    }

    /**
     * Here is point for discussion on naming the function name to keep it as toEntity to be more consistent
     * with naming scheme with other facades
     * Or name it toEntityWithRelationships to better describe it purpose, as it's probably never called from controller
     * and only in facade
     */
    public SequenceEntity toEntityWithRelationships(SequenceDto dto) {
        SequenceEntity entity = sequenceMapper.toEntity(dto);

        try {
            if (dto.getStornoSeqName() != null){
                SequenceEntity stornoSeqOpt = sequenceService.findByPrimaryKey(dto.getStornoSeqName());
                entity.setStornoSeq(stornoSeqOpt);
            }
        } catch (Exception ex) {
            logger.error("Failed to find storno sequence with name : {} for sequence with name : {}", dto.getStornoSeqName(), dto.getName(), ex);
        }
        return entity;
    }

    public void update(SequenceDto dto) throws SystemException {
        SequenceEntity entity = toEntityWithRelationships(dto);
        logger.debug("setDetails - setting last: {}", dto.getLast());
        sequenceService.update(entity);
    }

}
