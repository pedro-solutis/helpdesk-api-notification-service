package br.com.solutis.helpdesk.notification.dto;

import br.com.solutis.helpdesk.notification.model.Notification;
import java.time.LocalDateTime;

public record NotificationListDTO(
    Long id,
    String type,
    String message,
    Long ticketId,
    boolean read,
    LocalDateTime createdAt
) {
    public NotificationListDTO(Notification notification){
        this(notification.getId(), notification.getType(), notification.getMessage(), notification.getTicketId(), notification.isRead(), notification.getCreatedAt());
    }
}
