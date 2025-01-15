package cz.inspire.sequence.facade;

import cz.inspire.exception.SystemException;
import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.mapper.SequenceMapper;
import cz.inspire.sequence.service.SequenceService;
import cz.inspire.sequence.utils.SequenceUtil;
import jakarta.ejb.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


@ApplicationScoped
public class SequenceFacade {
    @Inject
    SequenceService sequenceService;

    @Inject
    SequenceMapper sequenceMapper;

    Logger logger = LogManager.getLogger(SequenceFacade.class);

    @Transactional
    public String create(SequenceDto dto) throws CreateException {
        try {
            if (dto.getName() == null) {
                dto.setName(SequenceUtil.generateGUID(dto));
            }
            SequenceEntity entity = sequenceMapper.toEntity(dto);
            sequenceService.create(entity);
            entity = toEntityWithRelationships(dto);
            sequenceService.update(entity);
            return entity.getName();
        } catch (Exception e) {
            throw new CreateException();
        }
    }

    public SequenceDto toDto(SequenceEntity entity) {
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
                Optional<SequenceEntity> stornoSeqOpt = sequenceService.findById(dto.getStornoSeqName());
                if (stornoSeqOpt.isPresent()) {
                    entity.setStornoSeq(stornoSeqOpt.get());
                } else {
                    logger.error("Cannot find storno sequence with name: {}", dto.getStornoSeqName());
                }
            }
        } catch (Exception ex) {
            logger.error("Error occurred while looking up storno sequence: {}", dto.getStornoSeqName(), ex);
        }
        return entity;
    }

    @Transactional
    public void update(SequenceDto dto) throws SystemException {
        SequenceEntity entity = toEntityWithRelationships(dto);
        logger.debug("setDetails - setting last: {}", dto.getLast());
        sequenceService.update(entity);
    }

}
