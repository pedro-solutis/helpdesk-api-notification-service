package br.com.solutis.helpdesk.notification.dto;

import br.com.solutis.helpdesk.notification.model.Notification;

public record NotificationListDTO(
    String type, 
    String message
) {
    public NotificationListDTO(Notification notification){
        this(notification.getType(), notification.getMessage());
    }
}
