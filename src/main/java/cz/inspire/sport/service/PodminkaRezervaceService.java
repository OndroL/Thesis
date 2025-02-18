package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.repository.PodminkaRezervaceRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class PodminkaRezervaceService extends BaseService<PodminkaRezervaceEntity, String, PodminkaRezervaceRepository> {

    public PodminkaRezervaceService() {
    }

    @Inject
    public PodminkaRezervaceService(PodminkaRezervaceRepository repository) {
        super(repository);
    }

    public List<PodminkaRezervaceEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all PodminkaRezervaceEntity records, Ordered by priorita"
        );
    }

    public List<PodminkaRezervaceEntity> findAll(int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(Limit.range(offset + 1, count)),
                "Error retrieving paginated PodminkaRezervaceEntity records (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public List<PodminkaRezervaceEntity> findByObjekt(String objektId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjekt(objektId, Limit.range(offset + 1, count)),
                "Error retrieving PodminkaRezervaceEntity records for objektId = " + objektId +
                " with pagination (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public Long countAllByObject(String objektId) throws FinderException {
        return wrapDBException(
                () -> repository.countAllByObject(objektId),
                "Error retrieving count of PodminkaRezervaceEntity records for objektId = " + objektId
        );
    }

    public Long countAll() throws FinderException {
        return wrapDBException(
                () -> repository.countAll(),
                "Error retrieving total count of PodminkaRezervaceEntity records"
        );
    }

    public List<String> getObjectIdsByReservationConditionObject(String objektRezervaceId) throws FinderException {
        return wrapDBException(
                () -> repository.getObjectIdsByReservationConditionObject(objektRezervaceId),
                "Error retrieving object IDs from PodminkaRezervaceEntity for objektRezervaceId = " + objektRezervaceId
        );
    }

    public Long getMaxPriority() throws FinderException {
        return wrapDBException(
                () -> repository.getMaxPriority(),
                "Error retrieving max priority from PodminkaRezervaceEntity"
        );
    }
}
