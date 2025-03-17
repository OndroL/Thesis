package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.dto.SportKategorieLocDto;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import cz.inspire.sport.mapper.SportKategorieMapper;
import cz.inspire.sport.service.SportKategorieService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportKategorieMapperIT {

    @Inject
    SportKategorieMapper sportKategorieMapper;

    @Inject
    SportKategorieService sportKategorieService;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @Transactional
    public void setup() {
        // Clear tables for a clean test state.
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
        databaseCleaner.clearTable(SportKategorieLocEntity.class, true);
    }

    @Test
    @Transactional
    public void testToEntity_withLocaleData() throws Exception {
        // Create a DTO with locale data.
        SportKategorieDto dto = new SportKategorieDto();
        dto.setMultiSportFacilityId("FAC123");
        dto.setMultiSportServiceUUID("UUID123");
        dto.setNadrazenaKategorieId(null);
        
        // Populate locale data.
        Map<String, SportKategorieLocDto> localeData = new HashMap<>();
        SportKategorieLocDto locDto = new SportKategorieLocDto();
        locDto.setJazyk("cs");
        locDto.setNazev("Test Název");
        locDto.setPopis("Test Popis");
        localeData.put("cs", locDto);
        dto.setLocaleData(localeData);

        SportKategorieEntity entity = sportKategorieMapper.toEntity(dto);
        assertNotNull(entity);
        // Verify locale data mapping.
        assertNotNull(entity.getLocaleData());
        assertEquals(1, entity.getLocaleData().size());
        SportKategorieLocEntity locEntity = entity.getLocaleData().getFirst();
        assertEquals("cs", locEntity.getJazyk());
        assertEquals("Test Název", locEntity.getNazev());
        assertEquals("Test Popis", locEntity.getPopis());
    }

    @Test
    @Transactional
    public void testToDto_withLocaleData() throws Exception {
        // Create an entity with locale data.
        SportKategorieEntity entity = new SportKategorieEntity();
        entity.setMultiSportFacilityId("FAC456");
        entity.setMultiSportServiceUUID("UUID456");
        // Prepare locale data.
        SportKategorieLocEntity locEntity = new SportKategorieLocEntity();
        locEntity.setJazyk("en");
        locEntity.setNazev("Test Name");
        locEntity.setPopis("Test Description");
        entity.setLocaleData(Collections.singletonList(locEntity));
        // Persist the entity.
        SportKategorieEntity persisted = sportKategorieService.create(entity);

        SportKategorieDto dto = sportKategorieMapper.toDto(persisted);
        assertNotNull(dto);
        assertEquals("FAC456", dto.getMultiSportFacilityId());
        assertNotNull(dto.getLocaleData());
        // Check that locale data is returned as a Map keyed by language.
        assertTrue(dto.getLocaleData().containsKey("en"));
        SportKategorieLocDto mappedLoc = dto.getLocaleData().get("en");
        assertEquals("Test Name", mappedLoc.getNazev());
        assertEquals("Test Description", mappedLoc.getPopis());
    }

    @Test
    @Transactional
    public void testMapNadrazenaKategorie() throws Exception {
        // Create and persist a parent entity.
        SportKategorieEntity parent = new SportKategorieEntity();
        parent.setMultiSportFacilityId("PARENT_FAC");
        parent.setMultiSportServiceUUID("PARENT_UUID");
        SportKategorieEntity persistedParent = sportKategorieService.create(parent);

        // Create a DTO with the parent's id.
        SportKategorieDto dto = new SportKategorieDto();
        dto.setMultiSportFacilityId("FAC789");
        dto.setMultiSportServiceUUID("UUID789");
        dto.setNadrazenaKategorieId(persistedParent.getId());
        dto.setLocaleData(new HashMap<>()); // empty locale data

        SportKategorieEntity entity = sportKategorieMapper.toEntity(dto);
        // Verify that the parent association is mapped.
        assertNotNull(entity.getNadrazenaKategorie());
        assertEquals(persistedParent.getId(), entity.getNadrazenaKategorie().getId());
    }
}
