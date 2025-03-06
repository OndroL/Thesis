package ServiceTests.Template;

import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import cz.inspire.template.service.PrintTemplateService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrintTemplateServiceTest {

    @Mock
    private PrintTemplateRepository printTemplateRepository;

    private PrintTemplateService printTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        printTemplateService = new PrintTemplateService(printTemplateRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.create(entity)).thenReturn(entity);

        PrintTemplateEntity result = printTemplateService.create(entity);

        assertEquals(entity, result);
        verify(printTemplateRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.create(entity)).thenThrow(new RuntimeException("Database failure"));

        CreateException exception = assertThrows(CreateException.class, () -> printTemplateService.create(entity));
        assertEquals("Failed to create PrintTemplateEntity", exception.getMessage());
        verify(printTemplateRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.update(entity)).thenReturn(entity);

        PrintTemplateEntity result = printTemplateService.update(entity);

        assertEquals(entity, result);
        verify(printTemplateRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.update(entity)).thenThrow(new RuntimeException("Database failure"));

        SystemException exception = assertThrows(SystemException.class, () -> printTemplateService.update(entity));
        assertEquals("Failed to update PrintTemplateEntity", exception.getMessage());
        verify(printTemplateRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doNothing().when(printTemplateRepository).delete(entity);

        printTemplateService.delete(entity);

        verify(printTemplateRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(new RuntimeException("Database failure")).when(printTemplateRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> printTemplateService.delete(entity));
        assertEquals("Failed to remove PrintTemplateEntity", exception.getMessage());
        verify(printTemplateRepository, times(1)).delete(entity);
    }

    @Test
    void testFindById_Success() throws FinderException {
        PrintTemplateEntity expectedEntity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.findByPrimaryKey("1")).thenReturn(expectedEntity);

        PrintTemplateEntity result = printTemplateService.findByPrimaryKey("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(printTemplateRepository, times(1)).findByPrimaryKey("1");
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        when(printTemplateRepository.findByPrimaryKey("1")).thenReturn(null);

        PrintTemplateEntity result = printTemplateService.findByPrimaryKey("1");

        assertNull(result);
        verify(printTemplateRepository, times(1)).findByPrimaryKey("1");
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> printTemplateService.delete(null));
        assertEquals("Cannot delete null as PrintTemplateEntity", exception.getMessage());
    }
}
