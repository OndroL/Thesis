package ServiceTests.License;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import cz.inspire.license.service.LicenseService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
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

    @Mock
    private EntityManager em;

    private LicenseService licenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        licenseService = new LicenseService(licenseRepository);
        licenseService.setEntityManager(em);
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

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        licenseService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
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

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> licenseService.create(entity));
        assertEquals("Failed to create LicenseEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
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

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        licenseService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
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

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> licenseService.update(entity));
        assertEquals("Failed to update LicenseEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
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

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        licenseService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
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

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> licenseService.delete(entity));
        assertEquals("Failed to remove LicenseEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
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
        assertEquals("Cannot delete null entity in LicenseEntity", exception.getMessage());
    }
}
