package MapperTests.Common;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.common.dto.HeaderDto;
import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.mapper.HeaderMapper;
import cz.inspire.common.service.HeaderService;
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
public class HeaderMapperIT {

    @Inject
    HeaderMapper headerMapper;

    @Inject
    HeaderService headerService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String headerId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(HeaderEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistHeader() throws CreateException {
        HeaderDto headerDto = new HeaderDto();
        headerDto.setField(111);
        headerDto.setLocation(222);

        HeaderEntity headerEntity = headerMapper.toEntity(headerDto);

        headerService.create(headerEntity);
        headerId = headerEntity.getId();

        assertNotNull(headerId, "HeaderEntity id should be generated");
        assertEquals(111, headerEntity.getField());
        assertEquals(222, headerEntity.getLocation());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveHeaderAndMapBackToDto() throws FinderException {
        HeaderEntity fromDb = headerService.findByPrimaryKey(headerId);
        assertNotNull(fromDb, "Retrieved HeaderEntity should not be null");

        HeaderDto mappedDto = headerMapper.toDto(fromDb);

        assertEquals(headerId, mappedDto.getId());
        assertEquals(111, mappedDto.getField());
        assertEquals(222, mappedDto.getLocation());
    }
}
