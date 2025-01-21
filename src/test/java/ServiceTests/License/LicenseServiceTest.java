package ServiceTests.License;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import cz.inspire.license.service.LicenseService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LicenseServiceTest {

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private LicenseService licenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );

        licenseService.create(entity);

        verify(licenseRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(RuntimeException.class).when(licenseRepository).save(entity);

        assertThrows(CreateException.class, () -> licenseService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create LicenseEntity"), any(RuntimeException.class));
    }

    @Test
    void testFindAll_Success() {
        List<LicenseEntity> entities = Arrays.asList(
                new LicenseEntity("1", "Customer1", true, true, new Date(), true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L, "hash123", new Date(), new Date(), 1001),
                new LicenseEntity("2", "Customer2", false, false, new Date(), false, new Date(), false, 20, 10, 100, 40, 20, 10, 30, 20, false, 54321L, "hash456", new Date(), new Date(), 1002)
        );

        when(licenseRepository.findAll().toList()).thenReturn(entities);

        List<LicenseEntity> result = licenseService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(licenseRepository, times(1)).findAll();
    }

    @Test
    void testUpdate_Success() throws SystemException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );

        licenseService.update(entity);

        verify(licenseRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(RuntimeException.class).when(licenseRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> licenseService.update(entity));
        assertEquals("Failed to update LicenseEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update LicenseEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );

        licenseService.delete(entity);

        verify(licenseRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(RuntimeException.class).when(licenseRepository).delete(entity);

        SystemException exception = assertThrows(SystemException.class, () -> licenseService.delete(entity));
        assertEquals("Failed to remove LicenseEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove LicenseEntity"), any(RuntimeException.class));
    }
}
