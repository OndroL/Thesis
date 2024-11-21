package ServiceTests;

import cz.inspire.thesis.data.dto.MenaDetails;
import cz.inspire.thesis.data.model.MenaEntity;
import cz.inspire.thesis.data.repository.MenaRepository;
import cz.inspire.thesis.data.service.MenaService;
import cz.inspire.thesis.exceptions.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenaServiceTest {

    @Mock
    private MenaRepository menaRepository;

    @InjectMocks
    private MenaService menaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDetails() {
        MenaEntity entity = new MenaEntity("1", "USD", "10.5;20.5", 840, 2, 2);

        MenaDetails details = menaService.getDetails(entity);

        assertNotNull(details);
        assertEquals("1", details.getId());
        assertEquals("USD", details.getKod());
        assertEquals(840, details.getKodNum());
        assertEquals(2, details.getZaokrouhleniHotovost());
        assertEquals(2, details.getZaokrouhleniKarta());
        assertEquals(Arrays.asList(new BigDecimal("10.5"), new BigDecimal("20.5")), details.getVycetkaList());
    }

    @Test
    void testEjbCreate() throws CreateException {
        MenaDetails details = new MenaDetails();
        details.setId("1");
        details.setKod("EUR");
        details.setVycetka("5.5;15.0");
        details.setKodNum(978);
        details.setZaokrouhleniHotovost(1);
        details.setZaokrouhleniKarta(2);

        MenaEntity mockEntity = new MenaEntity(
                "1",
                "EUR",
                "5.5;15.0",
                978,
                1,
                2
        );
        when(menaRepository.save(any(MenaEntity.class))).thenReturn(mockEntity);

        String id = menaService.ejbCreate(details);

        assertEquals("1", id);
        verify(menaRepository, times(1)).save(any(MenaEntity.class));
    }

    @Test
    void testEjbCreateThrowsException() {
        MenaDetails details = new MenaDetails();
        details.setId("1");
        details.setKod("EUR");

        doThrow(new RuntimeException("Database error")).when(menaRepository).save(any(MenaEntity.class));

        CreateException exception = assertThrows(CreateException.class, () -> menaService.ejbCreate(details));
        assertEquals("Failed to create MenaEntity", exception.getMessage());
    }

    @Test
    void testFindAll() {
        List<MenaEntity> entities = Arrays.asList(
                new MenaEntity("1", "USD", "10.5", 840, 2, 2),
                new MenaEntity("2", "EUR", "5.5", 978, 1, 1)
        );

        when(menaRepository.findAll()).thenReturn(entities);

        List<MenaEntity> result = menaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getKod());
        assertEquals("EUR", result.get(1).getKod());
    }

    @Test
    void testFindByCode() {
        List<MenaEntity> entities = List.of(new MenaEntity("1", "USD", "10.5", 840, 2, 2));

        when(menaRepository.findByCode("USD")).thenReturn(entities);

        List<MenaEntity> result = menaService.findByCode("USD");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getKod());
    }

    @Test
    void testFindByCodeNum() {
        List<MenaEntity> entities = List.of(new MenaEntity("1", "USD", "10.5", 840, 2, 2));

        when(menaRepository.findByCodeNum(840)).thenReturn(entities);

        List<MenaEntity> result = menaService.findByCodeNum(840);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(840, result.get(0).getKodnum());
    }
}
