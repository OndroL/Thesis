package cz.inspire.sequence.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.repository.SequenceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SequenceService extends BaseService<SequenceEntity, SequenceRepository> {

    @Inject
    public SequenceService(Logger logger, SequenceRepository repository) {
        super(logger, repository, SequenceEntity.class);
    }

    public List<SequenceEntity> findAll() { return repository.findAll(); }

    public Optional<SequenceEntity> findBySkladType(String skladId, int type) {
        return repository.findBySkladType(skladId, type);
    }

    public Optional<SequenceEntity> findByPokladnaType(String pokladnaId, int type) {
        return repository.findByPokladnaType(pokladnaId, type);
    }

    public List<SequenceEntity> findByType(int type,int offset,int limit) {
        return repository.findByType(type, offset, limit);
    }

    public Optional<SequenceEntity> findById(String id) { return repository.findById(id); }
}
