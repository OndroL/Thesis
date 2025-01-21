package ServiceTests.Template;

import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import cz.inspire.template.service.PrintTemplateService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrintTemplateServiceTest {

    @Mock
    private PrintTemplateRepository printTemplateRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private PrintTemplateService printTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.create(entity);

        verify(printTemplateRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(RuntimeException.class).when(printTemplateRepository).save(entity);

        assertThrows(CreateException.class, () -> printTemplateService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create PrintTemplateEntity"), any(RuntimeException.class));
    }

    @Test
    void testUpdate_Success() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.update(entity);

        verify(printTemplateRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(RuntimeException.class).when(printTemplateRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> printTemplateService.update(entity));
        assertEquals("Failed to update PrintTemplateEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update PrintTemplateEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.delete(entity);

        verify(printTemplateRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(RuntimeException.class).when(printTemplateRepository).delete(entity);

        SystemException exception = assertThrows(SystemException.class, () -> printTemplateService.delete(entity));
        assertEquals("Failed to remove PrintTemplateEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove PrintTemplateEntity"), any(RuntimeException.class));
    }

    @Test
    void testFindById_Success() {
        PrintTemplateEntity expectedEntity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        when(printTemplateRepository.findById("1")).thenReturn(Optional.of(expectedEntity));

        Optional<PrintTemplateEntity> result = printTemplateService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(printTemplateRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(printTemplateRepository.findById("1")).thenReturn(Optional.empty());

        Optional<PrintTemplateEntity> result = printTemplateService.findById("1");

        assertFalse(result.isPresent());
        verify(printTemplateRepository, times(1)).findById("1");
    }
}
