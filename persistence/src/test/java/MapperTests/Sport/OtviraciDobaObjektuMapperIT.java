package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.OtviraciDobaObjektuDto;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.mapper.OtviraciDobaObjektuMapper;
import cz.inspire.sport.service.OtviraciDobaObjektuService;
import cz.inspire.sport.utils.OtviraciDoba;
import cz.inspire.utils.TydeniOtviraciDoba;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Example integration test for OtviraciDobaObjektuMapper.
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OtviraciDobaObjektuMapperIT {

    @Inject
    OtviraciDobaObjektuMapper mapper;

    @Inject
    OtviraciDobaObjektuService service; // Your actual service or repository

    @Inject
    DatabaseCleaner databaseCleaner;

    private final String OBJ_ID = "OBJ-123";
    private Date platnostOd;
    private OtviraciDoba otviraciDoba;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(OtviraciDobaObjektuEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testCreateOtviraciDobaObjektu() throws CreateException {
        // Create or pick a date for "platnostOd"
        platnostOd = new Date();

        otviraciDoba = new TydeniOtviraciDoba();

        OtviraciDobaObjektuDto dto = new OtviraciDobaObjektuDto();
        dto.setObjektId(OBJ_ID);
        dto.setPlatnostOd(platnostOd);
        dto.setOtviraciDoba(otviraciDoba);

        // Map DTO -> Entity
        OtviraciDobaObjektuEntity entity = mapper.toEntity(dto);
        service.create(entity);

        assertNotNull(entity.getEmbeddedId(), "Embedded ID should be assigned");
        assertEquals(OBJ_ID, entity.getEmbeddedId().getObjektId());
        assertNotNull(entity.getOtviraciDoba(), "OtviraciDoba should not be null in the entity");
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveOtviraciDobaObjektu() throws FinderException {
        // Retrieve by composite key or however your service is set up
        OtviraciDobaObjektuEntity fromDb = service.findByPrimaryKey(
                new OtviraciDobaObjektuPK(OBJ_ID, LocalDateTime.ofInstant(platnostOd.toInstant(), ZoneId.systemDefault())));
        assertNotNull(fromDb);

        // Map Entity -> DTO
        OtviraciDobaObjektuDto mappedDto = mapper.toDto(fromDb);
        assertEquals(OBJ_ID, mappedDto.getObjektId());
        assertNotNull(mappedDto.getOtviraciDoba(), "OtviraciDoba should be deserialized");

    }
}
