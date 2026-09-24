package br.com.solutis.helpdesk.notification.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import br.com.solutis.helpdesk.notification.model.Notification;

@SpringBootTest
@Transactional
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        
        notification1 = new Notification(null, 10L, 20L, "TICKET_CREATED", "Message 1", false, LocalDateTime.now());
        notification2 = new Notification(null, 11L, 21L, "TICKET_ASSIGNED", "Message 2", true, LocalDateTime.now());
        
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
    }

    @Test
    @DisplayName("Should find notifications by recipient ID with pagination")
    void shouldFindAllByRecipientId() {
        Page<Notification> result = notificationRepository.findAllByRecipientId(20L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("TICKET_CREATED", result.getContent().get(0).getType());
    }

    @Test
    @DisplayName("Should filter notifications with exact match for ticketId and recipientId")
    void shouldFindAllFilter_WithExactMatch() {
        Page<Notification> result = notificationRepository.findAllFilter(10L, 20L, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(notification1.getId(), result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should filter notifications by read status")
    void shouldFindAllFilter_ByReadStatus() {
        Page<Notification> result = notificationRepository.findAllFilter(null, null, null, true, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).isRead());
        assertEquals("TICKET_ASSIGNED", result.getContent().get(0).getType());
    }

    @Test
    @DisplayName("Should return all notifications when no filters are applied")
    void shouldFindAllFilter_NoFilters() {
        Page<Notification> result = notificationRepository.findAllFilter(null, null, null, null, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }
}
