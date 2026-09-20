package br.com.solutis.helpdesk.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.dto.TicketEvent;
import br.com.solutis.helpdesk.notification.exception.ResourceNotFoundException;
import br.com.solutis.helpdesk.notification.model.Notification;
import br.com.solutis.helpdesk.notification.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification(1L, 100L, 200L, "TICKET_CREATED", "Mensagem", false, LocalDateTime.now());
    }

    @Test
    void shouldReturnNotificationById_WhenExists() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationDetailDTO result = notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals(notification.getTickedId(), result.ticketId());
    }

    @Test
    void shouldThrowException_WhenNotificationByIdNotFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationById(99L);
        });
    }

    @Test
    void shouldProcessTicketEvent_AndSaveNotification() {
        TicketEvent event = new TicketEvent(100L, 200L, "TICKET_CREATED", "Problema no PC", "Descricao");

        notificationService.processTicketEvent(event);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldReturnAllNotificationsPaged() {
        Page<Notification> pagedResponse = new PageImpl<>(List.of(notification));
        when(notificationRepository.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        Page<NotificationListDTO> result = notificationService.getAllNotifications(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}

