package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.repository.OvladacObjektuRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class OvladacObjektuService extends BaseService<OvladacObjektuEntity, String, OvladacObjektuRepository> {

    public OvladacObjektuService() {
    }

    @Inject
    public OvladacObjektuService(OvladacObjektuRepository repository) {
        super(repository);
    }

    public List<OvladacObjektuEntity> findWithOvladacObjektu(String idOvladace) throws FinderException {
        return wrapDBException(
                () -> repository.findWithOvladacObjektu(idOvladace),
                "Error retrieving OvladacObjektuEntity records for idOvladace=" + idOvladace
        );
    }

    public List<OvladacObjektuEntity> findByObjekt(String objektId) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjekt(objektId),
                "Error retrieving OvladacObjektuEntity records for objektId=" + objektId
        );
    }
}
