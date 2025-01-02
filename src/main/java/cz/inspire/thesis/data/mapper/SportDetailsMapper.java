package cz.inspire.thesis.data.mapper;

import cz.inspire.thesis.data.dto.sport.sport.*;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.*;
import cz.inspire.thesis.data.service.sport.sport.SportLocService;
import cz.inspire.thesis.data.utils.SazbaStorna;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.InstructorConstants.ZADNY_INSTRUKTOR_ID;

@ApplicationScoped
public class SportDetailsMapper {

    @Inject
    private SportLocService sportLocService;

    public SportDetails getDetails(SportEntity entity) {
        SportDetails details = new SportDetails();
        details.setId(entity.getId());
        details.setTyp(entity.getTyp());
        details.setZboziId(entity.getZboziId());
        details.setSkladId(entity.getSkladId());
        details.setPodSportyCount(entity.getPodSportyCount());
        details.setSazbaJednotek(entity.getSazbaJednotek());
        details.setSazbaNaOsobu(entity.getSazbaNaOsobu());
        details.setSazbaNaCas(entity.getSazbaNaCas());
        details.setUctovatZalohu(entity.getUctovatZalohu());
        details.setSazbyStorna((List<SazbaStorna>) entity.getSazbyStorna());
        details.setMinDelkaRezervace(entity.getMinDelkaRezervace());
        details.setMaxDelkaRezervace(entity.getMaxDelkaRezervace());
        details.setObjednavkaZaplniObjekt(entity.getObjednavkaZaplniObjekt());
        details.setMaximalniPocetOsobNaZakaznika(entity.getMaximalniPocetOsobNaZakaznika());
        details.setDelkaRezervaceNasobkem(entity.getDelkaRezervaceNasobkem());

        details.setBarvaPopredi(entity.getBarvaPopredi());
        details.setBarvaPozadi(entity.getBarvaPozadi());
        details.setZobrazitText(entity.getZobrazitText());
        details.setViditelnyWeb(entity.getViditelnyWeb());

        details.setNavRezervaceOffset(entity.getNavRezervaceOffset());
        details.setDelkaHlavniRez(entity.getDelkaHlavniRez());

        details.setMinimalniPocetOsob(entity.getMinimalniPocetOsob());
        details.setMinutyPredVyhodnocenimKapacity(entity.getMinutyPredVyhodnocenimKapacity());

        if (entity.getSportKategorie() != null) {
            details.setSportKategorie(getDetails(entity.getSportKategorie()));

        }

        Map<String, SportLocDetails> locData = entity.getLocaleData().stream()
                .map(sportLocService::getDetails)
                .collect(Collectors.toMap(SportLocDetails::getJazyk, sportLocDetails -> sportLocDetails));

        // Set the locale data into the SportDetails object
        details.setLocaleData(locData);

        SportEntity navazujiciSport = entity.getNavazujiciSport();
        if (navazujiciSport != null){
            details.setNavazujiciSportId(navazujiciSport.getId());
        }

        ActivityEntity activity = entity.getActivity();
        if (activity != null) {
            details.setActivityId(activity.getId());
        }

//        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = loadInstructors(entity);
        entity.setInstructorSet(instructors);
        details.setInstructors(instructors);

        return details;

    }

    public Set<InstructorDetails> loadInstructors(SportEntity entity) {
        //        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = new HashSet<InstructorDetails>();
        Collection<SportInstructorEntity> sportInstructors = entity.getSportInstructors();
        for (SportInstructorEntity localSportInstructor : sportInstructors) {
            if (localSportInstructor.getDeleted()) {
                continue;
            }
            InstructorEntity instructor = localSportInstructor.getInstructor();
            if (instructor == null) {
                //add special instructor standing for none instructor
                InstructorDetails noneInstructor = new InstructorDetails();
                noneInstructor.setId(ZADNY_INSTRUKTOR_ID);
                instructors.add(noneInstructor);
            } else {
                instructors.add(mapInstructorData(instructor));
            }
        }
        return instructors;
    }

    public InstructorDetails mapInstructorData(InstructorEntity instructorEntity) {
        InstructorDetails instructorDetails = new InstructorDetails();
        instructorDetails.setId(instructorEntity.getId());
        instructorDetails.setColor(instructorEntity.getColor());
        instructorDetails.setDeleted(instructorEntity.getDeleted());
        instructorDetails.setEmail(instructorEntity.getEmail());
        instructorDetails.setFirstName(instructorEntity.getFirstName());
        instructorDetails.setLastName(instructorEntity.getLastName());
        instructorDetails.setPhoneCode(instructorEntity.getPhoneCode());
        instructorDetails.setPhoneNumber(instructorEntity.getPhoneNumber());
        instructorDetails.setGoogleCalendarId(instructorEntity.getGoogleCalendarId());
        instructorDetails.setGoogleCalendarNotification(instructorEntity.getGoogleCalendarNotification());
        instructorDetails.setGoogleCalendarNotificationBefore(instructorEntity.getGoogleCalendarNotificationBefore());
//        instructorDetails.setPhoto(instructorEntity.getPhoto());
        return instructorDetails;
    }

    public SportKategorieDetails getDetails(SportKategorieEntity entity) {
        SportKategorieDetails details = new SportKategorieDetails();
        details.setId(entity.getId());
        details.setMultisportFacilityId(entity.getMultiSportFacilityId());
        details.setMultisportServiceUUID(entity.getMultiSportServiceUUID());

        if (entity.getNadrazenaKategorie() != null) {
            details.setNadrazenaKategorieId(entity.getNadrazenaKategorie().getId());
        }

        Map<String, SportKategorieLocDetails> localeData = entity.getLocaleData().stream()
                .collect(Collectors.toMap(SportKategorieLocEntity::getJazyk, loc -> {
                    SportKategorieLocDetails locDetails = new SportKategorieLocDetails();
                    locDetails.setJazyk(loc.getJazyk());
                    locDetails.setNazev(loc.getNazev());
                    locDetails.setPopis(loc.getPopis());
                    return locDetails;
                }));
        details.setLocaleData(localeData);

        return details;
    }

}
