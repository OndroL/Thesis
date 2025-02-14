package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.repository.OtviraciDobaObjektuRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public class OtviraciDobaObjektuService extends BaseService<OtviraciDobaObjektuEntity, OtviraciDobaObjektuPK, OtviraciDobaObjektuRepository> {

    public OtviraciDobaObjektuService() {
    }

    @Inject
    public OtviraciDobaObjektuService(OtviraciDobaObjektuRepository repository) {
        super(repository);
    }

    public List<OtviraciDobaObjektuEntity> findByObjekt(String objektId) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjekt(objektId),
                "Error retrieving valid attributes from OtviraciDobaObjektuEntity"
        );
    }

    public List<OtviraciDobaObjektuEntity> findByObjektWithLimit(String objektId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjektWithLimit(objektId, Limit.range(offset + 1, count)),
                "Error retrieving valid attributes from OtviraciDobaObjektuEntity"
        );
    }

    public Optional<OtviraciDobaObjektuEntity> findCurrent(String objektId, Date day) throws FinderException {
        return wrapDBException(
                () -> repository.findCurrent(objektId, LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC)),
                "Error retrieving valid attributes from OtviraciDobaObjektuEntity"
        );
    }

    public List<OtviraciDobaObjektuEntity> findAfter(String objektId, Date day) throws FinderException {
        return wrapDBException(
                () -> repository.findAfter(objektId, LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC)),
                "Error retrieving valid attributes from OtviraciDobaObjektuEntity"
        );
    }

    public List<LocalDateTime> getCurrentIdsByObjectAndDay(String objektId, Date day) throws FinderException {
        return wrapDBException(
                () -> repository.getCurrentIdsByObjectAndDay(objektId, LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC)),
                "Error retrieving valid attributes from OtviraciDobaObjektuEntity"
        );
    }

    public Optional<OtviraciDobaObjektuEntity> findById(OtviraciDobaObjektuPK pk) throws FinderException {
        return wrapDBException(
                () -> repository.findById(pk),
                "Error retrieving valid attributes from ObjektSportEntity"
        );
    }

    public void deleteById(OtviraciDobaObjektuPK pk) throws FinderException {
        wrapDBException(
                () -> {
                    repository.deleteById(pk);
                    return null;
                },
                "Error deleting ObjektSportEntity record for primary key=" + pk
        );
    }
}
