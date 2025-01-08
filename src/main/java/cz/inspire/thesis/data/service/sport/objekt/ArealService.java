package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ArealDetails;
import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.*;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 * Plus ApplicationException is additional Exception for update, see setDetails
 */
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;

/**
 * This is import of simple generateGUID functionality created to mimic real functionality
 * In your implementation use your import of guidGenerator
 */
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ArealService {

    @Inject
    private ArealRepository arealRepository;


    @Transactional
    public ArealEntity create(ArealDetails details) throws CreateException {
        try {
            ArealEntity entity = new ArealEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setPocetNavazujucichRez(details.getPocetNavazujucichRez());

            arealRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create Areal entity", e);
        }
    }

    @Transactional
    public void save(ArealEntity entity) throws ApplicationException {
        try {
            arealRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save ArealEntity", e);
        }
    }

    /**
     * Calling these two methods from controller will need to be changed
     * Ideally by giving the methods parameter Id of Areal to be edited by adding objekt/areal
     */
    @Transactional
    public void addObjekt(ObjektEntity objekt, String arealId) throws ApplicationException {
        ArealEntity entity = arealRepository.findOptionalBy(arealId)
                .orElseThrow(() -> new ApplicationException("Parent Areal not found"));
        Collection<ObjektEntity> newObjekty = entity.getObjekty();

        newObjekty.add(objekt);
        entity.setObjekty(newObjekty);

        arealRepository.save(entity);
    }
    @Transactional
    public void addAreal(ArealEntity areal, String arealId) throws ApplicationException {
        ArealEntity entity = arealRepository.findOptionalBy(arealId)
                .orElseThrow(() -> new ApplicationException("Parent Areal not found"));
        Collection<ArealEntity> newPodrazeneArealy = entity.getPodrazeneArealy();

        newPodrazeneArealy.add(areal);
        entity.setPodrazeneArealy(newPodrazeneArealy);

        arealRepository.save(entity);
    }

    public Collection<ArealEntity> findAll() {
        return arealRepository.findAll();
    }

    public Collection<ArealEntity> findByParent(String parentId, String jazyk) {
        return arealRepository.findByParent(parentId, jazyk);
    }

    public Collection<ArealEntity> findRoot(String jazyk) {
        return arealRepository.findRoot(jazyk);
    }

    public Collection<String> getArealIdsByParent(String arealId){
        return arealRepository.findArealIdsByParent(arealId);
    }

    public Optional<ArealEntity> findOptionalBy (String objektId) {
        return arealRepository.findOptionalBy(objektId);
    }

    public ArealEntity findById(String id) throws ApplicationException {
            return arealRepository.findOptionalBy(id)
                    .orElseThrow(() -> new ApplicationException("Areal not found with id : " + id));
    }
}
