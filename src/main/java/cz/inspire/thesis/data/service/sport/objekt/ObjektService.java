package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektDetails;
import cz.inspire.thesis.data.dto.sport.objekt.OvladacObjektuDetails;
import cz.inspire.thesis.data.dto.sport.objekt.PodminkaRezervaceDetails;
import cz.inspire.thesis.data.dto.sport.objekt.ObjektLocDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektLocRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektRepository;
import cz.inspire.thesis.data.repository.sport.objekt.OvladacObjektuRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import cz.inspire.thesis.data.service.sport.sport.SportService;
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
public class ObjektService {

    @Inject
    private SportRepository sportRepository;

    @Inject
    private OvladacObjektuRepository ovladacRepository;

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
            setCinnosti(objektDetails, objektEntity);
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


    private void setCinnosti(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getSports() != null) {
            Collection<SportEntity> sports = objektDetails.getSports().stream()
                    .map(sportDetails -> {
                        try {
                            return sportRepository.findOptionalBy(sportDetails.getId())
                                    .orElseThrow(() -> new ApplicationException("Sport not found for ID: " + sportDetails.getId()));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setSports(sports);
        }
    }

    private void setOvladaceObjektu(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getOvladaceObjektu() != null) {
            Collection<OvladacObjektuEntity> ovladaceEntities = objektDetails.getOvladaceObjektu().stream()
                    .map(controllerDetails -> {
                        try {
                            return ovladacRepository.findOptionalBy(controllerDetails.getId())
                                    .orElseThrow(() -> new ApplicationException("Controller not found for ID: " + controllerDetails.getId()));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setOvladaceObjektu(ovladaceEntities);
        }
    }

    private void setNadobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getNadobjekty() != null) {
            Collection<ObjektEntity> nadobjekts = objektDetails.getNadobjekty().stream()
                    .map(nadDetails -> objektRepository.findOptionalBy(nadDetails.getId())
                            .orElseThrow(() -> new ApplicationException("Nadobjekt not found for ID: " + nadDetails.getId())))
                    .collect(Collectors.toSet());
            objektEntity.setNadobjekty(nadobjekts);
        }
    }

    private void setPodobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getPodobjekty() != null) {
            Collection<ObjektEntity> podobjekts = objektDetails.getPodobjekty().stream()
                    .map(podDetails -> objektRepository.findOptionalBy(podDetails.getId())
                            .orElseThrow(() -> new ApplicationException("Podobjekt not found for ID: " + podDetails.getId())))
                    .collect(Collectors.toSet());
            objektEntity.setPodobjekty(podobjekts);
        }
    }

    private void setPodminkyRezervaciInternal(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getPodminkyRezervaci() != null) {
            objektEntity.setPodminkyRezervaci(mapToReservationConditions(objektDetails.getPodminkyRezervaci()));
        }
    }

    private Collection<ReservationConditionEntity> mapToReservationConditions(Collection<ReservationConditionDetails> details) {
        return details.stream()
                .map(condition -> {
                    ReservationConditionEntity entity = new ReservationConditionEntity();
                    entity.setId(generateGUID(entity)); // Generate unique ID
                    entity.setCondition(condition.getCondition());
                    // Map other fields if necessary
                    return entity;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void setDetails(ObjektDetails details) throws ApplicationException {
        try {
            ObjektEntity entity = objektRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Objekt entity not found"));

            setEntityAttributes(entity, details);

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

    public void setSports(List<SportDetails> sports) {
        Iterator<ObjektSportEntity> it = getObjektSports().iterator();
        while (it.hasNext()) {
            ObjektSportEntity objektSport = it.next();
            try {
                objektSport.remove();
            } catch (Exception ex) {
                logger.error("Nepodarilo se odstranit ObjektSport!", ex);
            }
        }

        setCinnosti(sports);
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

    public ObjektDetails getDetails(ObjektEntity entity) {
        ObjektDetails details = getDetailsWithoutSports(entity);
        details.setSports(getSports(ObjektEntity entity));
        return details;
    }

    public Collection<ObjektDetails> findAll() {
        return objektRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk) {
        return objektRepository.findByAreal(arealId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk) {
        return objektRepository.findBaseByAreal(arealId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findByAreal(arealId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findByTypRezervace(Integer typRezervace, String jazyk) {
        return objektRepository.findByTypRezervace(typRezervace, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findBaseByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findBaseByAreal(arealId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findBySport(String sportId, String jazyk) {
        return objektRepository.findBySport(sportId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, primyVstup).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektDetails> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, offset, count, primyVstup).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<String> getObjektIdsOfAreal(String arealId) {
        return new ArrayList<>(objektRepository.findObjektIdsOfAreal(arealId));
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
