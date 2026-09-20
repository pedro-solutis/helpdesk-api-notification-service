package br.com.solutis.helpdesk.notification.dto;

public record TicketEventDTO(
    Long ticketId,
    Long recipientId,
    String eventType,
    String title,
    String message
) {

}
