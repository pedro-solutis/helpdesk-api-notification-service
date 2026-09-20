package br.com.solutis.helpdesk.notification.dto;

public record NotificationDetailDTO(
    String title,
    String message,
    Long ticketId,
    Long recipientId
) {

}
