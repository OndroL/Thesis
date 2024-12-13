package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ArealDetails;
import cz.inspire.thesis.data.dto.sport.objekt.ArealLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import cz.inspire.thesis.data.model.sport.objekt.ArealLocEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ArealLocRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ArealService {

    @Inject
    private ArealRepository arealRepository;

    @Inject
    private ArealLocRepository arealLocRepository;

    public String ejbCreate(ArealDetails details) throws CreateException {
        try {
            ArealEntity entity = new ArealEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setPocetNavazujucichRez(details.getPocetNavazujucichRez());

            arealRepository.save(entity);
            ejbPostCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Areal entity", e);
        }
    }

    public void ejbPostCreate(ArealDetails details, ArealEntity entity) throws CreateException {
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

        List<ArealLocEntity> locEntities = entity.getLocaleData();
        Map<String, ArealLocDetails> locDetails = locEntities.stream()
                .collect(Collectors.toMap(ArealLocEntity::getJazyk, loc -> new ArealLocDetails(
                        loc.getId(), loc.getJazyk(), loc.getNazev(), loc.getPopis())));

        details.setLocaleData(locDetails);

        if (entity.getNadrazenyAreal() != null) {
            details.setNadrazenyArealId(entity.getNadrazenyAreal().getId());
        }

        return details;
    }

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

    public Collection<String> ejbHomeGetArealIdsByParent(String arealId) throws ApplicationException {
        try {
            return arealRepository.findArealIdsByParent(arealId);
        } catch (Exception e) {
            throw new ApplicationException("Failed to retrieve Areal IDs by parent", e);
        }
    }
}
