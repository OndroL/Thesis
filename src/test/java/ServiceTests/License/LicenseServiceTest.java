package ServiceTests.License;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import cz.inspire.license.service.LicenseService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicenseServiceTest {

    @Mock
    private LicenseRepository licenseRepository;

    @Spy
    private LicenseService licenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        licenseService = spy(new LicenseService(licenseRepository));
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

        verify(licenseService, times(1)).create(entity);
        verify(licenseRepository, times(1)).insert(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(new RuntimeException("Database failure")).when(licenseRepository).insert(entity);

        assertThrows(CreateException.class, () -> licenseService.create(entity));

        verify(licenseService, times(1)).create(entity);
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

        verify(licenseService, times(1)).update(entity);
        verify(licenseRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(new RuntimeException("Database failure")).when(licenseRepository).save(entity);

        assertThrows(SystemException.class, () -> licenseService.update(entity));

        verify(licenseService, times(1)).update(entity);
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

        licenseService.delete(entity);

        verify(licenseService, times(1)).delete(entity);
        verify(licenseRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws RemoveException {
        LicenseEntity entity = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        doThrow(new RuntimeException("Database failure")).when(licenseRepository).delete(entity);

        assertThrows(RemoveException.class, () -> licenseService.delete(entity));

        verify(licenseService, times(1)).delete(entity);
    }
}
