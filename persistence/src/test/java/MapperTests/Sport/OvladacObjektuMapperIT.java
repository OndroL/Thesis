package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.OvladacObjektuDto;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.mapper.OvladacObjektuMapper;
import cz.inspire.sport.service.OvladacObjektuService;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for OvladacObjektuMapper
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OvladacObjektuMapperIT {

    @Inject
    OvladacObjektuMapper ovladacObjektuMapper;

    @Inject
    OvladacObjektuService ovladacObjektuService; // adapt to your actual service or repository

    @Inject
    DatabaseCleaner databaseCleaner;

    private String ovladacObjektuId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(OvladacObjektuEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistOvladacObjektu() throws CreateException {
        OvladacObjektuDto dto = DtoFactory.createOvladacObjektuDto();
        dto.setCislaZapojeniList(Arrays.asList(2, 5, 7));
        dto.setObjektId("OBJ-12345");

        OvladacObjektuEntity entity = ovladacObjektuMapper.toEntity(dto);
        ovladacObjektuService.create(entity);
        ovladacObjektuId = entity.getId();

        assertNotNull(ovladacObjektuId);
        assertEquals(dto.getIdOvladace(), entity.getIdOvladace());
        assertEquals(dto.getObjektId(), entity.getObjektId());
        assertEquals(dto.getCislaZapojeniList(), entity.getCislaZapojeni());
        assertEquals(dto.getDelkaSepnutiPoKonci(), entity.getDelkaSepnutiPoKonci());
        assertEquals(dto.getZapnutiPredZacatkem(), entity.getZapnutiPredZacatkem());
        assertEquals(dto.getManual(), entity.getManual());
        assertEquals(dto.getAutomat(), entity.getAutomat());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveOvladacObjektu() throws FinderException {
        OvladacObjektuEntity fromDb = ovladacObjektuService.findByPrimaryKey(ovladacObjektuId);
        assertNotNull(fromDb);

        OvladacObjektuDto mappedDto = ovladacObjektuMapper.toDto(fromDb);
        assertEquals(ovladacObjektuId, mappedDto.getId());
        assertEquals(fromDb.getIdOvladace(), mappedDto.getIdOvladace());
        assertEquals(fromDb.getObjektId(), mappedDto.getObjektId());
        assertEquals(fromDb.getCislaZapojeni(), mappedDto.getCislaZapojeniList());
        assertEquals(fromDb.getDelkaSepnutiPoKonci(), mappedDto.getDelkaSepnutiPoKonci());
        assertEquals(fromDb.getZapnutiPredZacatkem(), mappedDto.getZapnutiPredZacatkem());
        assertEquals(fromDb.getManual(), mappedDto.getManual());
        assertEquals(fromDb.getAutomat(), mappedDto.getAutomat());
    }
}
