package MapperTests.Common;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.common.dto.NastaveniJsonDto;
import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.mapper.NastaveniJsonMapper;
import cz.inspire.common.service.NastaveniJsonService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NastaveniJsonMapperIT {

    @Inject
    NastaveniJsonMapper nastaveniJsonMapper;

    @Inject
    NastaveniJsonService nastaveniJsonService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String nastaveniId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(NastaveniJsonEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistNastaveniJson() throws CreateException {
        NastaveniJsonDto dto = new NastaveniJsonDto();
        dto.setValue("{\"some\": \"json\"}");

        NastaveniJsonEntity entity = nastaveniJsonMapper.toEntity(dto);
        nastaveniJsonService.create(entity);
        nastaveniId = entity.getKey();

        assertNotNull(nastaveniId);
        assertEquals("{\"some\": \"json\"}", entity.getValue());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveNastaveniJson() throws FinderException {
        NastaveniJsonEntity entity = nastaveniJsonService.findByPrimaryKey(nastaveniId);
        assertNotNull(entity);

        NastaveniJsonDto dto = nastaveniJsonMapper.toDto(entity);
        assertEquals(nastaveniId, dto.getKey());
        assertEquals("{\"some\": \"json\"}", dto.getValue());
    }
}
