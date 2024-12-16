package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ArealDetails;
import cz.inspire.thesis.data.dto.sport.objekt.ArealLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import cz.inspire.thesis.data.model.sport.objekt.ArealLocEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ArealLocRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

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

    @Inject
    private ArealLocRepository arealLocRepository;

    @Transactional
    public String create(ArealDetails details) throws CreateException {
        try {
            ArealEntity entity = new ArealEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setPocetNavazujucichRez(details.getPocetNavazujucichRez());

            arealRepository.save(entity);
            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Areal entity", e);
        }
    }

    @Transactional
    public void postCreate(ArealDetails details, ArealEntity entity) throws CreateException {
        try {
            Map<String, ArealLocDetails> localeData = details.getLocaleData();
            if (localeData != null) {
                for (ArealLocDetails locDetails : localeData.values()) {
                    ArealLocEntity locEntity = new ArealLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                    arealLocRepository.save(locEntity);
                }
            }
            /**
             * This functionality of mapping "nadrazenyAreal" is missing in old bean,
             * but it makes sense to map it while creating new areal, because it is present
             * in setDetails and if getNadrazenyArealId is present while creating Areal
             */
            if (details.getNadrazenyArealId() != null) {
                ArealEntity parentEntity = arealRepository.findOptionalBy(details.getNadrazenyArealId())
                        .orElseThrow(() -> new CreateException("Parent Areal not found"));
                entity.setNadrazenyAreal(parentEntity);
            }

            arealRepository.save(entity);
        } catch (Exception e) {
            throw new CreateException("Failed in ejbPostCreate for Areal entity", e);
        }
    }

    public ArealDetails getDetails(ArealEntity entity) {
        ArealDetails details = new ArealDetails();
        details.setId(entity.getId());
        details.setPocetNavazujucichRez(entity.getPocetNavazujucichRez());

        Collection<ArealLocEntity> locEntities = entity.getLocaleData();
        Map<String, ArealLocDetails> locDetails = locEntities.stream()
                .collect(Collectors.toMap(ArealLocEntity::getJazyk, loc -> new ArealLocDetails(
                        loc.getId(), loc.getJazyk(), loc.getNazev(), loc.getPopis())));

        details.setLocaleData(locDetails);

        if (entity.getNadrazenyAreal() != null) {
            details.setNadrazenyArealId(entity.getNadrazenyAreal().getId());
        }

        return details;
    }

    @Transactional
    public void setDetails(ArealDetails details) throws ApplicationException {
        try {
            ArealEntity entity = arealRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Areal entity not found"));

            if (details.getLocaleData() != null) {
                List<ArealLocEntity> listArealLoc = new ArrayList<>();
                for (ArealLocDetails locDetails : details.getLocaleData().values()) {
                    ArealLocEntity locEntity = new ArealLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                    listArealLoc.add(locEntity);
                }
                entity.setLocaleData(listArealLoc);
            }

            // Update parent areal
            if (details.getNadrazenyArealId() == null) {
                entity.setNadrazenyAreal(null);
            } else {
                ArealEntity parentEntity = arealRepository.findOptionalBy(details.getNadrazenyArealId())
                        .orElseThrow(() -> new ApplicationException("Parent Areal not found"));
                entity.setNadrazenyAreal(parentEntity);
            }

            // Update other fields
            entity.setPocetNavazujucichRez(details.getPocetNavazujucichRez());

            arealRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Areal entity", e);
        }
    }

    /**
     * Calling these two methods from controller will need to be changed
     * Ideally by giving the methods parameter Id of Areal to be edited by adding
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

    public Collection<ArealDetails> findAll() {
        return arealRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ArealDetails> findByParent(String parentId, String jazyk) {
        return arealRepository.findByParent(parentId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ArealDetails> findRoot(String jazyk) {
        return arealRepository.findRoot(jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<String> getArealIdsByParent(String arealId){
        return arealRepository.findArealIdsByParent(arealId);
    }
}
