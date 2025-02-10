package cz.inspire.sequence.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.repository.SequenceRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class SequenceService extends BaseService<SequenceEntity, String, SequenceRepository> {

    public SequenceService() {
    }

    @Inject
    public SequenceService(SequenceRepository repository) {
        super(repository);
    }

    public List<SequenceEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all SequenceEntity records (Ordered)"
        );
    }
   /*
    public Optional<SequenceEntity> findBySkladType(String skladId, int type) throws FinderException {
        return wrapDBException(
                () -> repository.findBySkladType(skladId, type),
                "Error retrieving SequenceEntity by sklad type (skladId=" + skladId + ", type=" + type + ")"
        );
    }

    public Optional<SequenceEntity> findByPokladnaType(String pokladnaId, int type) throws FinderException {
        return wrapDBException(
                () -> repository.findByPokladnaType(pokladnaId, type),
                "Error retrieving SequenceEntity by pokladna type (pokladnaId=" + pokladnaId + ", type=" + type + ")"
        );
    }
*/
    public List<SequenceEntity> findByType(int type, int offset, int limit) throws FinderException {
        return wrapDBException(
                () -> repository.findByType(type, new Limit(limit, offset + 1)),
                "Error retrieving SequenceEntity by type (type=" + type + ", offset=" + offset + ", limit=" + limit + ")"
        );
    }
}
