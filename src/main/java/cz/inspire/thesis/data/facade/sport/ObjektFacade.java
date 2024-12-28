package cz.inspire.thesis.data.facade.sport;

import cz.inspire.thesis.data.dto.sport.objekt.*;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.service.sport.objekt.*;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ObjektFacade {
    @Inject
    private ObjektService objektService;

    @Inject
    private ArealService arealService;

    @Inject
    private ObjektLocService objektLocService;

    @Inject
    private OvladacObjektuService ovladacObjektuService;

    @Inject
    private PodminkaRezervaceService podminkaRezervaceService;

    @Inject
    private SportService sportService;

    @Inject
    private ObjektSportService objektSportService;

    public String create(ObjektDetails objektDetails) throws CreateException {
        try {
            ObjektEntity objektEntity = objektService.create(objektDetails);

            // PostCreate functionality
            objektService.setNadobjekty(objektDetails, objektEntity);
            objektService.setPodobjekty(objektDetails, objektEntity);

            setCinnosti(getSports(objektEntity), objektEntity, objektDetails);
            setOvladaceObjektu(objektDetails, objektEntity);
            setPodminkyRezervaciInternal(objektDetails, objektEntity);


            return objektEntity.getId();
        } catch (Exception ex) {
            throw new CreateException("Failed to complete create operations for Objekt: " + ex.getMessage(), ex);
        }
    }

    public String create(ArealDetails details) throws CreateException {
        try {
            ArealEntity entity = arealService.create(details);

            Map<String, ArealLocDetails> localeData = details.getLocaleData();
            if (localeData != null) {
                for (ArealLocDetails locDetails : localeData.values()) {
                    ArealLocEntity locEntity = new ArealLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                }
            }
            /**
             * This functionality of mapping "nadrazenyAreal" is missing in old bean,
             * but it makes sense to map it while creating new areal, when it is present
             * in setDetails and if getNadrazenyArealId is existing in DB
             */
            if (details.getNadrazenyArealId() != null) {
                ArealEntity parentEntity = arealService.findById(details.getNadrazenyArealId());
                entity.setNadrazenyAreal(parentEntity);
            }

            arealService.save(entity);

            return entity.getId();

        } catch (Exception ex) {
            throw new CreateException("Failed to complete create operations for Areal: " + ex.getMessage(), ex);
        }
    }

    public void setDetails(ObjektDetails details) throws ApplicationException {
        try {
            ObjektEntity entity = objektService.findById(details.getId());

            setEntityAttributes(entity, details);
            setLocaleData(details, entity);

            // Areal
            try {
                ArealEntity newAreal = arealService.findOptionalBy(details.getAreal().getId())
                        .orElseThrow(() -> new ApplicationException("Areal not found for ID: " + details.getAreal().getId()));

                entity.setAreal(newAreal);
            } catch (Exception e) {
                throw new ApplicationException("Failed to update Objekt entity with new Areal", e);
            }

            // sports
            setSports(getSports(entity), entity, details);


            //Ovladani
            try {
                Collection<OvladacObjektuEntity> ovladaceEntity = ovladacObjektuService.findByObjekt(entity.getId());
                String ovladacId;
                for (OvladacObjektuEntity localOvl : ovladaceEntity) {
                    ovladacId = localOvl.getId();
                    ovladacObjektuService.remove(ovladacId);
                }
            } catch (Exception e) {
                throw new ApplicationException("Could not remove ovladace from object: " , e);
            }
            setOvladaceObjektu(details, entity);

            objektService.setNadobjekty(details, entity);
            objektService.setPodobjekty(details, entity);
            setPodminkyRezervaciInternal(details, entity);
            entity.setGoogleCalendarId(details.getGoogleCalendarId());
            entity.setGoogleCalendarNotification(details.isGoogleCalendarNotification());
            entity.setGoogleCalendarNotificationBefore(details.getGoogleCalendarNotificationBefore());

            objektService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Objekt entity", e);
        }
    }

    public void setDetails(ArealDetails details) throws ApplicationException {
        try {
            ArealEntity entity = arealService.findById(details.getId());

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
                ArealEntity parentEntity = arealService.findOptionalBy(details.getNadrazenyArealId())
                        .orElseThrow(() -> new ApplicationException("Parent Areal set but not found with id : " + details.getNadrazenyArealId()));
                entity.setNadrazenyAreal(parentEntity);
            }

            // Update other fields
            entity.setPocetNavazujucichRez(details.getPocetNavazujucichRez());

            arealService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Areal entity", e);
        }
    }

    private void setCinnosti(List<SportDetails> sportList, ObjektEntity objektEntity, ObjektDetails entityDetails) throws ApplicationException {
        try {
            // Add new sports
            if (sportList != null && !sportList.isEmpty()) {
                int index = 0;
                for (SportDetails sportDetails : sportList) {
                    ObjektSportEntity newSport = new ObjektSportEntity();
                    newSport.setId(new ObjektSportPK(UUID.randomUUID().toString(), index++));

                    // Associate sport and object
                    SportEntity sportEntity = new SportEntity();
                    sportEntity.setId(sportDetails.getId());
                    newSport.setSport(sportEntity);
                    newSport.setObjekt(objektEntity);
                    // Just magic, need to save new objektSport Entity into DB, but create from ObjektSportService is expecting DTO
                    // so work around is to call getDetails from service with newly created objektSportEntity
                    // Here cascade persist would make a lot of sense by saving the objektEntity with objektSportEntity in it.
                    objektSportService.create(objektSportService.getDetails(newSport));

                    // Set new ObjektSportEntity to details of ObjektEntity
                    entityDetails.getObjektSport().add((objektSportService.getDetails(newSport)));

                    // Set new ObjektSportEntity to ObjektEntity
                    objektEntity.getObjektSports().add(newSport);
                }
            }
        } catch (Exception ex) {
            throw new ApplicationException("Failed to set ObjektSports for ObjektEntity with ID: " + objektEntity.getId(), ex);
        }
    }

    /**
     * Calling these two methods from controller will need to be changed
     * Ideally by giving the methods parameter Id of Areal to be edited by adding objekt/areal
     */
    public void addObjekt(ObjektEntity objekt, String arealId) throws ApplicationException {
        arealService.addObjekt(objekt, arealId);
    }
    public void addAreal(ArealEntity areal, String arealId) throws ApplicationException {
        arealService.addAreal(areal, arealId);
    }

    private void setOvladaceObjektu(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getOvladaceObjektu() != null) {
            Collection<OvladacObjektuEntity> ovladaceEntities = objektDetails.getOvladaceObjektu().stream()
                    .map(controllerDetails -> {
                        try {
                            return ovladacObjektuService.findOptionalBy(controllerDetails.getId())
                                    .orElseThrow(() -> new ApplicationException("Controller not found for ID: " + controllerDetails.getId()));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setOvladaceObjektu(ovladaceEntities);
        }
    }

    private void setPodminkyRezervaciInternal(ObjektDetails objektDetails, ObjektEntity objektEntity) throws ApplicationException {
        try {
            // Clear existing conditions
            Collection<PodminkaRezervaceEntity> existingConditions = objektEntity.getPodminkyRezervaci();
            if (existingConditions != null && !existingConditions.isEmpty()) {
                existingConditions.clear();
            }

            // Add new conditions
            List<PodminkaRezervaceDetails> newConditions = objektDetails.getPodminkyRezervaci();
            if (newConditions != null && !newConditions.isEmpty()) {
                long priority = 0;
                for (PodminkaRezervaceDetails conditionDetails : newConditions) {
                    PodminkaRezervaceEntity newCondition = new PodminkaRezervaceEntity();
                    newCondition.setId(UUID.randomUUID().toString());
                    newCondition.setName(conditionDetails.getName());
                    newCondition.setPriorita(priority++);
                    newCondition.setObjekt(objektEntity);
                    newCondition.setObjektRezervaceId(conditionDetails.getObjektRezervaceId());
                    newCondition.setObjektRezervaceObsazen(conditionDetails.getObjektRezervaceObsazen());

                    podminkaRezervaceService.create(podminkaRezervaceService.getDetails(newCondition));

                    objektEntity.getPodminkyRezervaci().add(newCondition);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Cannot set reservation conditions for object " + objektEntity.getId(), e);
        }
    }


    public void setSports(List<SportDetails> sports, ObjektEntity entity, ObjektDetails details) throws ApplicationException {
        Collection<ObjektSportEntity> existingSports = entity.getObjektSports();

        for(ObjektSportEntity objektSport : existingSports) {
            try {
                objektSportService.remove(objektSport.getId());

            } catch (Exception ex){
                throw new ApplicationException("Failed to remove ObjektSports for ObjektEntity with ID: " + entity.getId(), ex);
            }
            existingSports.clear();
        }
        setCinnosti(sports, entity, details);
    }

    public void setLocaleData(ObjektDetails details, ObjektEntity entity) throws CreateException {
        Map<String, ObjektLocDetails> localeData = details.getLocaleData();
        Collection<ObjektLocEntity> newLocData = new ArrayList<>();
        if (localeData != null) {
            for (ObjektLocDetails locDetails : localeData.values()) {
                ObjektLocEntity locEntity = new ObjektLocEntity();
                locEntity.setId(generateGUID(locEntity));
                locEntity.setJazyk(locDetails.getJazyk());
                locEntity.setNazev(locDetails.getNazev());
                locEntity.setPopis(locDetails.getPopis());
                locEntity.setZkracenyNazev(locDetails.getZkracenyNazev());
                newLocData.add(locEntity);

            }
        }
        entity.setLocaleData(newLocData);
    }

    public void setEntityAttributes(ObjektEntity entity, ObjektDetails details) {
        entity.setKapacita(details.getKapacita());
        entity.setCasovaJednotka(details.getCasovaJednotka());
        entity.setTypRezervace(details.getTypRezervace());
        entity.setPrimyVstup(details.isPrimyVstup());
        entity.setMinDelkaRezervace(details.getMinDelkaRezervace());
        entity.setMaxDelkaRezervace(details.getMaxDelkaRezervace());
        entity.setVolnoPredRezervaci(details.getVolnoPredRezervaci());
        entity.setVolnoPoRezervaci(details.getVolnoPoRezervaci());
        entity.setZarovnatZacatekRezervace(details.getZarovnatZacatekRezervace());
        entity.setDelkaRezervaceNasobkem(details.getDelkaRezervaceNasobkem());
        entity.setVicestavovy(details.getVicestavovy());
        entity.setStav(details.getStav());
        entity.setReservationStart(details.getReservationStart());
        entity.setReservationFinish(details.getReservationFinish());
        entity.setOdcitatProcedury(details.isOdcitatProcedury());
        entity.setRezervaceNaTokeny(details.isRezervaceNaTokeny());
        entity.setRucniUzavreniVstupu(details.isRucniUzavreniVstupu());
        entity.setUpraveniCasuVstupu(details.isUpraveniCasuVstupu());
        entity.setPozastavitVstup(details.isPozastavitVstup());
        entity.setShowProgress(details.isShowProgress());
        entity.setCheckTokensCount(details.isCheckTokensCount());
        entity.setSelectInstructor(details.isSelectInstructor());
        entity.setShowInstructorName(details.isShowInstructorName());
        entity.setShowSportName(details.isShowSportName());
        entity.setVytvoreniRezervacePredZacatkem(details.getVytvoreniRezervacePredZacatkem());
        entity.setEditaceRezervacePredZacatkem(details.getEditaceRezervacePredZacatkem());
        entity.setZruseniRezervacePredZacatkem(details.getZruseniRezervacePredZacatkem());
    }

    public ObjektDetails getDetails(ObjektEntity entity) {
        ObjektDetails details = getDetailsWithoutSports(entity);
        details.setObjektSport(entity.getObjektSports().stream().map(objektSportService::getDetails).toList());
        // I don't really understand the need to call set sports for getting the details
        // maybe some hidden functionality is of old bean is not clear to me
        // setSports(getSports(entity), entity, details);
        return details;
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

    public List<SportDetails> getSports(ObjektEntity entity) {
        return entity.getObjektSports().stream()
                .sorted(OBJEKT_SPORT_COMPARATOR)
                .map(objektSport -> sportService.getDetails(objektSport.getSport()))
                .collect(Collectors.toList());
    }

    private Set<String> getObjektIds(Collection<ObjektEntity> objekty) {
        Set<String> ret = Collections.synchronizedSet(new HashSet<String>());
        for (ObjektEntity localObjekt : objekty) {
            ret.add(localObjekt.getId());
        }
        return ret;
    }

    public ObjektDetails getDetailsWithoutSports(ObjektEntity entity) {
        ObjektDetails details = new ObjektDetails();
        details.setId(entity.getId());
        details.setKapacita(entity.getKapacita());
        details.setCasovaJednotka(entity.getCasovaJednotka());
        details.setTypRezervace(entity.getTypRezervace());
        details.setPrimyVstup(entity.isPrimyVstup());
        details.setMinDelkaRezervace(entity.getMinDelkaRezervace());
        details.setMaxDelkaRezervace(entity.getMaxDelkaRezervace());
        details.setVolnoPredRezervaci(entity.getVolnoPredRezervaci());
        details.setVolnoPoRezervaci(entity.getVolnoPoRezervaci());
        details.setZarovnatZacatekRezervace(entity.getZarovnatZacatekRezervace());
        details.setDelkaRezervaceNasobkem(entity.getDelkaRezervaceNasobkem());
        details.setVicestavovy(entity.getVicestavovy());
        details.setStav(entity.getStav());
        details.setReservationStart(entity.getReservationStart());
        details.setReservationFinish(entity.getReservationFinish());
        details.setOdcitatProcedury(entity.isOdcitatProcedury());
        details.setRezervaceNaTokeny(entity.isRezervaceNaTokeny());
        details.setRucniUzavreniVstupu(entity.isRucniUzavreniVstupu());
        details.setUpraveniCasuVstupu(entity.isUpraveniCasuVstupu());
        details.setPozastavitVstup(entity.isPozastavitVstup());
        details.setShowProgress(entity.isShowProgress());
        details.setCheckTokensCount(entity.isCheckTokensCount());
        details.setSelectInstructor(entity.isSelectInstructor());
        details.setShowInstructorName(entity.isShowInstructorName());
        details.setShowSportName(entity.isShowSportName());


        details.setAreal(getDetails(entity.getAreal()));

        Map<String, ObjektLocDetails> locData = entity.getLocaleData().stream()
                .collect(Collectors.toMap(
                        locEntity -> objektLocService.getDetails(locEntity).getJazyk(), // Key: `Jazyk`
                        objektLocService::getDetails   // Value: `ObjektLocDetails`
                ));

        details.setLocaleData(locData);

        List<OvladacObjektuDetails> ovladaceDetails = entity.getOvladaceObjektu().stream()
                .map(ovladacObjektuService::getDetails)
                .collect(Collectors.toList());
        details.setOvladaceObjektu(ovladaceDetails);


        details.setNadobjekty(getObjektIds(entity.getNadobjekty()));
        details.setPodobjekty(getObjektIds(entity.getPodobjekty()));
        details.setPodminkyRezervaci(getPodminkyRezervaciInternal(entity.getPodminkyRezervaci(), entity));
        details.setGoogleCalendarId(entity.getGoogleCalendarId());
        details.setGoogleCalendarNotification(entity.isGoogleCalendarNotification());
        details.setGoogleCalendarNotificationBefore(entity.getGoogleCalendarNotificationBefore());
        details.setVytvoreniRezervacePredZacatkem(entity.getVytvoreniRezervacePredZacatkem());
        details.setEditaceRezervacePredZacatkem(entity.getEditaceRezervacePredZacatkem());
        details.setZruseniRezervacePredZacatkem(entity.getZruseniRezervacePredZacatkem());

        return details;
    }

    private List<PodminkaRezervaceDetails> getPodminkyRezervaciInternal(
            Collection<PodminkaRezervaceEntity> podminkaEntities,
            ObjektEntity objektEntity) {

        if (podminkaEntities == null || podminkaEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return podminkaEntities.stream()
                .map(podminkaEntity -> {
                    PodminkaRezervaceDetails details = podminkaRezervaceService.getDetailsWithoutObjectId(podminkaEntity);
                    details.setObjektId(objektEntity.getId());
                    return details;
                })
                .sorted(PODMINKY_REZERVACI_COMPARATOR)
                .collect(Collectors.toList());
    }

    ////////////////////////
    /////// Queries ///////
    //////////////////////


    public Collection<ObjektDetails> findAll() {
        return objektService.findAll().stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk) {
        return objektService.findByAreal(arealId, jazyk).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk) {
        return objektService.findBaseByAreal(arealId, jazyk).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk, int offset, int count) {
        return objektService.findByAreal(arealId, jazyk, offset, count).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findByTypRezervace(Integer typRezervace, String jazyk) {
        return objektService.findByTypRezervace(typRezervace, jazyk).stream().map(this::getDetails).toList();
    }


    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk, int offset, int count) {
        return objektService.findBaseByAreal(arealId, jazyk, offset, count).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findBySport(String sportId, String jazyk) {
        return objektService.findBySport(sportId, jazyk).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, boolean primyVstup) {
        return objektService.findByPrimyVstup(jazyk, primyVstup).stream().map(this::getDetails).toList();
    }

    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) {
        return objektService.findByPrimyVstup(jazyk, offset, count, primyVstup).stream().map(this::getDetails).toList();
    }

    public Collection<String> getObjektIdsOfAreal(String arealId) {
        return new ArrayList<>(objektService.getObjektIdsOfAreal(arealId));
    }

    // Also this was renamed from findAll() to findAllAreals()
    public Collection<ArealDetails> findAllAreals() {
        return arealService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ArealDetails> findByParent(String parentId, String jazyk) {
        return arealService.findByParent(parentId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ArealDetails> findRoot(String jazyk) {
        return arealService.findRoot(jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<String> getArealIdsByParent(String arealId){
        return arealService.getArealIdsByParent(arealId);
    }


    /**
     * Here are modernized comparators for Sport and Podminka rezervace from old bean
     * Modern Java standards allow for cleaner and simpler implementation of comparators,
     * by using lambda expressions and method references introduced in Java 8.
     */
    private static final Comparator<ObjektSportEntity> OBJEKT_SPORT_COMPARATOR =
            Comparator.comparingInt(entity -> entity.getId().getIndex());

    private static final Comparator<PodminkaRezervaceDetails> PODMINKY_REZERVACI_COMPARATOR =
            Comparator.comparing(PodminkaRezervaceDetails::getPriorita);

}
