package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.repository.OtviraciDobaObjektuRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class OtviraciDobaObjektuService extends BaseService<OtviraciDobaObjektuEntity, OtviraciDobaObjektuPK, OtviraciDobaObjektuRepository> {

    public OtviraciDobaObjektuService() {
    }

    @Inject
    public OtviraciDobaObjektuService(OtviraciDobaObjektuRepository repository) {
        super(repository);
    }

    public List<OtviraciDobaObjektuEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all OtviraciDobaObjektuEntity records, Ordered by objektId in embeddedId"
        );
    }

    public List<OtviraciDobaObjektuEntity> findByObjekt(String objektId) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjekt(objektId),
                "Error retrieving OtviraciDobaObjektuEntity records for objektId = " + objektId
        );
    }

    public List<OtviraciDobaObjektuEntity> findByObjekt(String objektId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjektWithLimit(objektId, count, offset),
                "Error retrieving OtviraciDobaObjektuEntity records for objektId = " + objektId +
                        " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public OtviraciDobaObjektuEntity findCurrent(String objektId, Date day) throws FinderException {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC);
        return wrapDBException(
                () -> repository.findCurrent(objektId, localDateTime, 1).orElseThrow(() -> new NoResultException("No OtviraciDobaObjektuEntity found" +
                        " for objektId = " + objektId + " and date = " + localDateTime)),
                "Error retrieving current OtviraciDobaObjektuEntity record for objektId = " + objektId + ", date = " + localDateTime
        );
    }

    public List<OtviraciDobaObjektuEntity> findAfter(String objektId, Date day) throws FinderException {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC);
        return wrapDBException(
                () -> repository.findAfter(objektId, localDateTime),
                "Error retrieving OtviraciDobaObjektuEntity records after date = " + localDateTime + " for objektId = " + objektId
        );
    }

    public List<LocalDateTime> getCurrentIdsByObjectAndDay(String objektId, Date day) throws FinderException {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC);
        return wrapDBException(
                () -> repository.getCurrentIdsByObjectAndDay(objektId, localDateTime),
                "Error retrieving current OtviraciDobaObjektuEntity IDs for objektId = " + objektId + ", date = " + localDateTime
        );
    }
}
