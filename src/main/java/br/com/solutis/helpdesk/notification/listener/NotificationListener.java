package br.com.solutis.helpdesk.notification.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.solutis.helpdesk.notification.dto.TicketEventDTO;
import br.com.solutis.helpdesk.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j 
public class NotificationListener {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "${api.messager.queue}")
    public void handleTicketEvent(TicketEventDTO event) {
        log.info("Recebido evento do RabbitMQ: {}", event);
        try {
            notificationService.processTicketEvent(event);
            log.info("Notificação gerada com sucesso para o ticket: {}", event.ticketId());
        } catch (Exception e) {
            log.error("Erro ao processar evento do ticket: {}", event.ticketId(), e);
            throw e;
        }
    }

}
