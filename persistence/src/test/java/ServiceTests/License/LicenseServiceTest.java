package ServiceTests.License;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import cz.inspire.license.service.LicenseService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicenseServiceTest {

    @Mock
    private LicenseRepository licenseRepository;

    private LicenseService licenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        licenseService = new LicenseService(licenseRepository);
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
        when(licenseRepository.create(entity)).thenReturn(entity);

        LicenseEntity result = licenseService.create(entity);

        assertEquals(entity, result);
        verify(licenseRepository, times(1)).create(entity);
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
        when(licenseRepository.create(entity)).thenThrow(new RuntimeException("Database failure"));

        CreateException exception = assertThrows(CreateException.class, () -> licenseService.create(entity));
        assertEquals("Failed to create LicenseEntity", exception.getMessage());
        verify(licenseRepository, times(1)).create(entity);
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
        when(licenseRepository.update(entity)).thenReturn(entity);

        LicenseEntity result = licenseService.update(entity);

        assertEquals(entity, result);
        verify(licenseRepository, times(1)).update(entity);
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
        when(licenseRepository.update(entity)).thenThrow(new RuntimeException("Database failure"));

        SystemException exception = assertThrows(SystemException.class, () -> licenseService.update(entity));
        assertEquals("Failed to update LicenseEntity", exception.getMessage());
        verify(licenseRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doNothing().when(licenseRepository).delete(entity);

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
        doThrow(new RuntimeException("Database failure")).when(licenseRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> licenseService.delete(entity));
        assertEquals("Failed to remove LicenseEntity", exception.getMessage());
        verify(licenseRepository, times(1)).delete(entity);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<LicenseEntity> entities = List.of(
                new LicenseEntity("1", "Customer1", true, true, new Date(), true, new Date(), true, 10, 5, 50, 20, 10, 5, 15, 10, true, 12345L, "hash123", new Date(), new Date(), 1001),
                new LicenseEntity("2", "Customer2", false, false, new Date(), false, new Date(), false, 20, 10, 100, 50, 20, 10, 30, 20, false, 54321L, "hash321", new Date(), new Date(), 1002)
        );
        when(licenseRepository.findAllOrdered()).thenReturn(entities);

        List<LicenseEntity> result = licenseService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(licenseRepository, times(1)).findAllOrdered();
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> licenseService.delete(null));
        assertEquals("Cannot delete null as LicenseEntity", exception.getMessage());
    }
}
