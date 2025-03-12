package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.InvalidParameterException;
import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.mapper.ObjektMapper;
import cz.inspire.sport.mapper.PodminkaRezervaceMapper;
import cz.inspire.sport.service.PodminkaRezervaceService;
import cz.inspire.sport.service.ObjektService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PodminkaRezervaceMapperIT {

    @Inject
    PodminkaRezervaceMapper podminkaRezervaceMapper;

    @Inject
    PodminkaRezervaceService podminkaRezervaceService;

    @Inject
    ObjektService objektService;

    @Inject
    ObjektMapper objektMapper;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String podminkaRezervaceId;
    private String objektId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(ObjektEntity.class, true);
        databaseCleaner.clearTable(PodminkaRezervaceEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistPodminkaRezervace() throws CreateException, InvalidParameterException {
        ObjektDto objDto = DtoFactory.createObjektDto();

        ObjektEntity mainEntity = objektMapper.toEntity(objDto);
        ObjektEntity obj = objektService.create(mainEntity);
        objektId = obj.getId();

        PodminkaRezervaceDto dto = DtoFactory.createPodminkaRezervaceDto();
        dto.setName("Test Condition");
        dto.setPriorita(5L);
        dto.setObjektRezervaceId("rezervace-001");
        dto.setObjektRezervaceObsazen(true);

        PodminkaRezervaceEntity entity = podminkaRezervaceMapper.toEntity(dto);
        entity.setObjekt(obj);
        podminkaRezervaceService.create(entity);
        podminkaRezervaceId = entity.getId();

        assertNotNull(podminkaRezervaceId);
        assertEquals("Test Condition", entity.getName());
        assertEquals(5L, entity.getPriorita());
        assertEquals("rezervace-001", entity.getObjektRezervaceId());
        assertTrue(entity.getObjektRezervaceObsazen());
        assertNotNull(entity.getObjekt());
        assertEquals(objektId, entity.getObjekt().getId());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrievePodminkaRezervace() throws FinderException {
        PodminkaRezervaceEntity fromDb = podminkaRezervaceService.findByPrimaryKey(podminkaRezervaceId);
        assertNotNull(fromDb);

        PodminkaRezervaceDto mappedDto = podminkaRezervaceMapper.toDto(fromDb);
        assertEquals(podminkaRezervaceId, mappedDto.getId());
        assertEquals("Test Condition", mappedDto.getName());
        assertEquals(5L, mappedDto.getPriorita());
        assertEquals("rezervace-001", mappedDto.getObjektRezervaceId());
        assertTrue(mappedDto.getObjektRezervaceObsazen());
        assertEquals(objektId, mappedDto.getObjektId());
    }
}
