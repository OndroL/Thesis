package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.OmezeniRezervaciRepository;
import cz.inspire.sport.utils.SimpleOtviraciDoba;
import cz.inspire.utils.PeriodOfTime;
import cz.inspire.utils.TimeOfDay;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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


    @Test
    @Order(1)
    void testSaveAndFindById() {
        SimpleOtviraciDoba otviraciDoba = new SimpleOtviraciDoba(new TreeMap<>());

        SportEntity sport = new SportEntity("SP-001", 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(8, 0), new TimeOfDay(16, 0));

        otviraciDoba.addTimePeriod(period, sport);

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity("OBJ-001", otviraciDoba);
        em.persist(entity);
        em.flush();

        Optional<OmezeniRezervaciEntity> result = omezeniRezervaciRepository.findById("OBJ-001");

        assertTrue(result.isPresent());
        assertNotNull(result.get().getOmezeni());
    }


    @Test
    @Order(2)
    void testDeleteEntity() {
        SimpleOtviraciDoba otviraciDoba = new SimpleOtviraciDoba(new TreeMap<>());

        SportEntity sport = new SportEntity("SP-002", 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        PeriodOfTime period = new PeriodOfTime(new TimeOfDay(8, 0), new TimeOfDay(16, 0));

        otviraciDoba.addTimePeriod(period, sport);

        OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity("OBJ-002", otviraciDoba);
        em.persist(entity);
        em.flush();

        Optional<OmezeniRezervaciEntity> result = omezeniRezervaciRepository.findById("OBJ-002");
        assertTrue(result.isPresent());

        em.remove(entity);
        em.flush();

        result = omezeniRezervaciRepository.findById("OBJ-002");
        assertFalse(result.isPresent());
        assertNotNull(result.get().getOmezeni());
    }
}
