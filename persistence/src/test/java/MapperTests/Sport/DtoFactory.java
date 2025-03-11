package MapperTests.Sport;

import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.dto.ActivityFavouriteDto;
import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.dto.ArealLocDto;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.dto.ObjektLocDto;
import cz.inspire.sport.dto.OvladacObjektuDto;
import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.dto.SportKategorieLocDto;
import cz.inspire.sport.dto.SportLocDto;
import cz.inspire.sport.utils.SazbaStorna;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DtoFactory {

    private static final Random RANDOM = new Random();

    // --------------------------------------------------------------------------------
    // ObjektDto
    // --------------------------------------------------------------------------------
    public static ObjektDto createObjektDto() {
        ObjektDto dto = new ObjektDto();
        dto.setId(null);  // Let DB generate

        dto.setKapacita(randomInt(5, 50));
        dto.setCasovaJednotka(randomInt(5, 60));
        dto.setTypRezervace(randomInt(1, 5));
        dto.setPrimyVstup(RANDOM.nextBoolean());
        dto.setMinDelkaRezervace(randomInt(10, 30));
        dto.setMaxDelkaRezervace(randomInt(60, 240));
        dto.setVolnoPredRezervaci(randomInt(0, 10));
        dto.setVolnoPoRezervaci(randomInt(0, 10));
        dto.setZarovnatZacatekRezervace(randomInt(0, 15));
        dto.setDelkaRezervaceNasobkem(randomInt(5, 30));
        dto.setVicestavovy(RANDOM.nextBoolean());
        dto.setStav(randomInt(0, 5));

        dto.setReservationStart(new Integer[]{randomInt(6, 10), 0});
        dto.setReservationFinish(new Integer[]{randomInt(18, 22), 0});

        dto.setOdcitatProcedury(RANDOM.nextBoolean());
        dto.setRezervaceNaTokeny(RANDOM.nextBoolean());
        dto.setRucniUzavreniVstupu(RANDOM.nextBoolean());
        dto.setUpraveniCasuVstupu(RANDOM.nextBoolean());
        dto.setPozastavitVstup(RANDOM.nextBoolean());
        dto.setShowProgress(RANDOM.nextBoolean());
        dto.setCheckTokensCount(RANDOM.nextBoolean());
        dto.setSelectInstructor(RANDOM.nextBoolean());
        dto.setShowInstructorName(RANDOM.nextBoolean());
        dto.setShowSportName(RANDOM.nextBoolean());

        dto.setVytvoreniRezervacePredZacatkem(randomInt(15, 60));
        dto.setEditaceRezervacePredZacatkem(randomInt(5, 30));
        dto.setZruseniRezervacePredZacatkem(randomInt(30, 120));

        // All relationships/references to other DTOs are null:
        dto.setLocaleData(null);
        dto.setAreal(null);
        dto.setSports(null);
        dto.setOvladaceObjektu(null);
        dto.setPodminkyRezervaci(null);
        dto.setNadObjekty(null);
        dto.setPodObjekty(null);

        dto.setGoogleCalendarId(null);
        dto.setGoogleCalendarNotification(false);
        dto.setGoogleCalendarNotificationBefore(0);

        return dto;
    }

    // --------------------------------------------------------------------------------
    // ObjektLocDto
    // --------------------------------------------------------------------------------
    public static ObjektLocDto createObjektLocDto() {
        ObjektLocDto dto = new ObjektLocDto();
        dto.setId(null);
        dto.setJazyk("cs");
        dto.setNazev("Nazev" + randomInt(1, 100));
        dto.setPopis("Popis" + randomInt(1, 100));
        dto.setZkracenyNazev("Zkr" + randomInt(1, 100));
        return dto;
    }

    // --------------------------------------------------------------------------------
    // SportDto
    // --------------------------------------------------------------------------------
    public static SportDto createSportDto() {
        SportDto dto = new SportDto();
        dto.setId(null);
        dto.setTyp(randomInt(1, 100));
        dto.setZboziId("ZBOZI" + randomInt(1, 999));
        dto.setSkladId("SKLAD" + randomInt(1, 999));
        dto.setSazbaJednotek(randomInt(5, 50));
        dto.setSazbaNaOsobu(RANDOM.nextBoolean());
        dto.setSazbaNaCas(randomInt(15, 120));
        dto.setUctovatZalohu(RANDOM.nextBoolean());
        dto.setPodSportyCount(randomInt(0, 5));

        // Possibly create a small random list of SazbaStorna objects
        dto.setSazbyStorna(createRandomSazbaStornaList());

        dto.setMinDelkaRezervace(randomInt(10, 60));
        dto.setMaxDelkaRezervace(randomInt(60, 180));
        dto.setObjednavkaZaplniObjekt(RANDOM.nextBoolean());
        dto.setDelkaRezervaceNasobkem(randomInt(5, 30));

        // Random "Color" for demonstration
        dto.setBarvaPopredi(new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)));
        dto.setBarvaPozadi(new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)));

        dto.setZobrazitText(RANDOM.nextBoolean());
        dto.setViditelnyWeb(RANDOM.nextBoolean());
        dto.setNavRezervaceOffset(randomInt(0, 15));
        dto.setDelkaHlavniRez(randomInt(60, 120));
        dto.setMinimalniPocetOsob(randomInt(1, 4));
        dto.setMinutyPredVyhodnocenimKapacity(randomInt(5, 30));
        dto.setMaximalniPocetOsobNaZakaznika(randomInt(1, 10));

        dto.setLocaleData(null);
        dto.setSportKategorie(null);
        dto.setInstructors(null);
        dto.setNadrazenySportId(null);
        dto.setNavazujiciSportId(null);
        dto.setActivityId(null);

        return dto;
    }

    // --------------------------------------------------------------------------------
    // SportLocDto
    // --------------------------------------------------------------------------------
    public static SportLocDto createSportLocDto() {
        SportLocDto dto = new SportLocDto();
        dto.setId(null);
        dto.setJazyk("cs");
        dto.setNazev("SportNazev" + randomInt(1, 100));
        dto.setPopis("SportPopis" + randomInt(1, 100));
        return dto;
    }

    // --------------------------------------------------------------------------------
    // OvladacObjektuDto
    // --------------------------------------------------------------------------------
    public static OvladacObjektuDto createOvladacObjektuDto() {
        OvladacObjektuDto dto = new OvladacObjektuDto();
        dto.setId(null);
        dto.setIdOvladace("OVL-" + randomInt(100, 999));
        dto.setManual(RANDOM.nextBoolean());
        dto.setAutomat(RANDOM.nextBoolean());
        dto.setDelkaSepnutiPoKonci(randomInt(0, 10));
        dto.setZapnutiPredZacatkem(randomInt(0, 10));
        dto.setCislaZapojeniList(null); // or some random list
        dto.setObjektId(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // PodminkaRezervaceDto
    // --------------------------------------------------------------------------------
    public static PodminkaRezervaceDto createPodminkaRezervaceDto() {
        PodminkaRezervaceDto dto = new PodminkaRezervaceDto();
        dto.setId(null);
        dto.setName("Podminka" + randomInt(1, 100));
        dto.setPriorita(randomLong(1, 10));
        dto.setObjektRezervaceId(null);
        dto.setObjektRezervaceObsazen(RANDOM.nextBoolean());
        dto.setObjektId(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // SportKategorieDto
    // --------------------------------------------------------------------------------
    public static SportKategorieDto createSportKategorieDto() {
        SportKategorieDto dto = new SportKategorieDto();
        dto.setId(null);
        dto.setMultiSportFacilityId("FAC-" + randomInt(1, 999));
        dto.setMultiSportServiceUUID("UUID-" + randomInt(1, 9999));
        dto.setNadrazenaKategorieId(null);
        dto.setLocaleData(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // SportKategorieLocDto
    // --------------------------------------------------------------------------------
    public static SportKategorieLocDto createSportKategorieLocDto() {
        SportKategorieLocDto dto = new SportKategorieLocDto();
        dto.setId(null);
        dto.setJazyk("cs");
        dto.setNazev("KategorieNazev" + randomInt(1, 100));
        dto.setPopis("KategoriePopis" + randomInt(1, 100));
        return dto;
    }

    // --------------------------------------------------------------------------------
    // InstructorDto
    // --------------------------------------------------------------------------------
    public static InstructorDto createInstructorDto() {
        InstructorDto dto = new InstructorDto();
        dto.setId(null);
        dto.setFirstName("First" + randomInt(1, 100));
        dto.setLastName("Last" + randomInt(1, 100));
        dto.setIndex(randomInt(1, 50));
        dto.setEmail("email" + randomInt(1, 999) + "@example.com");
        dto.setPhoneCode("+1");
        dto.setPhoneNumber(String.valueOf(randomInt(100000000, 999999999)));
        dto.setEmailInternal(null);
        dto.setPhoneCodeInternal(null);
        dto.setPhoneNumberInternal(null);
        dto.setInfo("Some info");
        dto.setColor("blue");
        dto.setPhoto(null);
        dto.setDeleted(RANDOM.nextBoolean());
        dto.setGoogleCalendarId(null);
        dto.setGoogleCalendarNotification(RANDOM.nextBoolean());
        dto.setGoogleCalendarNotificationBefore(randomInt(0, 30));
        dto.setActivities(null);
        dto.setSports(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // ArealDto
    // --------------------------------------------------------------------------------
    public static ArealDto createArealDto() {
        ArealDto dto = new ArealDto();
        dto.setId(null);
        dto.setPocetNavazujucichRez(randomInt(0, 5));
        dto.setLocaleData(null);
        dto.setNadrazenyArealId(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // ArealLocDto
    // --------------------------------------------------------------------------------
    public static ArealLocDto createArealLocDto() {
        ArealLocDto dto = new ArealLocDto();
        dto.setId(null);
        dto.setJazyk("cs");
        dto.setNazev("Areal" + randomInt(1, 100));
        dto.setPopis("PopisAreal" + randomInt(1, 100));
        return dto;
    }

    // --------------------------------------------------------------------------------
    // ActivityDto
    // --------------------------------------------------------------------------------
    public static ActivityDto createActivityDto() {
        ActivityDto dto = new ActivityDto();
        dto.setId(null);
        dto.setName("Activity" + randomInt(1, 100));
        dto.setDescription("Desc" + randomInt(1, 100));
        dto.setIndex(randomInt(1, 20));
        dto.setIconId("icon-" + randomInt(1, 100));
        dto.setInstructors(null);
        dto.setSports(null);
        return dto;
    }

    // --------------------------------------------------------------------------------
    // ActivityFavouriteDto
    // --------------------------------------------------------------------------------
    public static ActivityFavouriteDto createActivityFavouriteDto() {
        ActivityFavouriteDto dto = new ActivityFavouriteDto();
        dto.setZakaznikId("ZAK-" + randomInt(100, 999));
        dto.setActivityId("ACT-" + randomInt(100, 999));
        dto.setPocet(randomInt(1, 10));
        dto.setDatumPosledniZmeny(new Date());
        return dto;
    }

    // --------------------------------------------------------------------------------
    // Helper: Create random SazbaStorna list
    // --------------------------------------------------------------------------------
    private static List<SazbaStorna> createRandomSazbaStornaList() {
        int count = randomInt(1, 3);  // 1 or 2 random SazbaStorna
        List<SazbaStorna> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SazbaStorna storna = new SazbaStorna(
                randomInt(6, 48),       // dobaPredZacatkemHry
                "hodin",
                RANDOM.nextDouble() * 100, // pocetJednotek
                RANDOM.nextBoolean()    // procentualne
            );
            list.add(storna);
        }
        return list;
    }

    // --------------------------------------------------------------------------------
    // Basic random helpers
    // --------------------------------------------------------------------------------
    private static int randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    private static long randomLong(int min, int max) {
        return (long) randomInt(min, max);
    }
}
