package FacadeTests.Email;

import cz.inspire.email.dto.EmailQueueDto;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.facade.EmailQueueFacade;
import cz.inspire.email.mapper.EmailQueueMapper;
import cz.inspire.email.service.EmailQueueService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EmailQueueFacadeTest {

    @Mock
    private EmailQueueService emailQueueService;

    @Mock
    private EmailQueueMapper emailQueueMapper;

    @InjectMocks
    private EmailQueueFacade emailQueueFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        when(emailQueueMapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(emailQueueService).create(entity);

        String result = emailQueueFacade.create(dto);

        assertEquals("queue123", result);
        verify(emailQueueMapper, times(1)).toEntity(dto);
        verify(emailQueueService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        when(emailQueueMapper.toEntity(dto)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(CreateException.class, () -> emailQueueFacade.create(dto));

        verify(emailQueueMapper, times(1)).toEntity(dto);
        verify(emailQueueService, never()).create(any(EmailQueueEntity.class));
    }

    @Test
    void testMapToDto() {
        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        EmailQueueDto expectedDto = new EmailQueueDto();
        expectedDto.setId("queue123");

        when(emailQueueMapper.toDto(entity)).thenReturn(expectedDto);

        EmailQueueDto result = emailQueueFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("queue123", result.getId());

        verify(emailQueueMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindAll() {
        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        when(emailQueueService.findAll()).thenReturn(List.of(entity));
        when(emailQueueMapper.toDto(entity)).thenReturn(dto);

        List<EmailQueueDto> result = emailQueueFacade.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("queue123", result.getFirst().getId());

        verify(emailQueueService, times(1)).findAll();
        verify(emailQueueMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindFirstMail_Found() {
        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        when(emailQueueService.findFirstMail()).thenReturn(Optional.of(entity));
        when(emailQueueMapper.toDto(entity)).thenReturn(dto);

        Optional<EmailQueueDto> result = emailQueueFacade.findFirstMail();

        assertTrue(result.isPresent());
        assertEquals("queue123", result.get().getId());

        verify(emailQueueService, times(1)).findFirstMail();
        verify(emailQueueMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindFirstMail_NotFound() {
        when(emailQueueService.findFirstMail()).thenReturn(Optional.empty());

        Optional<EmailQueueDto> result = emailQueueFacade.findFirstMail();

        assertTrue(result.isEmpty());

        verify(emailQueueService, times(1)).findFirstMail();
        verify(emailQueueMapper, never()).toDto(any());
    }

    @Test
    void testFindByHistory() {
        String historyId = "history123";
        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        when(emailQueueService.findByHistory(historyId)).thenReturn(List.of(entity));
        when(emailQueueMapper.toDto(entity)).thenReturn(dto);

        List<EmailQueueDto> result = emailQueueFacade.findByHistory(historyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("queue123", result.getFirst().getId());

        verify(emailQueueService, times(1)).findByHistory(historyId);
        verify(emailQueueMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByDependentHistory() {
        String historyId = "history123";
        EmailQueueEntity entity = new EmailQueueEntity();
        entity.setId("queue123");

        EmailQueueDto dto = new EmailQueueDto();
        dto.setId("queue123");

        when(emailQueueService.findByDependentHistory(historyId)).thenReturn(List.of(entity));
        when(emailQueueMapper.toDto(entity)).thenReturn(dto);

        List<EmailQueueDto> result = emailQueueFacade.findByDependentHistory(historyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("queue123", result.getFirst().getId());

        verify(emailQueueService, times(1)).findByDependentHistory(historyId);
        verify(emailQueueMapper, times(1)).toDto(entity);
    }
}
