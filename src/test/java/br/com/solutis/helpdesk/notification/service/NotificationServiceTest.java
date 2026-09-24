package br.com.solutis.helpdesk.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import br.com.solutis.helpdesk.notification.dto.TicketEventDTO;
import br.com.solutis.helpdesk.notification.exception.ReadNotificationException;
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
    @DisplayName("Should return notification by ID when it exists")
    void shouldReturnNotificationById_WhenExists() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationDetailDTO result = notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals(notification.getTicketId(), result.ticketId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when notification by ID does not exist")
    void shouldThrowException_WhenNotificationByIdNotFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationById(99L);
        });
    }

    @Test
    @DisplayName("Should process TICKET_CREATED event and save notification")
    void shouldProcessTicketEvent_AndSaveNotification() {
        TicketEventDTO event = new TicketEventDTO(100L, 200L, "TICKET_CREATED", "Problema no PC", "Descricao");

        notificationService.processTicketEvent(event);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should return all notifications paginated without filters")
    void shouldReturnAllNotificationsPaged() {
        Page<Notification> pagedResponse = new PageImpl<>(List.of(notification));
        when(notificationRepository.findAll(any(PageRequest.class))).thenReturn(pagedResponse);

        Page<NotificationListDTO> result = notificationService.getAllNotifications(null, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("Should return all notifications paginated with filters applied")
    void shouldReturnAllNotificationsPagedWithFilters() {
        Page<Notification> pagedResponse = new PageImpl<>(List.of(notification));
        when(notificationRepository.findAllFilter(100L, 200L, "TICKET_CREATED", false, PageRequest.of(0, 10)))
                .thenReturn(pagedResponse);

        Page<NotificationListDTO> result = notificationService.getAllNotifications(100L, 200L, "TICKET_CREATED", false, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(notificationRepository).findAllFilter(100L, 200L, "TICKET_CREATED", false, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Should process TICKET_ASSIGNED event and save notification")
    void shouldProcessTicketEventAssigned_AndSaveNotification() {
        TicketEventDTO event = new TicketEventDTO(100L, 200L, "TICKET_ASSIGNED", "Problema no PC", "Descricao");
        notificationService.processTicketEvent(event);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should process TICKET_STATUS_CHANGED event and save notification")
    void shouldProcessTicketEventStatusChanged_AndSaveNotification() {
        TicketEventDTO event = new TicketEventDTO(100L, 200L, "TICKET_STATUS_CHANGED", "Problema no PC", "Descricao");
        notificationService.processTicketEvent(event);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should process default event type and save notification")
    void shouldProcessTicketEventDefault_AndSaveNotification() {
        TicketEventDTO event = new TicketEventDTO(100L, 200L, "OTHER_EVENT", "Problema no PC", "Descricao");
        notificationService.processTicketEvent(event);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should mark notification as read when it exists and is currently unread")
    void shouldReadNotification_WhenExistsAndUnread() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        
        notificationService.readNotification(1L);
        
        assertTrue(notification.isRead());
    }

    @Test
    @DisplayName("Should throw ReadNotificationException when trying to read an already read notification")
    void shouldThrowException_WhenReadNotificationAlreadyRead() {
        notification.read();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        
        assertThrows(ReadNotificationException.class, () -> {
            notificationService.readNotification(1L);
        });
    }
}
