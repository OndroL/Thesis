package ServiceTests.Sport;

import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.repository.OmezeniRezervaciRepository;
import cz.inspire.sport.service.OmezeniRezervaciService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmezeniRezervaciServiceTest {

    @Mock
    private OmezeniRezervaciRepository omezeniRezervaciRepository;

    private OmezeniRezervaciService omezeniRezervaciService;

    @BeforeEach
    void setUp() {
        omezeniRezervaciService = new OmezeniRezervaciService(omezeniRezervaciRepository);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<OmezeniRezervaciEntity> expectedEntities = List.of(
                new OmezeniRezervaciEntity("objekt1", null),
                new OmezeniRezervaciEntity("objekt2", null)
        );
        when(omezeniRezervaciRepository.findAllOrdered()).thenReturn(expectedEntities);

        List<OmezeniRezervaciEntity> result = omezeniRezervaciService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(omezeniRezervaciRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(omezeniRezervaciRepository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> omezeniRezervaciService.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all OmezeniRezervaciEntity records"));

        verify(omezeniRezervaciRepository, times(1)).findAllOrdered();
    }
}
