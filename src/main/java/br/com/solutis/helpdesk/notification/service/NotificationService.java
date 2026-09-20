package br.com.solutis.helpdesk.notification.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.dto.TicketEvent;
import br.com.solutis.helpdesk.notification.exception.ResourceNotFoundException;
import br.com.solutis.helpdesk.notification.model.Notification;
import br.com.solutis.helpdesk.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;

@Service 
public class NotificationService {

    @Autowired 
    private NotificationRepository notificationRepository;

    public Page<NotificationListDTO> getAllNotifications(Pageable pageable) {
        var notifications = notificationRepository.findAll(pageable);
        return notifications.map(NotificationListDTO::new);
    }

    public NotificationDetailDTO getNotificationById(Long notificationId) {
        var ticket = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        return new NotificationDetailDTO(ticket.getType(), ticket.getMessage(), ticket.getTickedId(), ticket.getRecipientId());
    }

    @Transactional
    public void processTicketEvent(TicketEvent event) {
        String message = generateMessageForEvent(event);
        
        Notification notification = new Notification(
                null, 
                event.ticketId(), 
                event.recipientId() != null ? event.recipientId() : 0L,
                event.eventType(), 
                message, 
                false,
                LocalDateTime.now()
        );
        
        notificationRepository.save(notification);
    }

    private String generateMessageForEvent(TicketEvent event) {
        return switch (event.eventType()) {
            case "TICKET_CREATED" -> "Um novo chamado foi criado: " + event.title();
            case "TICKET_ASSIGNED" -> "Um técnico foi atribuído ao chamado: " + event.title();
            case "TICKET_STATUS_CHANGED" -> "O status do chamado " + event.title() + " foi alterado.";
            default -> "Atualização no chamado: " + event.title();
        };
    }

}
