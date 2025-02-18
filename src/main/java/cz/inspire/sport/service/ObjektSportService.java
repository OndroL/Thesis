package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.repository.ObjektSportRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class ObjektSportService extends BaseService<ObjektSportEntity, ObjektSportPK, ObjektSportRepository> {

    public ObjektSportService() {
    }

    @Inject
    public ObjektSportService(ObjektSportRepository repository) {
        super(repository);
    }

    public List<ObjektSportEntity> findByObjekt(String objektId) throws FinderException {
        return wrapDBException(
                () -> repository.findByObjekt(objektId),
                "Error retrieving valid attributes from ObjektSportEntity"
        );
    }
}
