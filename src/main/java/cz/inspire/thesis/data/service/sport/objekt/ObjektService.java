package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektDetails;
import cz.inspire.thesis.data.dto.sport.objekt.OvladacObjektuDetails;
import cz.inspire.thesis.data.dto.sport.objekt.PodminkaRezervaceDetails;
import cz.inspire.thesis.data.dto.sport.objekt.ObjektLocDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektRepository;
import cz.inspire.thesis.data.repository.sport.objekt.OvladacObjektuRepository;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.SneakyThrows;
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
public class ObjektService {

    @Inject
    private ObjektRepository objektRepository;

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

    @Transactional
    public String create(ObjektDetails details) throws CreateException {
        try {
            ObjektEntity entity = new ObjektEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);
            setLocaleData(details, entity);
            objektRepository.save(entity);
            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Objekt entity", e);
        }
    }

    @Transactional
    public void postCreate(ObjektDetails objektDetails, ObjektEntity objektEntity) throws CreateException {
        try {
            setLocaleData(objektDetails, objektEntity);
            setCinnosti(getSports(objektEntity), objektEntity, objektDetails);
            setOvladaceObjektu(objektDetails, objektEntity);
            setNadobjekty(objektDetails, objektEntity);
            setPodobjekty(objektDetails, objektEntity);
            setPodminkyRezervaciInternal(objektDetails, objektEntity);

            // Save the updated entity after handling all relationships
            objektRepository.save(objektEntity);
        } catch (Exception e) {
            throw new CreateException("Failed to complete post-create operations for Objekt: " + e.getMessage(), e);
        }
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

    @Transactional
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

    private void setNadobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity)  {
        if (objektDetails.getNadobjekty() != null) {
            Collection<ObjektEntity> nadobjekts = objektDetails.getNadobjekty().stream()
                    .map(nadId -> {
                        try {
                            return objektRepository.findOptionalBy(nadId)
                                    .orElseThrow(() -> new ApplicationException("Nadobjekt not found for ID: " + nadId));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setNadobjekty(nadobjekts);
        }
    }

    private void setPodobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getPodobjekty() != null) {
            Collection<ObjektEntity> podobjekts = objektDetails.getPodobjekty().stream()
                    .map(podId -> {
                        try {
                            return objektRepository.findOptionalBy(podId)
                                    .orElseThrow(() -> new ApplicationException("Podobjekt not found for ID: " + podId));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setPodobjekty(podobjekts);
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
                    objektEntity.getPodminkyRezervaci().add(newCondition);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Cannot set reservation conditions for object " + objektEntity.getId(), e);
        }
    }


    @Transactional
    public void setDetails(ObjektDetails details) throws ApplicationException {
        try {
            ObjektEntity entity = objektRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Objekt entity not found"));

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

            setNadobjekty(details, entity);
            setPodobjekty(details, entity);
            setPodminkyRezervaciInternal(details, entity);
            entity.setGoogleCalendarId(details.getGoogleCalendarId());
            entity.setGoogleCalendarNotification(details.isGoogleCalendarNotification());
            entity.setGoogleCalendarNotificationBefore(details.getGoogleCalendarNotificationBefore());

            objektRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Objekt entity", e);
        }
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


        details.setAreal(arealService.getDetails(entity.getAreal()));

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

    public List<SportDetails> getSports(ObjektEntity entity) {
        return entity.getObjektSports().stream()
                .sorted(OBJEKT_SPORT_COMPARATOR)
                .map(objektSport -> sportService.getDetails(objektSport.getSport()))
                .collect(Collectors.toList());
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

    private Set<String> getObjektIds(Collection<ObjektEntity> objekty) {
        Set<String> ret = Collections.synchronizedSet(new HashSet<String>());
        for (ObjektEntity localObjekt : objekty) {
            ret.add(localObjekt.getId());
        }
        return ret;
    }

    private void setEntityAttributes(ObjektEntity entity, ObjektDetails details) {
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

    public ObjektDetails getDetails(ObjektEntity entity) throws ApplicationException {
        ObjektDetails details = getDetailsWithoutSports(entity);
        setSports(getSports(entity), entity, details);
        return details;
    }

    // @SneakyThrow will avoid javac's insistence that you either catch or throw onward any checked exceptions that statements in your method body declare they generate.
    // It's here because getDatails is using setSports which throws exceptions because it's functionality of setCinnosti is doubled for setDetails and getDetails
    // I would rewritte this functionality entirely
    @SneakyThrows
    public Collection<ObjektDetails> findAll() {
        return objektRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk) {
        return objektRepository.findByAreal(arealId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk) {
        return objektRepository.findBaseByAreal(arealId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findByAreal(arealId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findByTypRezervace(Integer typRezervace, String jazyk) {
        return objektRepository.findByTypRezervace(typRezervace, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findBaseByAreal(arealId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findBySport(String sportId, String jazyk) {
        return objektRepository.findBySport(sportId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, primyVstup).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, offset, count, primyVstup).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<String> getObjektIdsOfAreal(String arealId) {
        return new ArrayList<>(objektRepository.findObjektIdsOfAreal(arealId));
    }

    public Optional<ObjektEntity> findOptionalBy(String objektId) {
        return objektRepository.findOptionalBy(objektId);
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
