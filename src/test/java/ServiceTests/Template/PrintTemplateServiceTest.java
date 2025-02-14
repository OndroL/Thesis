package ServiceTests.Template;

import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import cz.inspire.template.service.PrintTemplateService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrintTemplateServiceTest {

    @Mock
    private PrintTemplateRepository printTemplateRepository;

    @Mock
    private EntityManager em;

    private PrintTemplateService printTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        printTemplateService = new PrintTemplateService(printTemplateRepository);
        printTemplateService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        printTemplateService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> printTemplateService.create(entity));
        assertEquals("Failed to create PrintTemplateEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        printTemplateService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> printTemplateService.update(entity));
        assertEquals("Failed to update PrintTemplateEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        printTemplateService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> printTemplateService.delete(entity));
        assertEquals("Failed to remove PrintTemplateEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testFindById_Success() throws FinderException {
        PrintTemplateEntity expectedEntity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.findById("1")).thenReturn(Optional.of(expectedEntity));

        Optional<PrintTemplateEntity> result = printTemplateService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(printTemplateRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        when(printTemplateRepository.findById("1")).thenReturn(Optional.empty());

        Optional<PrintTemplateEntity> result = printTemplateService.findById("1");

        assertFalse(result.isPresent());
        verify(printTemplateRepository, times(1)).findById("1");
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> printTemplateService.delete(null));
        assertEquals("Cannot delete null entity in PrintTemplateEntity", exception.getMessage());
    }
}
