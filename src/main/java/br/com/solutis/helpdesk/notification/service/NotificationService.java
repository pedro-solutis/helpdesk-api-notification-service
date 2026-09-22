package br.com.solutis.helpdesk.notification.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.dto.TicketEventDTO;
import br.com.solutis.helpdesk.notification.exception.ReadNotificationException;
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
        var notification = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        return new NotificationDetailDTO(notification);
    }

    public Page<NotificationListDTO> getNotificationByRecipientId(Long recipientId, Pageable pageable) {
        var notifications = notificationRepository.findAllByRecipientId(recipientId, pageable);
        return notifications.map(NotificationListDTO::new);
    }

    @Transactional
    public void processTicketEvent(TicketEventDTO event) {
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

    private String generateMessageForEvent(TicketEventDTO event) {
        return switch (event.eventType()) {
            case "TICKET_CREATED" -> "Um novo chamado foi criado: " + event.title();
            case "TICKET_ASSIGNED" -> "Um técnico foi atribuído ao chamado: " + event.title();
            case "TICKET_STATUS_CHANGED" -> "O status do chamado " + event.title() + " foi alterado.";
            default -> "Atualização no chamado: " + event.title();
        };
    }

    public void readNotification(Long notifcationId) {
        var notification = notificationRepository.findById(notifcationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if(notification.isRead())
            throw new ReadNotificationException("Notification was read before");
        notification.read();
    }

}
