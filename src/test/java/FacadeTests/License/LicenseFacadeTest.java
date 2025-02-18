package FacadeTests.License;

import cz.inspire.license.dto.LicenseDto;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.facade.LicenseFacade;
import cz.inspire.license.mapper.LicenseMapper;
import cz.inspire.license.service.LicenseService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LicenseFacadeTest {

    @Mock
    private LicenseService licenseService;

    @Mock
    private LicenseMapper licenseMapper;

    @InjectMocks
    private LicenseFacade licenseFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        // Input DTO
        LicenseDto dto = new LicenseDto(null, 1001, "Customer1", true, true, true,
                new Date(), true, new Date(), 10, 5, 50, 20, 10, 5, 15, true, 12345L,
                10, "hash123", new Date(), new Date());

        // Mapped Entity
        LicenseEntity entity = new LicenseEntity(null, "Customer1", true, true, new Date(),
                true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L,
                "hash123", new Date(), new Date(), 1001);

        // Entity with generated ID
        LicenseEntity savedEntity = new LicenseEntity("generated-id", "Customer1", true, true,
                new Date(), true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L,
                "hash123", new Date(), new Date(), 1001);

        when(licenseMapper.toEntity(dto)).thenReturn(entity);
        doAnswer(invocation -> {
            entity.setId(LicenseUtil.generateGUID(entity));
            return null;
        }).when(licenseService).create(entity);

        // Test
        String result = licenseFacade.create(dto);

        assertNotNull(result);
        assertEquals(entity.getId(), result);
        verify(licenseMapper, times(1)).toEntity(dto);
        verify(licenseService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        LicenseDto dto = new LicenseDto(null, 1001, "Customer1", true, true, true,
                new Date(), true, new Date(), 10, 5, 50, 20, 10, 5, 15, true, 12345L,
                10, "hash123", new Date(), new Date());

        LicenseEntity entity = new LicenseEntity(null, "Customer1", true, true, new Date(),
                true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L,
                "hash123", new Date(), new Date(), 1001);

        when(licenseMapper.toEntity(dto)).thenReturn(entity);
        doThrow(new RuntimeException("Simulated failure")).when(licenseService).create(entity);

        assertThrows(CreateException.class, () -> licenseFacade.create(dto));
        verify(licenseMapper, times(1)).toEntity(dto);
        verify(licenseService, times(1)).create(entity);
    }

    @Test
    void testMapToDto() {
        LicenseEntity entity = new LicenseEntity("1", "Customer1", true, true, new Date(),
                true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L,
                "hash123", new Date(), new Date(), 1001);

        LicenseDto dto = new LicenseDto("1", 1001, "Customer1", true, true, true,
                new Date(), true, new Date(), 10, 5, 50, 20, 10, 5, 15, true, 12345L,
                10, "hash123", new Date(), new Date());

        when(licenseMapper.toDto(entity)).thenReturn(dto);

        LicenseDto result = licenseFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        verify(licenseMapper, times(1)).toDto(entity);
    }

    @Test
    void testMapToEntity() {
        LicenseDto dto = new LicenseDto("1", 1001, "Customer1", true, true, true,
                new Date(), true, new Date(), 10, 5, 50, 20, 10, 5, 15, true, 12345L,
                10, "hash123", new Date(), new Date());

        LicenseEntity entity = new LicenseEntity("1", "Customer1", true, true, new Date(),
                true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L,
                "hash123", new Date(), new Date(), 1001);

        when(licenseMapper.toEntity(dto)).thenReturn(entity);

        LicenseEntity result = licenseFacade.mapToEntity(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(licenseMapper, times(1)).toEntity(dto);
    }
}
