package MapperTests.Common;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.common.dto.NastaveniDto;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.mapper.NastaveniMapper;
import cz.inspire.common.service.NastaveniService;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NastaveniMapperIT {

    @Inject
    NastaveniMapper nastaveniMapper;

    @Inject
    NastaveniService nastaveniService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String nastaveniId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(NastaveniEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistNastaveni() throws CreateException {
        NastaveniDto dto = new NastaveniDto();
        dto.setKey(null);
        dto.setValue((java.io.Serializable) Map.of("testKey", "testValue"));

        NastaveniEntity entity = nastaveniMapper.toEntity(dto);
        nastaveniService.create(entity);
        nastaveniId = entity.getKey();

        assertNotNull(nastaveniId);
        assertNotNull(entity.getValue());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveNastaveni() throws FinderException {
        NastaveniEntity entity = nastaveniService.findByPrimaryKey(nastaveniId);
        assertNotNull(entity);

        NastaveniDto dto = nastaveniMapper.toDto(entity);
        assertEquals(nastaveniId, dto.getKey());
        assertNotNull(dto.getValue());
    }
}
