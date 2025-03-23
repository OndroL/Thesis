package MapperTests.Common;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.mapper.MenaMapper;
import cz.inspire.common.service.MenaService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenaMapperIT {

    @Inject
    MenaMapper menaMapper;

    @Inject
    MenaService menaService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String menaId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(MenaEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistMena() throws CreateException {
        MenaDto dto = new MenaDto();
        dto.setKod("CZK");
        dto.setVycetkaList(new ArrayList<>(List.of(new BigDecimal("10.50"), new BigDecimal("20"))));
        dto.setKodNum(203);
        dto.setZaokrouhleniHotovost(2);
        dto.setZaokrouhleniKarta(5);

        MenaEntity entity = menaMapper.toEntity(dto);
        menaService.create(entity);
        menaId = entity.getId();

        assertNotNull(entity.getId());
        assertEquals("CZK", entity.getKod());
        assertEquals(203, entity.getKodNum());
        assertEquals(2, entity.getZaokrouhleniHotovost());
        assertEquals(5, entity.getZaokrouhleniKarta());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveMena() throws FinderException {
        MenaEntity fromDb = menaService.findByPrimaryKey(menaId);
        assertNotNull(fromDb);

        MenaDto mappedDto = menaMapper.toDto(fromDb);
        assertEquals(menaId, mappedDto.getId());
        assertEquals("CZK", mappedDto.getKod());
        assertEquals(203, mappedDto.getKodNum());
        assertEquals(2, mappedDto.getZaokrouhleniHotovost());
        assertEquals(5, mappedDto.getZaokrouhleniKarta());
        assertNotNull(mappedDto.getVycetkaList());
    }
}
