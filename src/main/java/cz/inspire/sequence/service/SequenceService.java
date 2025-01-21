package cz.inspire.sequence.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.repository.SequenceRepository;
import jakarta.data.Limit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SequenceService extends BaseService<SequenceEntity, String, SequenceRepository> {

    public SequenceService() {
    }

    @Inject
    public SequenceService(SequenceRepository repository) {
        super(repository);
    }

    public List<SequenceEntity> findAll() { return repository.findAllOrdered(); }

//    public Optional<SequenceEntity> findBySkladType(String skladId, int type) {
//        return repository.findBySkladType(skladId, type);
//    }
//
//    public Optional<SequenceEntity> findByPokladnaType(String pokladnaId, int type) {
//        return repository.findByPokladnaType(pokladnaId, type);
//    }

    public List<SequenceEntity> findByType(int type, int offset, int limit) {
        return repository.findByType(type, new Limit(limit, offset));
    }

    public Optional<SequenceEntity> findById(String id) { return repository.findById(id); }
}
