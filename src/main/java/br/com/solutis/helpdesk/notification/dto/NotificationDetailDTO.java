package br.com.solutis.helpdesk.notification.dto;

import java.time.LocalDateTime;

import br.com.solutis.helpdesk.notification.model.Notification;

public record NotificationDetailDTO(
    Long id,
    Long ticketId,
    Long recipientId,
    String title,
    String message,
    boolean read,
    LocalDateTime createdAt
) {

    public NotificationDetailDTO(Notification notification) {
        this(
            notification.getId(),
            notification.getTickedId(),
            notification.getRecipientId(),
            notification.getType(),
            notification.getMessage(),
            notification.isRead(),
            notification.getCreatedAt());
    }

}
