package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.repository.ObjektRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class ObjektService extends BaseService<ObjektEntity, String, ObjektRepository> {

    public ObjektService() {
    }

    @Inject
    public ObjektService(ObjektRepository repository) { super(repository); }


    public List<ObjektEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all ObjektEntity records, Ordered by Id"
        );
    }

    public List<ObjektEntity> findByAreal(String arealId, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findByAreal(arealId, jazyk),
                "Error retrieving ObjektEntity records for arealId = " + arealId + ", jazyk = " + jazyk
        );
    }

    public List<ObjektEntity> findBaseByAreal(String arealId, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findBaseByAreal(arealId, jazyk),
                "Error retrieving base ObjektEntity records for arealId = " + arealId + ", jazyk = " + jazyk
        );
    }

    public List<ObjektEntity> findByAreal(String arealId, String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByArealWithLimit(arealId, jazyk, count, offset),
                "Error retrieving ObjektEntity records for arealId = " + arealId + ", jazyk = " + jazyk +
                        " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }

    public List<ObjektEntity> findByTypRezervace(Integer typRezervace, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findByTypRezervace(typRezervace, jazyk),
                "Error retrieving ObjektEntity records for typRezervace = " + typRezervace + ", jazyk = " + jazyk
        );
    }

    public List<ObjektEntity> findBaseByAreal(String arealId, String jazyk, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findBaseByArealWithLimit(arealId, jazyk, count, offset),
                "Error retrieving base ObjektEntity records for arealId = " + arealId + ", jazyk = " + jazyk +
                        " with pagination (offset  = " + offset + ", count = " + count + ")"
        );
    }

    public List<ObjektEntity> findBySport(String arealId, String jazyk) throws FinderException {
        return wrapDBException(
                () -> repository.findBySport(arealId, jazyk),
                "Error retrieving ObjektEntity records by sport for arealId = " + arealId + ", jazyk = " + jazyk
        );
    }

    public List<ObjektEntity> findByPrimyVstup(String jazyk, boolean primyVstup) throws FinderException {
        return wrapDBException(
                () -> repository.findByPrimyVstup(jazyk, primyVstup),
                "Error retrieving ObjektEntity records for jazyk = " + jazyk + ", primyVstup = " + primyVstup
        );
    }

    public List<ObjektEntity> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) throws FinderException {
        return wrapDBException(
                () -> repository.findByPrimyVstupWithLimit(jazyk, count, offset, primyVstup),
                "Error retrieving ObjektEntity records for jazyk = " + jazyk + ", primyVstup = " + primyVstup +
                        " with pagination (offset = " + offset + ", count = " + count + ")"
        );
    }
}
