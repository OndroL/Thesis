package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.OmezeniRezervaciRepository;
import cz.inspire.utils.PeriodOfTime;
import cz.inspire.utils.RozsirenaTydenniOtviraciDoba;
import cz.inspire.utils.TimeOfDay;
import cz.inspire.utils.TydeniOtviraciDoba;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class OmezeniRezervaciRepositoryIT {

    @Inject
    OmezeniRezervaciRepository omezeniRezervaciRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(OmezeniRezervaciEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
    }

    @Order(1)
    @Test
    void testSaveAndRetrieve_TydeniOtviraciDoba() {
        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba();

        SportEntity sport = new SportEntity(null, 1, "ZB-008", "SK-008", 180, true, 90, true, 20, null, 45, 200, true, 30, null, null, true, true, 15, 120, 3, 7, 40, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        // Create two identical periods for Monday
        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(9, 0), new TimeOfDay(12, 0));
        otviraciDoba.addOtevreno(period, Calendar.MONDAY, sport.getId());
        // Adding the same period a second time; depending on your implementation this might update or be ignored.
        otviraciDoba.addOtevreno(period, Calendar.MONDAY, sport.getId());

        // Create OmezeniRezervaciEntity with null ID so it is generated.
        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);
        TydeniOtviraciDoba retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        SortedMap<PeriodOfTime, String> retrievedMap = retrievedDoba.getOtevreno(Calendar.MONDAY);
        assertNotNull(retrievedMap);
        assertTrue(retrievedMap.containsKey(period));
        assertEquals(sport.getId(), retrievedMap.get(period));
    }

    @Order(2)
    @Test
    void testDelete_TydeniOtviraciDoba() {
        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba();

        SportEntity sport = new SportEntity(null, 1, "ZB-009", "SK-009", 190, true, 100, true, 25, null, 50, 210, true, 35, null, null, true, true, 18, 130, 3, 8, 50, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(10, 0), new TimeOfDay(13, 0));
        otviraciDoba.addOtevreno(period, Calendar.TUESDAY, sport.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        // Delete using repository
        omezeniRezervaciRepository.deleteById(entity.getObjektId());

        result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNull(result);
    }

    @Order(3)
    @Test
    void testSaveMultipleEntriesForSameDay() {
        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba();

        SportEntity sport1 = new SportEntity(null, 1, "ZB-010", "SK-010", 200, true, 80, true, 30, null, 60, 220, true, 40, null, null, true, true, 20, 150, 4, 9, 55, null, null, null, null, null, null, null, null, null);
        SportEntity sport2 = new SportEntity(null, 1, "ZB-011", "SK-011", 210, true, 85, true, 35, null, 70, 230, true, 45, null, null, true, true, 22, 160, 5, 10, 60, null, null, null, null, null, null, null, null, null);
        em.persist(sport1);
        em.persist(sport2);

        PeriodOfTime period1 = new PeriodOfTime(new TimeOfDay(8, 0), new TimeOfDay(10, 0));
        PeriodOfTime period2 = new PeriodOfTime(new TimeOfDay(14, 0), new TimeOfDay(16, 0));

        otviraciDoba.addOtevreno(period1, Calendar.WEDNESDAY, sport1.getId());
        otviraciDoba.addOtevreno(period2, Calendar.WEDNESDAY, sport2.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);
        TydeniOtviraciDoba retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        SortedMap<PeriodOfTime, String> retrievedMap = retrievedDoba.getOtevreno(Calendar.WEDNESDAY);
        assertNotNull(retrievedMap);
        assertTrue(retrievedMap.containsKey(period1));
        assertTrue(retrievedMap.containsKey(period2));
        assertEquals(sport1.getId(), retrievedMap.get(period1));
        assertEquals(sport2.getId(), retrievedMap.get(period2));
    }

    @Order(4)
    @Test
    void testUpdateOpeningHours() {
        SportEntity sport = new SportEntity(null, 1, "ZB-012", "SK-012", 220, true, 95, true, 40, null, 80, 240, true, 50, null, null, true, true, 25, 170, 6, 11, 65, null, null, null, null, null, null, null, null, null);
        em.persist(sport);
        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba(sport.getClass());

        PeriodOfTime oldPeriod = new PeriodOfTime(new TimeOfDay(7, 0), new TimeOfDay(9, 0));
        PeriodOfTime newPeriod = new PeriodOfTime(new TimeOfDay(10, 0), new TimeOfDay(12, 0));

        otviraciDoba.addOtevreno(oldPeriod, 1, sport.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        // Retrieve and update the opening hours
        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        TydeniOtviraciDoba retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        retrievedDoba.updateOtevreno(oldPeriod, newPeriod, 1, sport.getId());
        entity.setOmezeni(retrievedDoba);

        entity = omezeniRezervaciRepository.create(entity);

        // Retrieve again and verify update
        result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        SortedMap<PeriodOfTime, String> retrievedMap = retrievedDoba.getOtevreno(1);
        assertNotNull(retrievedMap);
        assertFalse(retrievedMap.containsKey(oldPeriod));
        assertTrue(retrievedMap.containsKey(newPeriod));
        assertEquals(sport.getId(), retrievedMap.get(newPeriod));
    }

    @Order(5)
    @Test
    void testRemoveOpeningHourEntry() {
        SportEntity sport = new SportEntity(null, 1, "ZB-013", "SK-013", 230, true, 100, true, 50, null, 90, 250, true, 55, null, null, true, true, 28, 180, 7, 12, 70, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba(sport.getClass());
        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(8, 0), new TimeOfDay(11, 0));
        otviraciDoba.addOtevreno(period, 4, sport.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        // Retrieve and delete the opening hours
        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        TydeniOtviraciDoba retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        retrievedDoba.removeOtevreno(period, 4);
        entity.setOmezeni(retrievedDoba);

        entity = omezeniRezervaciRepository.create(entity);

        // Retrieve again and verify deletion
        result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        SortedMap<PeriodOfTime, String> retrievedMap = retrievedDoba.getOtevreno(Calendar.FRIDAY);
        assertNotNull(retrievedMap);
        assertFalse(retrievedMap.containsKey(period));
    }

    @Order(6)
    @Test
    void testSaveMultipleSportsForSameTimeSlot() {
        RozsirenaTydenniOtviraciDoba otviraciDoba = new RozsirenaTydenniOtviraciDoba();

        SportEntity sport1 = new SportEntity(null, 1, "ZB-014", "SK-014", 260, true, 120, true, 70, null, 110, 280, true, 75, null, null, true, true, 35, 200, 9, 14, 85, null, null, null, null, null, null, null, null, null);
        SportEntity sport2 = new SportEntity(null, 1, "ZB-015", "SK-015", 270, true, 125, true, 75, null, 115, 290, true, 80, null, null, true, true, 38, 210, 10, 15, 90, null, null, null, null, null, null, null, null, null);
        em.persist(sport1);
        em.persist(sport2);

        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(9, 0), new TimeOfDay(12, 0));

        otviraciDoba.addOtevreno(period, 0, sport1.getId());
        otviraciDoba.addOtevreno(period, 0, sport2.getId());
        otviraciDoba.addOtevreno(period, 1, sport1.getId());
        otviraciDoba.addOtevreno(period, 1, sport2.getId());
        otviraciDoba.addOtevreno(period, 2, sport1.getId());
        otviraciDoba.addOtevreno(period, 2, sport2.getId());
        otviraciDoba.addOtevreno(period, 3, sport1.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);
        RozsirenaTydenniOtviraciDoba retrievedDoba = (RozsirenaTydenniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        // Retrieve the list of sport IDs for index 0
        SortedMap<PeriodOfTime, List<String>> retrievedMap = retrievedDoba.getOtevreno(0);
        assertNotNull(retrievedMap);
        assertTrue(retrievedMap.containsKey(period));

        List<String> retrievedIds = retrievedMap.get(period);
        assertNotNull(retrievedIds);
        assertTrue(retrievedIds.contains(sport1.getId()));
        assertTrue(retrievedIds.contains(sport2.getId()));
    }

    @Order(7)
    @Test
    void testSaveAndRetrieve_PeriodOfTime() {
        SportEntity sport = new SportEntity(null, 1, "ZB-016", "SK-016", 180, true, 90, true, 20, null, 45, 200, true, 30, null, null, true, true, 15, 120, 3, 7, 40, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        TydeniOtviraciDoba otviraciDoba = new TydeniOtviraciDoba();
        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(9, 0), new TimeOfDay(12, 0));
        otviraciDoba.addOtevreno(period, 1, sport.getId());

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity(null, otviraciDoba);
        entity = omezeniRezervaciRepository.create(entity);

        OmezeniRezervaciEntity result = omezeniRezervaciRepository.findById(entity.getObjektId());
        assertNotNull(result);

        TydeniOtviraciDoba retrievedDoba = (TydeniOtviraciDoba) result.getOmezeni();
        assertNotNull(retrievedDoba);

        SortedMap<PeriodOfTime, String> retrievedMap = retrievedDoba.getOtevreno(1);
        assertNotNull(retrievedMap);
        assertTrue(retrievedMap.containsKey(period));
        assertEquals(sport.getId(), retrievedMap.get(period));
    }
}
