package br.com.solutis.helpdesk.notification.dto;

import br.com.solutis.helpdesk.notification.model.Notification;

public record NotificationListDTO(
    Long id,
    String type, 
    String message,
    Long ticketId
) {
    public NotificationListDTO(Notification notification){
        this(notification.getId(), notification.getType(), notification.getMessage(), notification.getTicketId());
    }
}
