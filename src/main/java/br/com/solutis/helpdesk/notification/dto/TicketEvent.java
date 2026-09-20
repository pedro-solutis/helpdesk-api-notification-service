package br.com.solutis.helpdesk.notification.dto;

public record TicketEvent(
    Long ticketId,
    Long recipientId, // Quem vai receber a notificacao
    String eventType, // TICKET_CREATED, TICKET_ASSIGNED, TICKET_STATUS_CHANGED
    String title,
    String description
) {

}
