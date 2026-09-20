package br.com.solutis.helpdesk.notification.listener;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.solutis.helpdesk.notification.dto.TicketEventDTO;
import br.com.solutis.helpdesk.notification.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void shouldHandleTicketEventAndCallService() {
        TicketEventDTO event = new TicketEventDTO(10L, 5L, "TICKET_CREATED", "Erro na rede", "Fios cortados");

        notificationListener.handleTicketEvent(event);

        verify(notificationService).processTicketEvent(event);
    }
}

