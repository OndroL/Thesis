package ServiceTests.Template;

import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import cz.inspire.template.service.PrintTemplateService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrintTemplateServiceTest {

    @Mock
    private PrintTemplateRepository printTemplateRepository;

    @Spy
    private PrintTemplateService printTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        printTemplateService = spy(new PrintTemplateService(printTemplateRepository));
    }

    @Test
    void testCreate_Success() throws CreateException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.create(entity);

        verify(printTemplateService, times(1)).create(entity);
        verify(printTemplateRepository, times(1)).insert(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(new RuntimeException("Database failure")).when(printTemplateRepository).insert(entity);

        assertThrows(CreateException.class, () -> printTemplateService.create(entity));

        verify(printTemplateService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.update(entity);

        verify(printTemplateService, times(1)).update(entity);
        verify(printTemplateRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(new RuntimeException("Database failure")).when(printTemplateRepository).save(entity);

        assertThrows(SystemException.class, () -> printTemplateService.update(entity));

        verify(printTemplateService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");

        printTemplateService.delete(entity);

        verify(printTemplateService, times(1)).delete(entity);
        verify(printTemplateRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws RemoveException {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        doThrow(new RuntimeException("Database failure")).when(printTemplateRepository).delete(entity);

        assertThrows(RemoveException.class, () -> printTemplateService.delete(entity));

        verify(printTemplateService, times(1)).delete(entity);
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
