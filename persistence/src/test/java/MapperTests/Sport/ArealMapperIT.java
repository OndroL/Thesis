package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.dto.ArealLocDto;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.mapper.ArealMapper;
import cz.inspire.sport.service.ArealService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArealMapperIT {

    @Inject
    ArealMapper arealMapper;

    @Inject
    ArealService arealService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String parentArealId;
    private String childArealId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(ArealEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testCreateParentAreal() throws FinderException, CreateException {
        ArealDto parentDto = DtoFactory.createArealDto(); 
        parentDto.setPocetNavazujucichRez(2);

        // Add a locale entry with key "cs"
        ArealLocDto locDto = DtoFactory.createArealLocDto(); 
        locDto.setJazyk("cs");
        locDto.setNazev("Parent Areal Name");
        locDto.setPopis("Parent Areal Description");
        parentDto.setLocaleData(Map.of("cs", locDto));

        ArealEntity parentEntity = arealMapper.toEntity(parentDto);
        arealService.create(parentEntity);
        parentArealId = parentEntity.getId();

        assertNotNull(parentArealId);

        ArealEntity fromDb = arealService.findByPrimaryKey(parentArealId);
        assertNotNull(fromDb);
        assertEquals(Integer.valueOf(2), fromDb.getPocetNavazujucichRez());
        assertEquals(1, fromDb.getLocaleData().size());

        ArealLocEntity singleLoc = fromDb.getLocaleData().get(0);
        assertEquals("cs", singleLoc.getJazyk());
        assertEquals("Parent Areal Name", singleLoc.getNazev());
        assertEquals("Parent Areal Description", singleLoc.getPopis());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveParentAreal() throws FinderException {
        ArealEntity fromDb = arealService.findByPrimaryKey(parentArealId);
        assertNotNull(fromDb);

        ArealDto dto = arealMapper.toDto(fromDb);
        assertEquals(parentArealId, dto.getId());
        assertEquals(Integer.valueOf(2), dto.getPocetNavazujucichRez());
        assertNotNull(dto.getLocaleData());
        assertTrue(dto.getLocaleData().containsKey("cs"));

        ArealLocDto loc = dto.getLocaleData().get("cs");
        assertEquals("Parent Areal Name", loc.getNazev());
        assertEquals("Parent Areal Description", loc.getPopis());
    }

    @Order(3)
    @Test
    @Transactional
    public void testCreateChildArealWithParent() throws FinderException, CreateException {
        ArealDto childDto = DtoFactory.createArealDto();
        childDto.setPocetNavazujucichRez(0);
        childDto.setNadrazenyArealId(parentArealId);

        ArealLocDto locDto = DtoFactory.createArealLocDto();
        locDto.setJazyk("cs");
        locDto.setNazev("Child Areal");
        locDto.setPopis("Child Areal Description");
        childDto.setLocaleData(Map.of("cs", locDto));

        ArealEntity childEntity = arealMapper.toEntity(childDto);
        arealService.create(childEntity);
        childArealId = childEntity.getId();

        assertNotNull(childArealId);

        ArealEntity fromDb = arealService.findByPrimaryKey(childArealId);
        assertNotNull(fromDb);
        assertNotNull(fromDb.getNadrazenyAreal());
        assertEquals(parentArealId, fromDb.getNadrazenyAreal().getId());
    }

    @Order(4)
    @Test
    @Transactional
    public void testRetrieveChildAreal() throws FinderException {
        ArealEntity fromDb = arealService.findByPrimaryKey(childArealId);
        assertNotNull(fromDb);
        assertEquals(parentArealId, fromDb.getNadrazenyAreal().getId());

        ArealDto childDto = arealMapper.toDto(fromDb);
        assertEquals(childArealId, childDto.getId());
        assertEquals(parentArealId, childDto.getNadrazenyArealId());
        assertTrue(childDto.getLocaleData().containsKey("cs"));

        ArealLocDto loc = childDto.getLocaleData().get("cs");
        assertNotNull(loc);
    }
}
